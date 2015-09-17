/******************************************************************************
 * Copyright (c) 2008-2009 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;
import net.java.amateras.xlsbeans.XLSBeans;
import net.java.amateras.xlsbeans.XLSBeansException;
import nu.mine.kino.entity.ACTotalBean;
import nu.mine.kino.entity.EVTotalBean;
import nu.mine.kino.entity.ExcelPOIScheduleBean2ACTotalBean;
import nu.mine.kino.entity.ExcelPOIScheduleBean2EVTotalBean;
import nu.mine.kino.entity.ExcelPOIScheduleBean2PVTotalBean;
import nu.mine.kino.entity.ExcelPOIScheduleBean2TaskInformation;
import nu.mine.kino.entity.ExcelScheduleBean;
import nu.mine.kino.entity.ExcelScheduleBean2Task;
import nu.mine.kino.entity.PVTotalBean;
import nu.mine.kino.entity.Project;
import nu.mine.kino.entity.Task;
import nu.mine.kino.entity.TaskInformation;
import nu.mine.kino.projects.ExcelScheduleBeanSheet;
import nu.mine.kino.projects.utils.ProjectUtils;

import org.apache.commons.lang.time.DateUtils;

/**
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 * @version $Revision$
 */
public class Main {
    public static void main(String[] args) throws ParseException {
        java.io.InputStream in = null;
        try {
            List<TaskInformation> taskInfoList = new ArrayList<TaskInformation>();

            in = new java.io.FileInputStream("project_management_tools.xls");

            ExcelScheduleBeanSheet sheet = new XLSBeans().load(in,
                    ExcelScheduleBeanSheet.class);
            java.util.List<ExcelScheduleBean> instanceList = sheet
                    .getExcelScheduleBean();

            for (ExcelScheduleBean instance : instanceList) {
                if (!instance.getId().equals("")) {
                    instance.setBaseDate(sheet.getBaseDate());
                    Task task = ExcelScheduleBean2Task.convert(instance);

                    double pvs = ProjectUtils.calculatePVs(task,
                            instance.getBaseDate());

                    Date baseDate = DateUtils.parseDate("20140925",
                            new String[] { "yyyyMMdd" });
                    Map<String, String> plotDataMap = task.getPlotDataMap();
                    // Set<String>[] split = ProjectUtils.split(baseDate,
                    // plotDataMap);
                    // Utils.print(split);

                    double pv = ProjectUtils.calculatePV(task, baseDate);
                    System.out.println(baseDate + "のPV:" + pv);

                    PVTotalBean pvTotalBean = ExcelPOIScheduleBean2PVTotalBean
                            .convert(instance);
                    ACTotalBean acTotalBean = ExcelPOIScheduleBean2ACTotalBean
                            .convert(instance);
                    EVTotalBean evTotalBean = ExcelPOIScheduleBean2EVTotalBean
                            .convert(instance);

                    TaskInformation taskInfo = ExcelPOIScheduleBean2TaskInformation
                            .convert(instance);

                    taskInfo.setTask(task);
                    taskInfo.setPV(pvTotalBean);
                    taskInfo.setAC(acTotalBean);
                    taskInfo.setEV(evTotalBean);
                    taskInfoList.add(taskInfo);

                    // System.out.println(instance);
                    // System.out.println(task);
                    // System.out.println(pvTotalBean);
                    // System.out.println(acTotalBean);
                    // System.out.println(evTotalBean);
                }
            }
            System.out.println(instanceList.size());
            System.out.println(sheet.getBaseDate());

            Project project = new Project();
            project.setTaskInformations(taskInfoList
                    .toArray(new TaskInformation[taskInfoList.size()]));
            JSON json = new JSON();
            json.setPrettyPrint(true);
            String jsonStr = json.format(project);
            System.out.println(jsonStr);

        } catch (FileNotFoundException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (XLSBeansException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                }
            }
        }

    }
}
