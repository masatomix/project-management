/******************************************************************************
 * Copyright (c) 2014 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//作成日: 2015/01/27

package nu.mine.kino.jenkins.plugins.projectmanagement.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletOutputStream;

import hudson.Extension;
import hudson.FilePath;
import hudson.FilePath.FileCallable;
import hudson.cli.CLICommand;
import hudson.model.AbstractProject;
import hudson.remoting.VirtualChannel;

import nu.mine.kino.jenkins.plugins.projectmanagement.PMConstants;
import nu.mine.kino.jenkins.plugins.projectmanagement.utils.PMUtils;
import nu.mine.kino.projects.ExcelProjectCreator;
import nu.mine.kino.projects.ProjectException;
import nu.mine.kino.projects.utils.WriteUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.kohsuke.args4j.Argument;

/**
 * JenkinsによってEVM Excelマクロファイルの日替わり処理を実施します。
 * 
 * @author Masatomi KINO
 * @version $Revision$
 */
@Extension
public class HigawariCommand extends CLICommand {

    @Argument(metaVar = "JOB", usage = "日替わり処理を行うプロジェクトを指定します。", index = 0, required = true)
    public AbstractProject<?, ?> job;

    @Argument(metaVar = "FILE", usage = "EVMファイル名を指定します", index = 1, required = true)
    public String fileName;

    @Argument(metaVar = "PREFIX", usage = "日替わり処理を行うファイルのprefixを指定します。", index = 2, required = false)
    public String prefix;

    @Override
    public String getShortDescription() {
        return "指定したプロジェクトの日替わり処理を行います。";
    }

    @Override
    protected int run() throws Exception {
        // 相対的に指定されたファイルについて、ワークスペースルートにファイルコピーします。
        FilePath someWorkspace = job.getSomeWorkspace();
        FilePath source = new FilePath(someWorkspace, fileName + ".tmp");
        stdout.println(source);
        stdout.println("このファイルの日替わり処理を行います。");
        if (source.exists()) {
            String tmpPrefix = prefix;
            if (StringUtils.isEmpty(prefix)) {
                tmpPrefix = "base";
            }

            String destFileName = tmpPrefix + "_"
                    + (new FilePath(someWorkspace, fileName).getName());
            FilePath dest = new FilePath(someWorkspace, destFileName);
            source.copyTo(dest);
            stdout.println("copy to");
            stdout.println(dest);

            source.act(new CopyExecutor());

        } else {
            stderr.println("指定したファイルが存在しませんでした。");
            return -1;
        }
        return 0;
    }

    private static class CopyExecutor implements FileCallable<Void> {

        @Override
        public Void invoke(File f, VirtualChannel channel) throws IOException,
                InterruptedException {
            Date baseDate = PMUtils.getBaseDateFromExcel(f);
            if (baseDate != null) {
                String baseDateStr = DateFormatUtils.format(baseDate,
                        "yyyyMMdd");
                WriteUtils
                        .writeFile(baseDateStr.getBytes(),
                                new File(f.getParentFile(),
                                        PMConstants.DATE_DAT_FILENAME));
            }
            return null;
        }

    }

}
