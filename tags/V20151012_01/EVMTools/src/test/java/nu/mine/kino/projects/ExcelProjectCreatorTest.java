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
//作成日: 2015/09/19

package nu.mine.kino.projects;

import java.io.File;

import junit.framework.Assert;
import nu.mine.kino.entity.Project;
import nu.mine.kino.entity.TaskInformation;
import nu.mine.kino.projects.utils.Utils;

import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class ExcelProjectCreatorTest {

    @Test
    public void test() throws ProjectException {
        String fileName = "testdata";

        File baseDir = new File("./");
        String input = fileName + "." + "xls";
        File file = new File(baseDir, input);

        StopWatch watch = new StopWatch();
        watch.start();
        Project project = new ExcelProjectCreator(file).createProject();
        watch.stop();
        System.out.println(watch.getTime() + " ms.");

        TaskInformation[] infos = project.getTaskInformations();
        System.out.println(infos.length);
        checkPV(infos);
        checkAC(infos);
        checkEV(infos);
        checkNumberOfDays(infos);

        checkProjectInfo(project);

    }

    private void checkProjectInfo(Project project) {
        Assert.assertEquals(Utils.str2Date("2014/11/04"), project.getBaseDate());
        Assert.assertEquals(Utils.str2Date("2014/10/26"),
                project.getProjectStartDate());
        Assert.assertEquals(Utils.str2Date("2014/12/31"),
                project.getProjectEndDate());
        Assert.assertEquals(23, project.getTaskInformations().length);
        Assert.assertEquals(93, project.getTaskInformations()[0].getTask()
                .getPlotDataMap().size() + 26); //ヘッダの数は、固定の26コ + 日付データ67コ。

    }

    /**
     * 稼働予定日数の確認。
     * 
     * @param infos
     */
    private void checkNumberOfDays(TaskInformation[] infos) {
        System.out.println("--- check NumberOfDays ----");
        for (TaskInformation info : infos) {
            System.out.printf("%s:%s\n", info.getTaskId(), info.getTask()
                    .getNumberOfDays());
        }

        TaskInformation target = null;
        target = infos[0];
        Assert.assertEquals(0, target.getTask().getNumberOfDays());
        target = infos[6];
        Assert.assertEquals(9, target.getTask().getNumberOfDays());// 0.4
        target = infos[2];
        Assert.assertEquals(2, target.getTask().getNumberOfDays());// 0.5
        target = infos[3];
        Assert.assertEquals(2, target.getTask().getNumberOfDays());// 0.0

    }

    private void checkAC(TaskInformation[] infos) {
        System.out.println("--- check AC ----");
        for (TaskInformation info : infos) {
            System.out.printf("%s:%s\n", info.getTaskId(), info.getAC()
                    .getActualCost());
        }

        TaskInformation target = null;
        target = infos[0];
        Assert.assertEquals(Double.NaN, target.getAC().getActualCost());
        target = infos[6];
        Assert.assertEquals(0.1, target.getAC().getActualCost());// 0.4
        target = infos[2];
        Assert.assertEquals(0.6, target.getAC().getActualCost());// 0.5
        target = infos[3];
        Assert.assertEquals(Double.NaN, target.getAC().getActualCost());// 0.0

    }

    private void checkEV(TaskInformation[] infos) {
        System.out.println("--- check EV ----");
        for (TaskInformation info : infos) {
            System.out.printf("%s:%s\n", info.getTaskId(), info.getEV()
                    .getEarnedValue());
        }

        TaskInformation target = null;
        double progressRate = Double.NaN;

        // #は入ってて有効で、行にデータなしのケース
        target = infos[0];
        Assert.assertEquals(Double.NaN, target.getEV().getEarnedValue());
        Assert.assertEquals(Double.NaN, target.getEV().getProgressRate());
        Assert.assertNull(target.getEV().getStartDate());
        Assert.assertNull(target.getEV().getEndDate());

        // 通常ケース
        target = infos[6];
        progressRate = 0.8;
        Assert.assertEquals(0.5 * progressRate, target.getEV().getEarnedValue());// 0.4
        Assert.assertEquals(progressRate, target.getEV().getProgressRate());
        Assert.assertEquals(Utils.str2Date("2014/11/04"), target.getEV()
                .getStartDate());
        Assert.assertNull("getEndDateがnullでない", target.getEV().getEndDate());

        // 通常ケース
        target = infos[2];
        progressRate = 0.5;
        Assert.assertEquals(1.0 * progressRate, target.getEV().getEarnedValue());// 0.5
        Assert.assertEquals(progressRate, target.getEV().getProgressRate());
        Assert.assertEquals(Utils.str2Date("2014/11/04"), target.getEV()
                .getStartDate());
        Assert.assertNull("getEndDateがnullでない", target.getEV().getEndDate());

        // 通常ケース まだ進捗が開始されていないケース
        target = infos[3];
        progressRate = 0.0;
        Assert.assertEquals(0.5 * progressRate, target.getEV().getEarnedValue());// 0.0
        Assert.assertEquals(Double.NaN, target.getEV().getProgressRate()); // NaN
        Assert.assertNull("getStartDateがnullでない", target.getEV().getStartDate());
        Assert.assertNull("getEndDateがnullでない", target.getEV().getEndDate());

        // 通常ケース
        target = infos[7];
        progressRate = 1.0;
        Assert.assertEquals(0.5 * progressRate, target.getEV().getEarnedValue());// 0.4
        Assert.assertEquals(progressRate, target.getEV().getProgressRate());
        Assert.assertEquals(Utils.str2Date("2014/11/04"), target.getEV()
                .getStartDate());
        Assert.assertEquals(Utils.str2Date("2014/11/04"), target.getEV()
                .getEndDate());

    }

    private void checkPV(TaskInformation[] infos) {
        System.out.println("--- check PV ----");

        for (TaskInformation info : infos) {
            System.out.printf("%s:%s\n", info.getTaskId(), info.getPV()
                    .getPlannedValue());
        }

        TaskInformation target = null;
        target = infos[0];
        Assert.assertEquals(Double.NaN, target.getPV().getPlannedValue());
        target = infos[6];
        Assert.assertEquals(0.5 / 9.0 * 1, target.getPV().getPlannedValue());// 0.05555555555555555
        target = infos[2];
        Assert.assertEquals(1.0 / 2.0 * 1, target.getPV().getPlannedValue());// 0.5
        target = infos[3];
        Assert.assertEquals(0.0, target.getPV().getPlannedValue());// 0.0
    }
}
