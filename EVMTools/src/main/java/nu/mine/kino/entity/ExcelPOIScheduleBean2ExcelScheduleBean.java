/******************************************************************************
 * Copyright (c) 2008-2014 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 ******************************************************************************/

package nu.mine.kino.entity;

public class ExcelPOIScheduleBean2ExcelScheduleBean {

    /**
     * 引数のオブジェクトのプロパティからデータをコピーして戻り値のオブジェクトを生成して返すメソッド。
     * 
     * @param source
     * @return
     */
    public static ExcelScheduleBean convert(ExcelPOIScheduleBean source) {
        ExcelScheduleBean dest = new ExcelScheduleBean();

        // 必要に応じて特殊な載せ替え処理 開始
        ((ExcelScheduleBean) dest).setTaskId(null2Empty(source.getTaskId()));
        ((ExcelScheduleBean) dest).setId(null2Empty(source.getId()));
        ((ExcelScheduleBean) dest).setType(null2Empty(source.getType()));
        ((ExcelScheduleBean) dest).setTaskSharp(null2Empty(source
                .getTaskSharp()));
        ((ExcelScheduleBean) dest)
                .setTaskName(null2Empty(source.getTaskName()));
        ((ExcelScheduleBean) dest).setTaskName1(null2Empty(source
                .getTaskName1()));
        ((ExcelScheduleBean) dest).setTaskName2(null2Empty(source
                .getTaskName2()));
        ((ExcelScheduleBean) dest).setTaskName3(null2Empty(source
                .getTaskName3()));
        ((ExcelScheduleBean) dest).setSenkoTaskId(null2Empty(source
                .getSenkoTaskId()));
        ((ExcelScheduleBean) dest).setPersonInCharge(null2Empty(source
                .getPersonInCharge()));
        ((ExcelScheduleBean) dest).setTaskAbstract(null2Empty(source
                .getTaskAbstract()));
        ((ExcelScheduleBean) dest).setOutput(null2Empty(source.getOutput()));
        ((ExcelScheduleBean) dest).setReportOfLastWeek(null2Empty(source
                .getReportOfLastWeek()));
        ((ExcelScheduleBean) dest).setReportOfThisWeek(null2Empty(source
                .getReportOfThisWeek()));
        ((ExcelScheduleBean) dest).setStatus(null2Empty(source.getStatus()));
        ((ExcelScheduleBean) dest).setRemarks(null2Empty(source.getRemarks()));
        ((ExcelScheduleBean) dest).setBaseDate(source.getBaseDate());

        // 特殊な載せ替え処理 終了

        return dest;
    }

    /**
     * 第一引数から第二引数へプロパティをコピーするメソッド。
     * 
     * @param source
     * @param dest
     */
    public static void convert(ExcelPOIScheduleBean source,
            ExcelScheduleBean dest) {
        // 必要に応じて特殊な載せ替え処理 開始
        ((ExcelScheduleBean) dest).setTaskId(null2Empty(source.getTaskId()));
        ((ExcelScheduleBean) dest).setId(null2Empty(source.getId()));
        ((ExcelScheduleBean) dest).setType(null2Empty(source.getType()));
        ((ExcelScheduleBean) dest).setTaskSharp(null2Empty(source
                .getTaskSharp()));
        ((ExcelScheduleBean) dest)
                .setTaskName(null2Empty(source.getTaskName()));
        ((ExcelScheduleBean) dest).setTaskName1(null2Empty(source
                .getTaskName1()));
        ((ExcelScheduleBean) dest).setTaskName2(null2Empty(source
                .getTaskName2()));
        ((ExcelScheduleBean) dest).setTaskName3(null2Empty(source
                .getTaskName3()));
        ((ExcelScheduleBean) dest).setSenkoTaskId(null2Empty(source
                .getSenkoTaskId()));
        ((ExcelScheduleBean) dest).setPersonInCharge(null2Empty(source
                .getPersonInCharge()));
        ((ExcelScheduleBean) dest).setTaskAbstract(null2Empty(source
                .getTaskAbstract()));
        ((ExcelScheduleBean) dest).setOutput(null2Empty(source.getOutput()));
        ((ExcelScheduleBean) dest).setReportOfLastWeek(null2Empty(source
                .getReportOfLastWeek()));
        ((ExcelScheduleBean) dest).setReportOfThisWeek(null2Empty(source
                .getReportOfThisWeek()));
        ((ExcelScheduleBean) dest).setStatus(null2Empty(source.getStatus()));
        ((ExcelScheduleBean) dest).setRemarks(null2Empty(source.getRemarks()));
        ((ExcelScheduleBean) dest).setBaseDate(source.getBaseDate());

        // 特殊な載せ替え処理 終了

    }

    private static String null2Empty(String target) {
        return target == null ? "" : target;
    }

}
