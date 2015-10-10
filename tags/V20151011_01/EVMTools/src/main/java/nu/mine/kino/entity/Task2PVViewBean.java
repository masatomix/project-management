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

public class Task2PVViewBean {

    /**
     * 引数のオブジェクトのプロパティからデータをコピーして戻り値のオブジェクトを生成して返すメソッド。
     * 
     * @param source
     * @return
     */
    public static PVViewBean convert(Task source) {
        PVViewBean dest = new PVViewBean();

        // 必要に応じて特殊な載せ替え処理 開始
        ((PVViewBean) dest).setId(source.getId());
        ((PVViewBean) dest).setTaskId(source.getTaskId());
        ((PVViewBean) dest).setType(source.getType());
        ((PVViewBean) dest).setTaskSharp(source.getTaskSharp());
        ((PVViewBean) dest).setTaskName(source.getTaskName());
        ((PVViewBean) dest).setPersonInCharge(source.getPersonInCharge());
        ((PVViewBean) dest).setTaskAbstract(source.getTaskAbstract());
        ((PVViewBean) dest).setNumberOfManDays(source.getNumberOfManDays());
        ((PVViewBean) dest).setScheduledStartDate(source
                .getScheduledStartDate());
        ((PVViewBean) dest).setScheduledEndDate(source.getScheduledEndDate());
        ((PVViewBean) dest).setNumberOfDays(source.getNumberOfDays());

        // 特殊な載せ替え処理 終了

        return dest;
    }

    /**
     * 第一引数から第二引数へプロパティをコピーするメソッド。
     * 
     * @param source
     * @param dest
     */
    public static void convert(Task source, PVViewBean dest) {
        // 必要に応じて特殊な載せ替え処理 開始
        ((PVViewBean) dest).setId(source.getId());
        ((PVViewBean) dest).setTaskId(source.getTaskId());
        ((PVViewBean) dest).setType(source.getType());
        ((PVViewBean) dest).setTaskSharp(source.getTaskSharp());
        ((PVViewBean) dest).setTaskName(source.getTaskName());
        ((PVViewBean) dest).setPersonInCharge(source.getPersonInCharge());
        ((PVViewBean) dest).setTaskAbstract(source.getTaskAbstract());
        ((PVViewBean) dest).setNumberOfManDays(source.getNumberOfManDays());
        ((PVViewBean) dest).setScheduledStartDate(source
                .getScheduledStartDate());
        ((PVViewBean) dest).setScheduledEndDate(source.getScheduledEndDate());
        ((PVViewBean) dest).setNumberOfDays(source.getNumberOfDays());

        // 特殊な載せ替え処理 終了

    }

}
