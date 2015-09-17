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

public class ExcelPOIScheduleBean2Task {

    /**
     * 引数のオブジェクトのプロパティからデータをコピーして戻り値のオブジェクトを生成して返すメソッド。
     * 
     * @param source
     * @return
     */
    public static Task convert(ExcelPOIScheduleBean source) {
        Task dest = new Task();

        // 必要に応じて特殊な載せ替え処理 開始
        ((Task) dest).setId(null2Empty(source.getId()));
        ((Task) dest).setTaskId(null2Empty(source.getTaskId()));
        ((Task) dest).setType(null2Empty(source.getType()));
        ((Task) dest).setTaskSharp(null2Empty(source.getTaskSharp()));
        ((Task) dest).setTaskName(null2Empty(source.getTaskName()));
        ((Task) dest).setTaskName1(null2Empty(source.getTaskName1()));
        ((Task) dest).setTaskName2(null2Empty(source.getTaskName2()));
        ((Task) dest).setTaskName3(null2Empty(source.getTaskName3()));
        ((Task) dest).setSenkoTaskId(null2Empty(source.getSenkoTaskId()));
        ((Task) dest).setPersonInCharge(null2Empty(source.getPersonInCharge()));
        ((Task) dest).setTaskAbstract(null2Empty(source.getTaskAbstract()));
        ((Task) dest).setScheduledEndDate(scheduledEndDate(source, dest));
        ((Task) dest).setScheduledStartDate(scheduledStartDate(source, dest));
        ((Task) dest).setNumberOfDays(source.getNumberOfDays() == null ? 0
                : source.getNumberOfDays());
        ((Task) dest)
                .setNumberOfManDays(source.getNumberOfManDays() == null ? Double.NaN
                        : source.getNumberOfManDays());

        // 特殊な載せ替え処理 終了

        return dest;
    }

    /**
     * 第一引数から第二引数へプロパティをコピーするメソッド。
     * 
     * @param source
     * @param dest
     */
    public static void convert(ExcelPOIScheduleBean source, Task dest) {
        // 必要に応じて特殊な載せ替え処理 開始
        ((Task) dest).setId(null2Empty(source.getId()));
        ((Task) dest).setTaskId(null2Empty(source.getTaskId()));
        ((Task) dest).setType(null2Empty(source.getType()));
        ((Task) dest).setTaskSharp(null2Empty(source.getTaskSharp()));
        ((Task) dest).setTaskName(null2Empty(source.getTaskName()));
        ((Task) dest).setTaskName1(null2Empty(source.getTaskName1()));
        ((Task) dest).setTaskName2(null2Empty(source.getTaskName2()));
        ((Task) dest).setTaskName3(null2Empty(source.getTaskName3()));
        ((Task) dest).setSenkoTaskId(null2Empty(source.getSenkoTaskId()));
        ((Task) dest).setPersonInCharge(null2Empty(source.getPersonInCharge()));
        ((Task) dest).setTaskAbstract(null2Empty(source.getTaskAbstract()));
        ((Task) dest).setScheduledEndDate(scheduledEndDate(source, dest));
        ((Task) dest).setScheduledStartDate(scheduledStartDate(source, dest));
        ((Task) dest).setNumberOfDays(source.getNumberOfDays() == null ? 0
                : source.getNumberOfDays());
        ((Task) dest)
                .setNumberOfManDays(source.getNumberOfManDays() == null ? Double.NaN
                        : source.getNumberOfManDays());

        // 特殊な載せ替え処理 終了

    }

    private static java.util.Date scheduledEndDate(ExcelPOIScheduleBean source,
            Task dest) {
        if (source.getScheduledEndDate() != null) {
            return source.getScheduledEndDate();
        }
        return dest.getScheduledEndDate();
    }

    private static java.util.Date scheduledStartDate(
            ExcelPOIScheduleBean source, Task dest) {
        if (source.getScheduledStartDate() != null) {
            return source.getScheduledStartDate();
        }
        return dest.getScheduledStartDate();
    }

    private static String null2Empty(String target) {
        return target == null ? "" : target;
    }

}
