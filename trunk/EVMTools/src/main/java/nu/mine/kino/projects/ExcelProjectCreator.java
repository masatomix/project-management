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
//作成日: 2014/11/04

package nu.mine.kino.projects;

import static nu.mine.kino.projects.utils.PoiUtils.getDate;
import static nu.mine.kino.projects.utils.PoiUtils.getTaskId;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.java.amateras.xlsbeans.XLSBeans;
import net.java.amateras.xlsbeans.XLSBeansException;
import nu.mine.kino.entity.ACTotalBean;
import nu.mine.kino.entity.EVTotalBean;
import nu.mine.kino.entity.ExcelScheduleBean;
import nu.mine.kino.entity.ExcelScheduleBean2ACTotalBean;
import nu.mine.kino.entity.ExcelScheduleBean2EVTotalBean;
import nu.mine.kino.entity.ExcelScheduleBean2PVTotalBean;
import nu.mine.kino.entity.ExcelScheduleBean2Task;
import nu.mine.kino.entity.ExcelScheduleBean2TaskInformation;
import nu.mine.kino.entity.ExcelScheduleBeanSheet2Project;
import nu.mine.kino.entity.PVTotalBean;
import nu.mine.kino.entity.Project;
import nu.mine.kino.entity.Task;
import nu.mine.kino.entity.TaskInformation;
import nu.mine.kino.projects.utils.PoiUtils;
import nu.mine.kino.projects.utils.Utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class ExcelProjectCreator extends InputStreamProjectCreator {

    // public ExcelProjectCreator(InputStream in) {
    // super(in);
    // }

    private final File file;

    public ExcelProjectCreator(File file) throws ProjectException {
        super(file);
        this.file = file;
    }

    /**
     * Excel フォーマットのFile(InputStream)からProjectを作成する
     * 
     * @see nu.mine.kino.projects.ProjectCreator#createProject(java.io.InputStream)
     */
    @Override
    public Project createProjectFromStream() throws ProjectException {

        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            Workbook workbook = null;
            workbook = WorkbookFactory.create(in);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> e = sheet.rowIterator();
            while (e.hasNext()) {
                Row row = e.next();
                Cell taskIdCell = row.getCell(1);
                String taskId = getTaskId(taskIdCell);
                System.out.printf("[%s],[%s],[%s],[%s],[%s],", taskId,
                        row.getCell(15), row.getCell(22), row.getCell(23),
                        row.getCell(24));

                Cell scheduledSDateCell = row.getCell(16);
                Date sDate = getDate(scheduledSDateCell);
                Cell scheduledEDateCell = row.getCell(17);
                Date eDate = getDate(scheduledEDateCell);

                String pattern = "yyyy/MM/dd";
                System.out.printf("[%s],[%s],[%s]\n", taskId,
                        Utils.date2Str(sDate, pattern),
                        Utils.date2Str(eDate, pattern));

            }
        } catch (InvalidFormatException e) {
            throw new ProjectException(e);
        } catch (FileNotFoundException e) {
            throw new ProjectException(e);
        } catch (IOException e) {
            throw new ProjectException(e);
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                }
        }

        try {
            List<TaskInformation> taskInfoList = new ArrayList<TaskInformation>();
            ExcelScheduleBeanSheet sheet = new XLSBeans().load(
                    getInputStream(), ExcelScheduleBeanSheet.class);
            java.util.List<ExcelScheduleBean> instanceList = sheet
                    .getExcelScheduleBean();

            for (ExcelScheduleBean instance : instanceList) {
                if (!instance.getId().equals("")) {
                    instance.setBaseDate(sheet.getBaseDate());
                    Task task = ExcelScheduleBean2Task.convert(instance);
                    PVTotalBean pvTotalBean = ExcelScheduleBean2PVTotalBean
                            .convert(instance);
                    ACTotalBean acTotalBean = ExcelScheduleBean2ACTotalBean
                            .convert(instance);
                    EVTotalBean evTotalBean = ExcelScheduleBean2EVTotalBean
                            .convert(instance);
                    TaskInformation taskInfo = ExcelScheduleBean2TaskInformation
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

            Project project = new Project();
            project.setTaskInformations(taskInfoList
                    .toArray(new TaskInformation[taskInfoList.size()]));

            ExcelScheduleBeanSheet2Project.convert(sheet, project);

            // System.out.println("---");
            // System.out.println(project.getProjectStartDate());
            // System.out.println(project.getProjectEndDate());
            // System.out.println("---");
            return project;
        } catch (XLSBeansException e) {
            throw new ProjectException(e);
            // } finally {
            // if (in != null) {
            // try {
            // in.close();
            // } catch (IOException e) {
            // throw new ProjectException(e);
            // }
            // }
        }
    }
}
