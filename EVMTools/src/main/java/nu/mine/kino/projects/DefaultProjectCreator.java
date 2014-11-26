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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class DefaultProjectCreator implements ProjectCreator {

    /**
     * Excel フォーマットのFile(InputStream)からProjectを作成する
     * 
     * @see nu.mine.kino.projects.ProjectCreator#createProject(java.io.InputStream)
     */
    @Override
    public Project createProject(InputStream in) throws ProjectException {
        try {
            List<TaskInformation> taskInfoList = new ArrayList<TaskInformation>();
            ExcelScheduleBeanSheet sheet = new XLSBeans().load(in,
                    ExcelScheduleBeanSheet.class);
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

    /**
     * Excel フォーマットのFile(InputStream)からProjectを作成する
     * 
     * @see nu.mine.kino.projects.ProjectCreator#createProject(java.io.File)
     */
    @Override
    public Project createProject(File input) throws ProjectException {
        java.io.InputStream in = null;
        try {
            in = new java.io.FileInputStream(input);
            Project project = this.createProject(in);
            return project;
        } catch (FileNotFoundException e) {
            throw new ProjectException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new ProjectException(e);
                }
            }
        }
    }
}
