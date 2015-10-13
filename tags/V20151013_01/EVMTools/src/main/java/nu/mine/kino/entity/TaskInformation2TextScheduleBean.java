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

public class TaskInformation2TextScheduleBean {

    /**
     * 引数のオブジェクトのプロパティからデータをコピーして戻り値のオブジェクトを生成して返すメソッド。
     * 
     * @param source
     * @return
     */
    public static TextScheduleBean convert(TaskInformation source) {
        TextScheduleBean dest = new TextScheduleBean();

        // 必要に応じて特殊な載せ替え処理 開始
        ((TextScheduleBean) dest).setTaskId(source.getTaskId());
        ((TextScheduleBean) dest).setTaskName(source.getTask().getTaskName());
        ((TextScheduleBean) dest).setPersonInCharge(source.getTask()
                .getPersonInCharge());
        ((TextScheduleBean) dest).setNumberOfManDays(source.getTask()
                .getNumberOfManDays());
        ((TextScheduleBean) dest).setScheduledStartDate(source.getTask()
                .getScheduledStartDate());
        ((TextScheduleBean) dest).setScheduledEndDate(source.getTask()
                .getScheduledEndDate());
        ((TextScheduleBean) dest).setProgressRate(source.getEV()
                .getProgressRate());
        ((TextScheduleBean) dest).setNumberOfDays(source.getTask()
                .getNumberOfDays());
        ((TextScheduleBean) dest).setPlannedValue(source.getPV()
                .getPlannedValue());
        ((TextScheduleBean) dest).setEarnedValue(source.getEV()
                .getEarnedValue());
        ((TextScheduleBean) dest).setActualCost(source.getAC().getActualCost());
        ((TextScheduleBean) dest).setBaseDate(source.getBaseDate());

        // 特殊な載せ替え処理 終了

        return dest;
    }

    /**
     * 第一引数から第二引数へプロパティをコピーするメソッド。
     * 
     * @param source
     * @param dest
     */
    public static void convert(TaskInformation source, TextScheduleBean dest) {
        // 必要に応じて特殊な載せ替え処理 開始
        ((TextScheduleBean) dest).setTaskId(source.getTaskId());
        ((TextScheduleBean) dest).setTaskName(source.getTask().getTaskName());
        ((TextScheduleBean) dest).setPersonInCharge(source.getTask()
                .getPersonInCharge());
        ((TextScheduleBean) dest).setNumberOfManDays(source.getTask()
                .getNumberOfManDays());
        ((TextScheduleBean) dest).setScheduledStartDate(source.getTask()
                .getScheduledStartDate());
        ((TextScheduleBean) dest).setScheduledEndDate(source.getTask()
                .getScheduledEndDate());
        ((TextScheduleBean) dest).setProgressRate(source.getEV()
                .getProgressRate());
        ((TextScheduleBean) dest).setNumberOfDays(source.getTask()
                .getNumberOfDays());
        ((TextScheduleBean) dest).setPlannedValue(source.getPV()
                .getPlannedValue());
        ((TextScheduleBean) dest).setEarnedValue(source.getEV()
                .getEarnedValue());
        ((TextScheduleBean) dest).setActualCost(source.getAC().getActualCost());
        ((TextScheduleBean) dest).setBaseDate(source.getBaseDate());

        // 特殊な載せ替え処理 終了

    }

}
