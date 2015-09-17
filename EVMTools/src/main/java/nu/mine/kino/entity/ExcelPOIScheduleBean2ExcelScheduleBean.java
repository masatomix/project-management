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
        ((ExcelScheduleBean) dest).setTaskId(source.getTaskId());
        ((ExcelScheduleBean) dest).setId(source.getId());
        ((ExcelScheduleBean) dest).setType(source.getType());
        ((ExcelScheduleBean) dest).setTaskSharp(source.getTaskSharp());
        ((ExcelScheduleBean) dest).setTaskName(source.getTaskName());
        ((ExcelScheduleBean) dest).setTaskName1(source.getTaskName1());
        ((ExcelScheduleBean) dest).setTaskName2(source.getTaskName2());
        ((ExcelScheduleBean) dest).setTaskName3(source.getTaskName3());
        ((ExcelScheduleBean) dest).setSenkoTaskId(source.getSenkoTaskId());
        ((ExcelScheduleBean) dest)
                .setPersonInCharge(source.getPersonInCharge());
        ((ExcelScheduleBean) dest).setTaskAbstract(source.getTaskAbstract());
        ((ExcelScheduleBean) dest).setOutput(source.getOutput());
        ((ExcelScheduleBean) dest).setReportOfLastWeek(source
                .getReportOfLastWeek());
        ((ExcelScheduleBean) dest).setReportOfThisWeek(source
                .getReportOfThisWeek());
        ((ExcelScheduleBean) dest).setStatus(source.getStatus());
        ((ExcelScheduleBean) dest).setRemarks(source.getRemarks());
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
        ((ExcelScheduleBean) dest).setTaskId(source.getTaskId());
        ((ExcelScheduleBean) dest).setId(source.getId());
        ((ExcelScheduleBean) dest).setType(source.getType());
        ((ExcelScheduleBean) dest).setTaskSharp(source.getTaskSharp());
        ((ExcelScheduleBean) dest).setTaskName(source.getTaskName());
        ((ExcelScheduleBean) dest).setTaskName1(source.getTaskName1());
        ((ExcelScheduleBean) dest).setTaskName2(source.getTaskName2());
        ((ExcelScheduleBean) dest).setTaskName3(source.getTaskName3());
        ((ExcelScheduleBean) dest).setSenkoTaskId(source.getSenkoTaskId());
        ((ExcelScheduleBean) dest)
                .setPersonInCharge(source.getPersonInCharge());
        ((ExcelScheduleBean) dest).setTaskAbstract(source.getTaskAbstract());
        ((ExcelScheduleBean) dest).setOutput(source.getOutput());
        ((ExcelScheduleBean) dest).setReportOfLastWeek(source
                .getReportOfLastWeek());
        ((ExcelScheduleBean) dest).setReportOfThisWeek(source
                .getReportOfThisWeek());
        ((ExcelScheduleBean) dest).setStatus(source.getStatus());
        ((ExcelScheduleBean) dest).setRemarks(source.getRemarks());
        ((ExcelScheduleBean) dest).setBaseDate(source.getBaseDate());

        // 特殊な載せ替え処理 終了

    }

}
