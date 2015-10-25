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
//作成日: 2015/10/22

package nu.mine.kino.jenkins.plugins.projectmanagement.tokenmacros;

import hudson.AbortException;
import hudson.Extension;
import hudson.model.TaskListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.model.FreeStyleProject;
import hudson.tasks.Builder;
import hudson.util.DescribableList;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nu.mine.kino.entity.Project;
import nu.mine.kino.jenkins.plugins.projectmanagement.PMConstants;
import nu.mine.kino.jenkins.plugins.projectmanagement.HigawariCheckBuilder;
import nu.mine.kino.jenkins.plugins.projectmanagement.HigawariCheckBuilder.EnableTextBlock;
import nu.mine.kino.jenkins.plugins.projectmanagement.utils.PMUtils;
import nu.mine.kino.projects.JSONProjectCreator;
import nu.mine.kino.projects.ProjectException;
import nu.mine.kino.projects.utils.ProjectUtils;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.jenkinsci.plugins.tokenmacro.DataBoundTokenMacro;
import org.jenkinsci.plugins.tokenmacro.MacroEvaluationException;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
@Extension
public class HigawariCheckResultsTokenMacro extends DataBoundTokenMacro {
    @Override
    public boolean acceptsMacroName(String macroName) {
        return macroName.equals("HIGAWARI_CHECK_RESULTS");
    }

    @Override
    public String evaluate(AbstractBuild<?, ?> context, TaskListener listener,
            String macroName) throws MacroEvaluationException, IOException,
            InterruptedException {
        EnableTextBlock useFilter = null;
        String targetProjects = null;

        // このTockeMacroが呼ばれたときに、この変数を呼び出しているプロジェクト
        // (たぶん、FreeStyleProjectで HigawariCheckBuilderが設定されている)
        // に設定されたHigawariCheckBuilderの情報を取得している。
        AbstractProject p = context.getProject();
        if (p instanceof FreeStyleProject) {
            FreeStyleProject fProject = (FreeStyleProject) p;
            DescribableList<Builder, Descriptor<Builder>> buildersList = fProject
                    .getBuildersList();
            HigawariCheckBuilder builder = buildersList
                    .get(HigawariCheckBuilder.class);
            if (builder != null) {
                useFilter = builder.getUseFilter();
                targetProjects = builder.getTargetProjects();
            }
        }

        // 以下、そのプロジェクトに設定された情報を元に、処理を動かす。
        List<AbstractProject<?, ?>> projects = null;
        if (useFilter == null) {
            projects = PMUtils.findProjectsWithEVMToolsBuilder();
        } else {
            String[] targetProjectsArray = targetProjects.split("\n");
            projects = PMUtils
                    .findProjectsWithEVMToolsBuilder(targetProjectsArray);
        }

        StringBuffer buf = new StringBuffer();
        for (AbstractProject<?, ?> project : projects) {
            File newBaseDateFile = PMUtils.findBaseDateFile(project); // buildDirの新しいファイル
            if (newBaseDateFile != null) {
                Date baseDateFromBaseDateFile = PMUtils
                        .getBaseDateFromBaseDateFile(newBaseDateFile);
                String dateStr = DateFormatUtils.format(
                        baseDateFromBaseDateFile, "yyyyMMdd");

                String msg = String
                        .format("%s\t%s", project.getName(), dateStr);
                buf.append(msg);
                if (checkNextTradingDate(project,
                        PMUtils.findProjectFileName(project))) {// 過去ならば
                    buf.append("\t日替わりチェックエラー");
                }
            } else {
                String msg = String.format("%s\t日替処理が未実施か、"
                        + "ワークスペースに存在する旧バージョンの日替ファイルしか存在しない。"
                        + "日替処理を実施後、ファイルが見つかるようになります。", project.getName());
                buf.append(msg);
            }
            buf.append("\n");
        }
        String message = new String(buf);
        return message;
    }

    /**
     * EVMスケジュールファイルの基準日の次営業日が、今日の日付より過去であるかをtrue/falseで返す
     * 過去の場合true。同日か未来の場合false
     * 
     * @param listener
     * @param jenkinsProject
     * @param builder
     * @return
     * @throws IOException
     * @throws AbortException
     */
    private boolean checkNextTradingDate(AbstractProject<?, ?> jenkinsProject,
            String evmFileName) throws IOException, AbortException {

        String evmJSONFileName = ProjectUtils.findJSONFileName(evmFileName);
        AbstractBuild<?, ?> newestBuild = PMUtils
                .findNewestBuild(jenkinsProject);
        File newestJsonFile = new File(newestBuild.getRootDir(),
                evmJSONFileName + "." + PMConstants.TMP_EXT);
        System.out.println(newestJsonFile.getAbsolutePath());
        try {
            Project evmProject = new JSONProjectCreator(newestJsonFile)
                    .createProject();
            Date nextTradingDate = ProjectUtils.nextTradingDate(evmProject);
            Date now = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
            System.out.println(DateFormatUtils.format(nextTradingDate,
                    "yyyyMMdd"));
            System.out.println(DateFormatUtils.format(now, "yyyyMMdd"));
            boolean before = nextTradingDate.before(now);
            System.out.println(before);
            return before;
        } catch (ProjectException e) {
            throw new AbortException(e.getMessage());
        }
        //
    }
}
