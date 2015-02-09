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

import hudson.Extension;
import hudson.FilePath;
import hudson.FilePath.FileCallable;
import hudson.cli.CLICommand;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.remoting.VirtualChannel;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import nu.mine.kino.jenkins.plugins.projectmanagement.PMConstants;
import nu.mine.kino.jenkins.plugins.projectmanagement.utils.PMUtils;
import nu.mine.kino.projects.utils.ReadUtils;
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

    private static final String seriesFileName = PMConstants.SERIES_DAT_FILENAME;

    @Override
    protected int run() throws Exception {
        // 相対的に指定されたファイルについて、ワークスペースルートにファイルコピーします。
        FilePath someWorkspace = job.getSomeWorkspace();
        FilePath org = new FilePath(someWorkspace, fileName);
        // FilePath excelSource = new FilePath(someWorkspace, fileName +
        // ".tmp");
        FilePath jsonSource = new FilePath(someWorkspace, fileName + ".json"
                + ".tmp");
        stdout.println(org);
        stdout.println("このファイルの日替わり処理を行います。");
        // if (excelSource.exists() && jsonSource.exists()) { //
        if (jsonSource.exists()) { //
            String tmpPrefix = prefix;
            if (StringUtils.isEmpty(prefix)) {
                tmpPrefix = "base";
            }

            String destFileName = tmpPrefix + "_"
                    + (new FilePath(someWorkspace, fileName).getName())
                    + ".json";
            FilePath dest = new FilePath(someWorkspace, destFileName);
            jsonSource.copyTo(dest);
            stdout.println("[" + jsonSource.getName() + "] -> ["
                    + dest.getName() + "] コピー完了");

            String baseDateStr = jsonSource.act(new DateFileCopyExecutor());
            stdout.println("基準日: " + baseDateStr + " を締めました。日替わり処理が正常終了しました。");

            // Prefix引数ナシの時だけ、時系列ファイルを書き込む
            if (StringUtils.isEmpty(prefix)) {
                final AbstractBuild<?, ?> shimeBuild = job
                        .getLastSuccessfulBuild();
                // stdout.printf("[%s]\n",
                // shimeBuild.getRootDir().getAbsolutePath());
                // stdout.printf("[%s]:[%s]:[%s]\n", baseDateStr,
                // shimeBuild.getNumber(), shimeBuild.getId());
                String prevData = findSeriesFile(seriesFileName);
                String currentData = appendData(prevData,
                        shimeBuild.getNumber(), baseDateStr);
                File file = new File(shimeBuild.getRootDir().getAbsolutePath(),
                        seriesFileName);
                WriteUtils.writeFile(currentData.getBytes(), file);
                stdout.printf("EVM時系列情報ファイルに情報を追記してビルド #%s に書き込みました。\n",
                        shimeBuild.getNumber());
            }

        } else {
            stderr.println("---- エラーが発生したため日替わり処理を停止します。------");
            // if (!excelSource.exists()) {
            // stderr.println("バックアップファイル:");
            // stderr.println(excelSource);
            // stderr.println("が存在しないため日替わり処理を停止します。");
            // }
            if (!jsonSource.exists()) {
                stderr.println("バックアップファイル(基準日も取得する):");
                stderr.println(jsonSource);
                stderr.println("が存在しないため日替わり処理を停止します。");
            }
            stderr.println("------------------------------------------------");
            return -1;
        }
        return 0;
    }

    private static class DateFileCopyExecutor implements FileCallable<String> {

        @Override
        public String invoke(File jsonFile, VirtualChannel channel)
                throws IOException, InterruptedException {
            Date baseDate = PMUtils.getBaseDateFromJSON(jsonFile);
            // Date baseDate = PMUtils.getBaseDateFromExcel(f);
            if (baseDate != null) {
                String baseDateStr = DateFormatUtils.format(baseDate,
                        "yyyyMMdd");
                WriteUtils.writeFile(baseDateStr.getBytes(),
                        new File(jsonFile.getParentFile(),
                                PMConstants.DATE_DAT_FILENAME));
                return baseDateStr;
            }
            return null;
        }

    }

    private String findSeriesFile(String fileName) {
        AbstractBuild<?, ?> build = PMUtils.findBuild(job, fileName);
        if (build == null) {
            stdout.println("EVM時系列情報ファイルがプロジェクト上に存在しないので、ファイルを新規作成します。");
            return null;
        } else {
            stdout.printf("EVM時系列情報ファイルが ビルド #%s 上に見つかりました。\n",
                    build.getNumber());
        }
        try {
            return ReadUtils.readFile(new File(build.getRootDir(), fileName));
        } catch (IOException e) {
            stderr.println("EVM時系列情報ファイルを探す際にエラーが発生したので、ファイルを新規作成します。");
        }
        return null;
    }

    private String appendData(String prevData, int buildNumber,
            String baseDateStr) {
        StringBuffer buffer = new StringBuffer();
        if (prevData != null) {
            buffer.append(prevData).append("\n");
        }
        return new String(buffer.append(baseDateStr).append("\t")
                .append(buildNumber));
    }
}
