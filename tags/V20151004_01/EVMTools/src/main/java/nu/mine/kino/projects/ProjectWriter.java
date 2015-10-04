package nu.mine.kino.projects;

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
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.arnx.jsonic.JSON;
import nu.mine.kino.entity.OutputScheduleBean;
import nu.mine.kino.entity.Project;
import nu.mine.kino.entity.TaskInformation;
import nu.mine.kino.entity.TaskInformation2TextScheduleBean;
import nu.mine.kino.entity.TextScheduleBean;
import nu.mine.kino.projects.utils.ProjectUtils;
import nu.mine.kino.projects.utils.WriteUtils;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.BeanToCsv;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;

/**
 * プロジェクト情報をJSON形式で出力する。
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 * @version $Revision$
 */
public class ProjectWriter {
    public static void writeText(Project project, File outputFile)
            throws ProjectException {
        List<TextScheduleBean> list = new ArrayList<TextScheduleBean>();
        TaskInformation[] taskInformations = project.getTaskInformations();
        for (TaskInformation taskInfo : taskInformations) {
            OutputScheduleBean bean = new OutputScheduleBean();
            TaskInformation2TextScheduleBean.convert(taskInfo, bean);
            list.add(bean);
        }

        ColumnPositionMappingStrategy<OutputScheduleBean> strat = new ColumnPositionMappingStrategy<OutputScheduleBean>();
        strat.setType(OutputScheduleBean.class);
        strat.setColumnMapping(new String[] { "taskId", "taskName",
                "personInCharge", "numberOfManDays", "scheduledStartDateStr",
                "scheduledEndDateStr", "progressRate", "numberOfDays",
                "plannedValue", "earnedValue", "actualCost" });
        BeanToCsv csv = new BeanToCsv();
        // StringWriter writer = new StringWriter();
        FileWriter writer = null;
        try {
            writer = new FileWriter(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        csv.writeAll(strat, new CSVWriter(writer, '\t',
                CSVWriter.NO_QUOTE_CHARACTER), list);

        // SVNLogBean bean = new SVNLogBean();
        // bean.setType(type);
        // list.add(bean);
        // BeanToCsv csv = new BeanToCsv();
        // String[] records = csv.write(strat, bean);
    }

    public static File write(File input) throws ProjectException {
        File outputFile = ProjectUtils.findJSONFilePath(input);
        // File baseDir = input.getParentFile();
        // String output = input.getName() + "." + "json";

        // java.io.InputStream in = null;
        try {
            // in = new java.io.FileInputStream(input);
            Project project = new ExcelProjectCreator(input).createProject();
            // File outputFile = new File(baseDir, output);
            write(project, outputFile);
            return outputFile;

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

    public static File write(Project project, File outputFile)
            throws ProjectException {
        try {
            JSON json = new JSON();
            json.setPrettyPrint(true);
            String jsonStr = json.format(project);
            WriteUtils.writeFile(jsonStr.getBytes("UTF-8"), outputFile);
            return outputFile;
        } catch (UnsupportedEncodingException e) {
            throw new ProjectException(e);
        }
    }

}
