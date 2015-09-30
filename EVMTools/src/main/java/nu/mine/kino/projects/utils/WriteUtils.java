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
//作成日: 2014/10/30

package nu.mine.kino.projects.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;

import nu.mine.kino.entity.ACBean;
import nu.mine.kino.entity.EVBean;
import nu.mine.kino.entity.Project;
import nu.mine.kino.entity.Task;
import nu.mine.kino.entity.TaskInformation;
import nu.mine.kino.projects.ProjectException;

import org.apache.commons.lang.time.DateUtils;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class WriteUtils {
    private static final String delimiter = "\t";

    /**
     * インプットのファイルについて./[subDir]/ファイル名[suffix]をつけたファイルへの参照を返す。
     * 
     * @param input
     * @param subDir
     * @param suffix
     * @return
     */
    public static File input2Output(File input, String subDir, String suffix) {
        File baseDir = input.getParentFile();
        String outputStr = input.getName() + suffix;

        File outputDir = new File(baseDir, subDir);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        File outputFile = new File(outputDir, outputStr);
        return outputFile;
    }

    private static String getPVHeader(Project project) {
        StringBuffer buf = new StringBuffer();
        buf.append("#" + delimiter);
        buf.append("タスクID" + delimiter);
        buf.append("タスク名" + delimiter);
        buf.append("担当者" + delimiter);
        Date projectStartDate = project.getProjectStartDate();
        Date projectEndDate = project.getProjectEndDate();

        // System.out.printf("プロジェクト開始日:%s\n", projectStartDate);
        // System.out.printf("プロジェクト終了日:%s\n", projectEndDate);

        Date targetDate = projectStartDate;
        while (!targetDate.equals(projectEndDate)) {
            // System.out.printf("target日:%s\n", targetDate);
            String data = String.format("%s" + delimiter,
                    Utils.date2Str(targetDate, "yyyy/MM/dd"));
            buf.append(data);
            targetDate = DateUtils.addDays(targetDate, 1);
        }
        return new String(buf);
    }

    private static String getPVHeaderForPivot(Project project) {
        StringBuffer buf = new StringBuffer();
        buf.append("#" + delimiter);
        buf.append("タスクID" + delimiter);
        buf.append("タスク名" + delimiter);
        buf.append("担当者" + delimiter);
        buf.append("日付" + delimiter);
        buf.append("PV");
        return new String(buf);
    }

    public static String getPvForPrint(Project project, TaskInformation taskInfo) {

        StringBuffer buf = new StringBuffer();
        Task task = taskInfo.getTask();
        buf.append(task.getTaskSharp() + delimiter);
        buf.append(task.getTaskId() + delimiter);
        buf.append(task.getTaskName() + delimiter);
        buf.append(task.getPersonInCharge() + delimiter);

        Date projectStartDate = project.getProjectStartDate();
        Date projectEndDate = project.getProjectEndDate();

        Date targetDate = projectStartDate;
        while (!targetDate.equals(projectEndDate)) {
            double pv = ProjectUtils.calculatePV(task, targetDate);
            if (!Double.isNaN(pv)) {
                buf.append(pv);
            }
            buf.append(delimiter);
            targetDate = DateUtils.addDays(targetDate, 1);
        }
        return new String(buf);
    }

    public static String getPvForPivot(Project project, TaskInformation taskInfo) {

        StringBuffer buf = new StringBuffer();
        Date projectStartDate = project.getProjectStartDate();
        Date projectEndDate = project.getProjectEndDate();
        Task task = taskInfo.getTask();

        Date targetDate = projectStartDate;
        while (!targetDate.equals(projectEndDate)) {
            double pv = ProjectUtils.calculatePV(task, targetDate);
            if (Utils.isNonZeroNumeric(pv)) {
                buf.append(task.getTaskSharp() + delimiter);
                buf.append(task.getTaskId() + delimiter);
                buf.append(task.getTaskName() + delimiter);
                buf.append(task.getPersonInCharge() + delimiter);
                String data = String.format("%s" + delimiter,
                        Utils.date2Str(targetDate, "yyyy/MM/dd"));
                buf.append(data);
                buf.append(pv);
                if (ProjectUtils.isHoliday(project, targetDate)) {
                    buf.append(delimiter + "休日");
                }
                buf.append("\n");
            }
            targetDate = DateUtils.addDays(targetDate, 1);
        }
        return new String(buf);
    }

    public static void writePV(Project project, File file)
            throws ProjectException {

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "MS932"));

            String header = getPVHeader(project);
            // System.out.println(header);
            writer.write(header, 0, header.length());
            writer.newLine();

            TaskInformation[] informations = project.getTaskInformations();
            for (TaskInformation taskInfo : informations) {
                String pvForPrint = getPvForPrint(project, taskInfo);
                // System.out.println(pvForPrint);

                writer.write(pvForPrint, 0, pvForPrint.length());
                writer.newLine();
            }
        } catch (FileNotFoundException e) {
            throw new ProjectException(e);
        } catch (IOException e) {
            throw new ProjectException(e);
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (Exception e) {
                throw new ProjectException(e);
            }
        }

    }

    public static void writeAC(Project project, List<ACBean> returnList,
            File file) throws ProjectException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "MS932"));
            writer.write(getACHeader(project));
            writer.newLine();

            for (ACBean acBean : returnList) {
                TaskInformation taskInfo = ProjectUtils.getTaskInformation(
                        project, acBean.getTaskId());
                StringBuffer buf = new StringBuffer();
                buf.append(taskInfo.getTask().getTaskSharp());
                buf.append("\t");
                buf.append(acBean.getTaskId());
                buf.append("\t");
                buf.append(taskInfo.getTask().getTaskName());
                buf.append("\t");
                buf.append(taskInfo.getTask().getPersonInCharge());
                buf.append("\t");
                if (!Double.isNaN(acBean.getActualCost())) {
                    buf.append(acBean.getActualCost());
                }
                String message = new String(buf);
                // System.out.println(message);
                writer.write(message, 0, message.length());
                writer.newLine();
            }
        } catch (FileNotFoundException e) {
            throw new ProjectException(e);
        } catch (IOException e) {
            throw new ProjectException(e);
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (Exception e) {
                throw new ProjectException(e);
            }
        }

    }

    private static String getACHeader(Project project) {
        StringBuffer buf = new StringBuffer();
        buf.append("#" + delimiter);
        buf.append("タスクID" + delimiter);
        buf.append("タスク名" + delimiter);
        buf.append("担当者" + delimiter);
        buf.append("AC" + delimiter);
        buf.append(Utils.date2Str(project.getBaseDate(), "yyyy/MM/dd"));
        return new String(buf);
    }

    public static void writeEV(Project project, List<EVBean> returnList,
            File file) throws ProjectException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "MS932"));
            writer.write(getEVHeader(project));
            writer.newLine();

            for (EVBean evBean : returnList) {
                TaskInformation taskInfo = ProjectUtils.getTaskInformation(
                        project, evBean.getTaskId());
                StringBuffer buf = new StringBuffer();
                buf.append(taskInfo.getTask().getTaskSharp());
                buf.append("\t");
                buf.append(evBean.getTaskId());
                buf.append("\t");
                buf.append(taskInfo.getTask().getTaskName());
                buf.append("\t");
                buf.append(taskInfo.getTask().getPersonInCharge());
                buf.append("\t");
                if (!Double.isNaN(evBean.getEarnedValue())) {
                    buf.append(evBean.getEarnedValue());
                }
                buf.append("\t");
                if (!Double.isNaN(evBean.getProgressRate())) {
                    buf.append(evBean.getProgressRate());
                }
                String message = new String(buf);
                // System.out.println(message);
                writer.write(message, 0, message.length());
                writer.newLine();
            }
        } catch (FileNotFoundException e) {
            throw new ProjectException(e);
        } catch (IOException e) {
            throw new ProjectException(e);
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (Exception e) {
                throw new ProjectException(e);
            }
        }

    }

    private static String getEVHeader(Project project) {
        StringBuffer buf = new StringBuffer();
        buf.append("#" + delimiter);
        buf.append("タスクID" + delimiter);
        buf.append("タスク名" + delimiter);
        buf.append("担当者" + delimiter);
        buf.append("EV" + delimiter);
        buf.append("進捗率" + delimiter);
        buf.append(Utils.date2Str(project.getBaseDate(), "yyyy/MM/dd"));
        return new String(buf);
    }

    public static void writeFile(byte[] bytes, File file) {
        BufferedOutputStream stream = null;
        try {
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }
    }

    public static void writePVForPivot(Project project, File file)
            throws ProjectException {

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "MS932"));

            String header = getPVHeaderForPivot(project);
            // System.out.println(header);
            writer.write(header, 0, header.length());
            writer.newLine();

            TaskInformation[] informations = project.getTaskInformations();
            for (TaskInformation taskInfo : informations) {
                String pvForPivot = getPvForPivot(project, taskInfo);
                // System.out.println(pvForPrint);

                writer.write(pvForPivot, 0, pvForPivot.length());
                // writer.newLine();
            }
        } catch (FileNotFoundException e) {
            throw new ProjectException(e);
        } catch (IOException e) {
            throw new ProjectException(e);
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (Exception e) {
                throw new ProjectException(e);
            }
        }

    }
}
