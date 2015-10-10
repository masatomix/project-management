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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nu.mine.kino.entity.EVBean;
import nu.mine.kino.entity.Project;
import nu.mine.kino.entity.TaskInformation;
import nu.mine.kino.projects.utils.ProjectUtils;
import nu.mine.kino.projects.utils.WriteUtils;

import org.apache.commons.lang.StringUtils;

/**
 * 2点間の差分を取ることにより、その期間のEVを取得する。
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 * @version $Revision$
 */
public class EVCreator {
    private static Object[] internalCreateList(Project targetProject,
            Project baseProject) throws ProjectException {
        List<EVBean> returnList = new ArrayList<EVBean>();
        TaskInformation[] taskInfos1 = targetProject.getTaskInformations();
        for (TaskInformation taskInfo1 : taskInfos1) {
            TaskInformation taskInfo2 = ProjectUtils.getTaskInformation(
                    baseProject, taskInfo1.getTaskId());
            EVBean bean = ProjectUtils.getEVBean(taskInfo1, taskInfo2);
            // System.out.println(bean);
            returnList.add(bean);
        }
        return new Object[] { returnList, targetProject };
    }

    private static Object[] internalCreateList(File target, File base,
            ProjectCreator creator1, ProjectCreator creator2)
            throws ProjectException {
        // java.io.InputStream in1 = null;
        // java.io.InputStream in2 = null;
        try {
            // in1 = new java.io.FileInputStream(target);
            Project project1 = creator1.createProject();
            // in2 = new java.io.FileInputStream(base);
            Project project2 = creator2.createProject();

            return internalCreateList(project1, project2);
            // } catch (FileNotFoundException e) {
            // throw new ProjectException(e);
        } finally {
            // if (in1 != null) {
            // try {
            // in1.close();
            // } catch (IOException e) {
            // throw new ProjectException(e);
            // }
            // }
            // if (in2 != null) {
            // try {
            // in2.close();
            // } catch (IOException e) {
            // throw new ProjectException(e);
            // }
            // }
        }
    }

    private static File internalCreate(File target, File base,
            ProjectCreator creator1, ProjectCreator creator2, String suffix)
            throws ProjectException {

        Object[] tmp = internalCreateList(target, base, creator1, creator2);
        List<EVBean> returnList = (List<EVBean>) tmp[0];
        Project project1 = (Project) tmp[1];

        Date baseDate = project1.getBaseDate();
        String baseDateStr = new SimpleDateFormat("yyyyMMdd").format(baseDate);

        File outputFile = WriteUtils.input2Output(target, baseDateStr, suffix);

        WriteUtils.writeEV(project1, returnList, outputFile);
        return outputFile;
    }

    /**
     * 2つのExcelファイルを引数に、2つの{@link Project}を生成し、それらのEVの差分を計算して ファイル出力する。
     * アウトプットファイル名は「インプットファイル名+_EV.tsv 」。
     * ディレクトリは、インプットファイルのあるファイルの場所にプロジェクトの基準日のディレクトリを掘って格納する。
     * 
     * 
     * @param target
     * @param base
     * @return
     * @throws ProjectException
     */
    public static File create(File target, File base) throws ProjectException {
        return create(target, base, null);
    }

    public static File create(File target, File base, String base_prefix)
            throws ProjectException {
        String suffix = "_EV.tsv";
        if (!StringUtils.isEmpty(base_prefix)) {
            suffix = "_" + base_prefix + "EV.tsv";
        }
        return internalCreate(target, base, new ExcelProjectCreator(target),
                new ExcelProjectCreator(base), suffix);
    }

    /**
     * 2つのJSONテキストファイルを引数に、2つの{@link Project}を生成し、それらのACの差分を計算して ファイル出力する。
     * アウトプットファイル名は「インプットファイル名+_ACj.tsv 」。
     * ディレクトリは、インプットファイルのあるファイルの場所にプロジェクトの基準日のディレクトリを掘って格納する。
     * 
     * @param target
     * @param base
     * @return
     * @throws ProjectException
     */
    public static File createFromJSON(File target, File base)
            throws ProjectException {
        return createFromJSON(target, base, null);
    }

    public static File createFromJSON(File target, File base, String base_prefix)
            throws ProjectException {
        String suffix = "_EVj.tsv";
        if (!StringUtils.isEmpty(base_prefix)) {
            suffix = "_" + base_prefix + "EVj.tsv";
        }
        return internalCreate(target, base, new JSONProjectCreator(target),
                new JSONProjectCreator(base), suffix);

    }

    public static List<EVBean> createEVList(File target, File base)
            throws ProjectException {
        Object[] tmp = internalCreateList(target, base,
                new ExcelProjectCreator(target), new ExcelProjectCreator(base));
        return (List<EVBean>) tmp[0];
    }

    /**
     * 2つのJSONテキストファイルを引数に、2つの{@link Project}を生成し、それらのEVの差分を計算して ファイル出力する。
     * アウトプットファイル名は「インプットファイル名+_EVj.tsv 」。
     * ディレクトリは、インプットファイルのあるファイルの場所にプロジェクトの基準日のディレクトリを掘って格納する。
     * 
     * 
     * @param target
     * @param base
     * @return
     * @throws ProjectException
     */
    public static List<EVBean> createEVListFromJSON(File target, File base)
            throws ProjectException {
        Object[] tmp = internalCreateList(target, base, new JSONProjectCreator(
                target), new JSONProjectCreator(base));
        return (List<EVBean>) tmp[0];
    }

}
