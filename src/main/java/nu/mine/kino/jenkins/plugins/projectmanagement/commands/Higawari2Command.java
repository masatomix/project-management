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
import nu.mine.kino.projects.ProjectException;
import nu.mine.kino.projects.utils.ProjectUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.jenkinsci.remoting.RoleChecker;
import org.kohsuke.args4j.Argument;

/**
 * JenkinsによってEVM Excelマクロファイルの日替わり処理を実施します。
 * 
 * @author Masatomi KINO
 * @version $Revision$
 */
@Extension
public class Higawari2Command extends CLICommand {

    @Argument(metaVar = "JOB", usage = "日替わり処理を行うプロジェクトを指定します。", index = 0, required = true)
    public AbstractProject<?, ?> job;

    @Argument(metaVar = "PREFIX", usage = "日替わり処理を行うファイルのprefixを指定します。", index = 1, required = false)
    public String prefix;

    @Override
    public String getShortDescription() {
        return "指定したプロジェクトの日替わり処理を行います。";
    }

    private static final String seriesFileNameSuffix = PMConstants.SERIES_DAT_FILENAME;

    @Override
    protected int run() throws Exception {
        String originalExcelFileName = PMUtils.findProjectFileName(job);
        if (originalExcelFileName == null) {
            throw new ProjectException("スケジュールファイルを見つけることができませんでした。");
        }
        stdout.println("EVMファイル名: " + originalExcelFileName);

        // 相対的に指定されたファイルについて、ワークスペースルートにファイルコピーします。
        FilePath someWorkspace = job.getSomeWorkspace();
        FilePath excelFilePath = new FilePath(someWorkspace,
                originalExcelFileName);
        String previousJsonFileName = PMUtils
                .getPreviousJsonFileName(originalExcelFileName);
        FilePath jsonSource = new FilePath(someWorkspace, previousJsonFileName);
        stdout.println(excelFilePath);
        stdout.println("このファイルの日替わり処理を行います。");
        if (jsonSource.exists()) { //
            String tmpPrefix = prefix;
            if (StringUtils.isEmpty(prefix)) {
                tmpPrefix = PMConstants.BASE;
            }

            String destFileName = tmpPrefix
                    + "_"
                    + ProjectUtils.findJSONFileName((new FilePath(
                            someWorkspace, originalExcelFileName).getName()));
            FilePath dest = new FilePath(someWorkspace, destFileName);
            jsonSource.copyTo(dest);
            stdout.println(jsonSource.getParent() + " 内 でコピー");
            stdout.println("[" + jsonSource.getName() + "] -> ["
                    + dest.getName() + "] コピー完了");

            final AbstractBuild<?, ?> shimeBuild = job.getLastSuccessfulBuild();

            String baseDateStr = jsonSource.act(new DateFileExecutor());
            PMUtils.writeBaseDateFile(baseDateStr, shimeBuild, stdout);

            // FilePath dest2 = new FilePath(
            // new FilePath(shimeBuild.getRootDir()), destFileName);
            // jsonSource.copyTo(dest2);

            stdout.println("基準日: " + baseDateStr + " を締めました。日替わり処理が正常終了しました。");

            // Prefix引数ナシの時だけ、時系列ファイルを書き込む
            String seriesFileName = tmpPrefix + "_" + seriesFileNameSuffix;
            PMUtils.writeSeriesFile(job, baseDateStr, seriesFileName,
                    shimeBuild, stdout, stderr);

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

    private static class DateFileExecutor implements FileCallable<String> {

        @Override
        public String invoke(File jsonFile, VirtualChannel channel)
                throws IOException, InterruptedException {
            Date baseDate = PMUtils.getBaseDateFromJSON(jsonFile);
            if (baseDate != null) {
                String baseDateStr = DateFormatUtils.format(baseDate,
                        "yyyyMMdd");
                return baseDateStr;
            }
            return null;
        }

        @Override
        public void checkRoles(RoleChecker checker) throws SecurityException {
            // TODO 自動生成されたメソッド・スタブ

        }

    }

}
