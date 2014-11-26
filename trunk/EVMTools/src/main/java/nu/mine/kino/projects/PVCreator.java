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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nu.mine.kino.entity.PVBean;
import nu.mine.kino.entity.Project;
import nu.mine.kino.projects.utils.ProjectUtils;
import nu.mine.kino.projects.utils.WriteUtils;

/**
 * プロジェクト全体のPVを日ごとに出力する。
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 * @version $Revision$
 */
public class PVCreator {
    public static List<PVBean> createCurrentList(Project project)
            throws ProjectException {
        return ProjectUtils.getCurrentPVList(project);
    }

    private static File internalCreate(File input, ProjectCreator creator,
            String suffix) throws ProjectException {
        File baseDir = input.getParentFile();
        String outputStr = input.getName() + suffix;

        java.io.InputStream in = null;
        try {
            in = new java.io.FileInputStream(input);
            Project project = creator.createProject(in);

            Date baseDate = project.getBaseDate();
            String baseDateStr = new SimpleDateFormat("yyyyMMdd")
                    .format(baseDate);

            File outputDir = new File(baseDir, baseDateStr);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            File outputFile = new File(outputDir, outputStr);
            WriteUtils.writePV(project, outputFile);
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

    /**
     * Excelファイルを引数に、{@link Project}を生成し、PVを生成して出力する。
     * アウトプットファイル名は「インプットファイル名+_PV.tsv 」。
     * ディレクトリは、インプットファイルのあるファイルの場所にプロジェクトの基準日のディレクトリを掘って格納する。
     * 
     * @param input
     * @return アウトプットファイルのパス
     * @throws ProjectException
     */
    public static File create(File input) throws ProjectException {
        return internalCreate(input, new DefaultProjectCreator(), "_PV.tsv");
    }

    /**
     * JSONテキストファイルを引数に、{@link Project}を生成し、PVを生成して出力する。
     * アウトプットファイル名は「インプットファイル名+_PVj.tsv 」。
     * ディレクトリは、インプットファイルのあるファイルの場所にプロジェクトの基準日のディレクトリを掘って格納する。
     * 
     * @param input
     * @return アウトプットファイルのパス
     * @throws ProjectException
     */
    public static File createFromJSON(File input) throws ProjectException {
        return internalCreate(input, new JSONProjectCreator(), "_PVj.tsv");
    }

}
