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

        Project project = creator.createProject();
        Date baseDate = project.getBaseDate();

        String baseDateStr = new SimpleDateFormat("yyyyMMdd").format(baseDate);
        File outputFile = WriteUtils.input2Output(input, baseDateStr, suffix);

        WriteUtils.writePV(project, outputFile);
        return outputFile;

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
        return internalCreate(input, new ExcelProjectCreator(input), "_PV.tsv");
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
        return internalCreate(input, new JSONProjectCreator(input), "_PVj.tsv");
    }

    private static File internalCreateForPivot(File input,
            ProjectCreator creator, String suffix) throws ProjectException {

        Project project = creator.createProject();
        Date baseDate = project.getBaseDate();

        String baseDateStr = new SimpleDateFormat("yyyyMMdd").format(baseDate);
        File outputFile = WriteUtils.input2Output(input, baseDateStr, suffix);

        WriteUtils.writePVForPivot(project, outputFile);
        return outputFile;

    }

    /**
     * Excelファイルを引数に、{@link Project}を生成し、PVを生成してPivotに適したフォーマットで出力する。
     * アウトプットファイル名は「インプットファイル名+_PVPivot.tsv 」。
     * ディレクトリは、インプットファイルのあるファイルの場所にプロジェクトの基準日のディレクトリを掘って格納する。
     * 
     * @param input
     * @return アウトプットファイルのパス
     * @throws ProjectException
     */
    public static File createForPivot(File input) throws ProjectException {
        return internalCreateForPivot(input, new ExcelProjectCreator(input),
                "_PVPivot.tsv");
    }

    /**
     * JSONテキストファイルを引数に、{@link Project}を生成し、PVを生成してPivotに適したフォーマットで出力する。
     * アウトプットファイル名は「インプットファイル名+_PVjPivot.tsv 」。
     * ディレクトリは、インプットファイルのあるファイルの場所にプロジェクトの基準日のディレクトリを掘って格納する。
     * 
     * @param input
     * @return アウトプットファイルのパス
     * @throws ProjectException
     */
    public static File createForPivotFromJSON(File input)
            throws ProjectException {
        return internalCreateForPivot(input, new JSONProjectCreator(input),
                "_PVjPivot.tsv");
    }

}
