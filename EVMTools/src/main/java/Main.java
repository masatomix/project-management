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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;
import nu.mine.kino.entity.ACTotalBean;
import nu.mine.kino.entity.EVTotalBean;
import nu.mine.kino.entity.ExcelPOIScheduleBean;
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
import nu.mine.kino.projects.ProjectException;
import nu.mine.kino.projects.utils.ProjectUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 * @version $Revision$
 */
public class Main {
    public static void main(String[] args) throws ParseException,
            ProjectException {
        java.io.InputStream in = null;
        try {
            Workbook workbook = WorkbookFactory.create(new FileInputStream(
                    new File("project_management_tools.xls")));
            Sheet poiSheet = workbook.getSheetAt(0);
            Date baseDate = ProjectUtils.createBaseDate(workbook, poiSheet);
            Map<String, ExcelPOIScheduleBean> poiMap = ProjectUtils
                    .createExcelPOIScheduleBeanMap(workbook, baseDate);

            List<TaskInformation> taskInfoList = new ArrayList<TaskInformation>();

            in = new java.io.FileInputStream("project_management_tools.xls");

            ExcelScheduleBeanSheet sheet = new ExcelScheduleBeanSheet();
            sheet.init(workbook);
            // ExcelScheduleBeanSheet sheet = new XLSBeans().load(in,
            // ExcelScheduleBeanSheet.class);
            java.util.List<ExcelScheduleBean> instanceList = sheet
                    .getExcelScheduleBean();

            for (ExcelScheduleBean instance : instanceList) {
                if (!StringUtils.isEmpty(instance.getTaskId())) {

                    Task task = ExcelScheduleBean2Task.convert(instance);

                    double pvs = ProjectUtils.calculatePVs(task, baseDate);

                    baseDate = DateUtils.parseDate("20140925",
                            new String[] { "yyyyMMdd" });
                    Map<String, String> plotDataMap = task.getPlotDataMap();
                    // Set<String>[] split = ProjectUtils.split(baseDate,
                    // plotDataMap);
                    // Utils.print(split);

                    ExcelPOIScheduleBean bean = poiMap
                            .get(instance.getTaskId());

                    double pv = ProjectUtils.calculatePV(task, baseDate);
                    System.out.println(baseDate + "のPV:" + pv);

                    PVTotalBean pvTotalBean = ExcelPOIScheduleBean2PVTotalBean
                            .convert(bean);
                    ACTotalBean acTotalBean = ExcelPOIScheduleBean2ACTotalBean
                            .convert(bean);
                    EVTotalBean evTotalBean = ExcelPOIScheduleBean2EVTotalBean
                            .convert(bean);

                    TaskInformation taskInfo = ExcelPOIScheduleBean2TaskInformation
                            .convert(bean);

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
            // } catch (XLSBeansException e) {
            // TODO 自動生成された catch ブロック
            // e.printStackTrace();
        } catch (InvalidFormatException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (IOException e) {
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
