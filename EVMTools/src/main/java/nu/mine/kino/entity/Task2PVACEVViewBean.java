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

public class Task2PVACEVViewBean {

    /**
     * 引数のオブジェクトのプロパティからデータをコピーして戻り値のオブジェクトを生成して返すメソッド。
     * 
     * @param source
     * @return
     */
    public static PVACEVViewBean convert(Task source) {
        PVACEVViewBean dest = new PVACEVViewBean();

        // 必要に応じて特殊な載せ替え処理 開始
        ((PVACEVViewBean) dest).setId(source.getId());
        ((PVACEVViewBean) dest).setTaskId(source.getTaskId());
        ((PVACEVViewBean) dest).setType(source.getType());
        ((PVACEVViewBean) dest).setTaskSharp(source.getTaskSharp());
        ((PVACEVViewBean) dest).setTaskName(source.getTaskName());
        ((PVACEVViewBean) dest).setPersonInCharge(source.getPersonInCharge());
        ((PVACEVViewBean) dest).setTaskAbstract(source.getTaskAbstract());
        ((PVACEVViewBean) dest).setNumberOfManDays(source.getNumberOfManDays());
        ((PVACEVViewBean) dest).setScheduledStartDate(source
                .getScheduledStartDate());
        ((PVACEVViewBean) dest).setScheduledEndDate(source
                .getScheduledEndDate());
        ((PVACEVViewBean) dest).setNumberOfDays(source.getNumberOfDays());

        // 特殊な載せ替え処理 終了

        return dest;
    }

    /**
     * 第一引数から第二引数へプロパティをコピーするメソッド。
     * 
     * @param source
     * @param dest
     */
    public static void convert(Task source, PVACEVViewBean dest) {
        // 必要に応じて特殊な載せ替え処理 開始
        ((PVACEVViewBean) dest).setId(source.getId());
        ((PVACEVViewBean) dest).setTaskId(source.getTaskId());
        ((PVACEVViewBean) dest).setType(source.getType());
        ((PVACEVViewBean) dest).setTaskSharp(source.getTaskSharp());
        ((PVACEVViewBean) dest).setTaskName(source.getTaskName());
        ((PVACEVViewBean) dest).setPersonInCharge(source.getPersonInCharge());
        ((PVACEVViewBean) dest).setTaskAbstract(source.getTaskAbstract());
        ((PVACEVViewBean) dest).setNumberOfManDays(source.getNumberOfManDays());
        ((PVACEVViewBean) dest).setScheduledStartDate(source
                .getScheduledStartDate());
        ((PVACEVViewBean) dest).setScheduledEndDate(source
                .getScheduledEndDate());
        ((PVACEVViewBean) dest).setNumberOfDays(source.getNumberOfDays());

        // 特殊な載せ替え処理 終了

    }

}
