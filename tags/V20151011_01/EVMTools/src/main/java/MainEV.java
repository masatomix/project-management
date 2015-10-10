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

import nu.mine.kino.entity.Project;
import nu.mine.kino.entity.TaskInformation;
import nu.mine.kino.projects.ExcelProjectCreator;
import nu.mine.kino.projects.ProjectException;
import nu.mine.kino.projects.utils.ProjectUtils;

/**
 * ある時点のEV累計(かな？)を出力する
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 * @version $Revision$
 */
public class MainEV {
    public static void main(String[] args) throws ProjectException {
        File baseDir = new File("C:/Temp");
        String fileName = "project_management_tools";

        String input = fileName + "." + "xls";
        // String output = fileName + "." + "tsv";

        // java.io.InputStream in = null;
        try {
            // in = new java.io.FileInputStream(new File(baseDir, input));
            Project project = new ExcelProjectCreator(new File(baseDir, input))
                    .createProject();
            TaskInformation[] taskInfos = project.getTaskInformations();
            for (TaskInformation taskInfo : taskInfos) {
                ProjectUtils.calculateEVs(taskInfo, null);
            }

            // } catch (FileNotFoundException e) {
            // throw new ProjectException(e);
        } finally {
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
