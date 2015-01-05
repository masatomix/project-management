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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.arnx.jsonic.JSON;
import nu.mine.kino.entity.Project;
import nu.mine.kino.projects.utils.WriteUtils;

/**
 * プロジェクト情報をJSON形式で出力する。
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 * @version $Revision$
 */
public class ProjectWriter {
    public static File write(File input) throws ProjectException {
        File baseDir = input.getParentFile();
        String output = input.getName() + "." + "json";

        java.io.InputStream in = null;
        try {
            in = new java.io.FileInputStream(input);
            Project project = new ExcelProjectCreator(in).createProject();

            // System.out.println(project);
            // JSON json = new JSON();
            // json.setPrettyPrint(true);
            // String jsonStr = json.format(project);
            //
            File outputFile = new File(baseDir, output);
            // ProjectUtils.writeFile(jsonStr.getBytes("UTF-8"), outputFile);

            write(project, outputFile);
            return outputFile;

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
