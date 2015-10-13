/******************************************************************************
 * Copyright (c) 2012 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//作成日: 2014/10/15

package nu.mine.kino.projects.utils;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

import javax.swing.filechooser.FileNameExtensionFilter;

import net.arnx.jsonic.JSON;
import nu.mine.kino.entity.PVBean;
import nu.mine.kino.entity.Project;
import nu.mine.kino.entity.TaskInformation;
import nu.mine.kino.projects.ExcelProjectCreator;
import nu.mine.kino.projects.JSONProjectCreator;
import nu.mine.kino.projects.PVCreator;
import nu.mine.kino.projects.ProjectException;
import nu.mine.kino.projects.ProjectWriter;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class ProjectUtilsTest {

    @Test
    public void test0() throws FileNotFoundException, ProjectException {
        test0("project_management_tools");
        testDate("project_management_tools");
    }

    public void test0(String fileName) throws FileNotFoundException,
            ProjectException {
        File baseDir = new File("./");
        // String fileName = "project_management_tools";
        String input = fileName + "." + "xls";

        // java.io.InputStream in = null;
        File target = new File(baseDir, input);
        // in = new java.io.FileInputStream(target);

        StopWatch watch = new StopWatch();
        watch.start();
        Project projectOrg = new ExcelProjectCreator(target).createProject();
        watch.stop();
        System.out.println(watch.getTime() + " ms.");
        watch.reset();
        // fail("Not yet implemented");
        File output = ProjectWriter
                .write(projectOrg, new File(input + ".json"));
        System.out.println(output.getAbsolutePath());

        watch.start();
        // Project projectJ = new JSONProjectCreator(new
        // FileInputStream(output))
        // .createProject();
        // watch.stop();
        // System.out.println(watch.getTime() + " ms.");
        //
        // // projectJ.getProjectStartDate().setTime(new Date().getTime());
        //
        // String expected = JSON.encode(projectOrg);
        // String actual = JSON.encode(projectJ);
        //
        // System.out.println("---------------");
        // System.out.println(expected);
        // System.out.println(actual);
        // System.out.println("---------------");
        // assertEquals(expected, actual);

    }

    public void testDate(String fileName) throws FileNotFoundException,
            ProjectException {
        File baseDir = new File("./");
        String input = fileName + "." + "xls";
        File target = new File(baseDir, input);

        StopWatch watch = new StopWatch();
        watch.start();
        Project projectOrg = new ExcelProjectCreator(target).createProject();
        watch.stop();
        System.out.println(watch.getTime() + " ms.");
        watch.reset();
        System.out.println(projectOrg.getBaseDate());
        System.out.println(ProjectUtils.nextTradingDate(projectOrg));
        System.out
                .println(ProjectUtils.nextTradingDate(new Date(), projectOrg));

        sevenDaysLater(projectOrg);
    }

    private void sevenDaysLater(Project project) {
        Date date = project.getBaseDate();
        for (int i = 0; i < 7; i++) {
            System.out.println(DateFormatUtils.format(date, "yyyyMMdd"));
            date = ProjectUtils.nextTradingDate(date, project);
        }
        Date base = new Date();
        Date target = base;
        System.out.println(base.after(target));
        System.out.println(base.before(target));
        System.out.println(base.before(DateUtils.addDays(target, 1)));
        System.out.println(base.after(DateUtils.addDays(target, -1)));
    }

    @Test
    public void test() throws FileNotFoundException, ProjectException {
        File baseDir = new File("./");
        String fileName = "project_management_tools";
        String input = fileName + "." + "xls";

        // java.io.InputStream in = null;
        File target = new File(baseDir, input);
        // in = new java.io.FileInputStream(target);

        StopWatch watch = new StopWatch();
        watch.start();
        Project projectOrg = new ExcelProjectCreator(target).createProject();
        watch.stop();
        System.out.println(watch.getTime() + " ms.");
        watch.reset();
        // fail("Not yet implemented");
        File output = ProjectWriter
                .write(projectOrg, new File(input + ".json"));
        System.out.println(output.getAbsolutePath());

        watch.start();
        Project projectJ = new JSONProjectCreator(new FileInputStream(output))
                .createProject();
        watch.stop();
        System.out.println(watch.getTime() + " ms.");

        // projectJ.getProjectStartDate().setTime(new Date().getTime());

        String expected = JSON.encode(projectOrg);
        String actual = JSON.encode(projectJ);

        System.out.println("---------------");
        System.out.println(expected);
        System.out.println(actual);
        System.out.println("---------------");
        assertEquals(expected, actual);

    }

    @Test
    public void test2() throws FileNotFoundException, ProjectException {
        File baseDir = new File("./");
        String fileName = "project_management_tools";
        String input = fileName + "." + "xls";

        // java.io.InputStream in = null;
        File target = new File(baseDir, input);
        // in = new java.io.FileInputStream(target);

        StopWatch watch = new StopWatch();
        watch.start();
        Project projectOrg = new ExcelProjectCreator(target).createProject();
        watch.stop();
        System.out.println(watch.getTime() + " ms.");
        watch.reset();

        List<PVBean> list = PVCreator.createCurrentList(projectOrg);
        for (PVBean pvBean : list) {
            System.out.println(pvBean);
        }

        Date startDate = projectOrg.getProjectStartDate();
        Date endDate = projectOrg.getProjectEndDate();
        Date cursor = startDate;
        while (cursor.getTime() <= endDate.getTime()) {
            // System.out.println(cursor);
            System.out.println(ProjectUtils.calculateTotalPVOfProject(
                    projectOrg, cursor));
            cursor = DateUtils.addDays(cursor, 1);

        }
    }

    @Test
    public void test3() throws FileNotFoundException, ProjectException {
        File baseDir = new File("./");
        String fileName = "project_management_tools";
        String input = fileName + "." + "xls";

        // java.io.InputStream in = null;
        File target = new File(baseDir, input);
        // in = new java.io.FileInputStream(target);

        StopWatch watch = new StopWatch();
        watch.start();
        Project project = new ExcelProjectCreator(target).createProject();
        watch.stop();
        System.out.println(watch.getTime() + " ms.");
        watch.reset();

        TaskInformation[] taskInformations = project.getTaskInformations();
        for (TaskInformation taskInfo : taskInformations) {
            // 終了予定日 <= 基準日 かつ、終了していないモノをアラート。
            System.out.println("-----" + taskInfo.getTaskId() + "------");
            boolean idEnd = (taskInfo.getEV().getEndDate() != null);
            Date baseDate = project.getBaseDate();
            Date scheduledEndDate = taskInfo.getTask().getScheduledEndDate();
            System.out.println("終了?: " + idEnd);// 採用
            System.out.println("基準日: " + baseDate); // 採用
            System.out.println("終了予定日: " + scheduledEndDate);
            if (scheduledEndDate != null) {
                boolean isBefore = scheduledEndDate.before(baseDate)
                        || scheduledEndDate.equals(baseDate);// 予定日は基準日より前か
                System.out.println(isBefore);
            }
            // System.out.println("実績終了日: " + taskInfo.getEV().getEndDate());
            // System.out.println("終了?: "
            // + (taskInfo.getEV().getProgressRate() == 1.0d));
            System.out.println("-----");
        }

    }
}
