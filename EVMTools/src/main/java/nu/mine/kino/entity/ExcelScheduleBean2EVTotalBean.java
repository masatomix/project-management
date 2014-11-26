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

public class ExcelScheduleBean2EVTotalBean {

    /**
     * 引数のオブジェクトのプロパティからデータをコピーして戻り値のオブジェクトを生成して返すメソッド。
     * 
     * @param source
     * @return
     */
    public static EVTotalBean convert(ExcelScheduleBean source) {
        EVTotalBean dest = new EVTotalBean();

        // 必要に応じて特殊な載せ替え処理 開始
        ((EVTotalBean) dest).setId(source.getId());
        ((EVTotalBean) dest).setTaskId(source.getTaskId());
        ((EVTotalBean) dest).setStartDate(nu.mine.kino.projects.utils.Utils
                .str2Date(source.getStartDate(), "MM/dd"));
        ((EVTotalBean) dest).setEndDate(nu.mine.kino.projects.utils.Utils
                .str2Date(source.getEndDate(), "MM/dd"));
        ((EVTotalBean) dest).setProgressRate(nu.mine.kino.projects.utils.Utils
                .convetPercentStr2Double(source.getProgressRate()));
        ((EVTotalBean) dest).setEarnedValue(nu.mine.kino.projects.utils.Utils
                .convert2Double(source.getEarnedValue()));
        ((EVTotalBean) dest).setBaseDate(source.getBaseDate());

        // 特殊な載せ替え処理 終了

        return dest;
    }

    /**
     * 第一引数から第二引数へプロパティをコピーするメソッド。
     * 
     * @param source
     * @param dest
     */
    public static void convert(ExcelScheduleBean source, EVTotalBean dest) {
        // 必要に応じて特殊な載せ替え処理 開始
        ((EVTotalBean) dest).setId(source.getId());
        ((EVTotalBean) dest).setTaskId(source.getTaskId());
        ((EVTotalBean) dest).setStartDate(nu.mine.kino.projects.utils.Utils
                .str2Date(source.getStartDate(), "MM/dd"));
        ((EVTotalBean) dest).setEndDate(nu.mine.kino.projects.utils.Utils
                .str2Date(source.getEndDate(), "MM/dd"));
        ((EVTotalBean) dest).setProgressRate(nu.mine.kino.projects.utils.Utils
                .convetPercentStr2Double(source.getProgressRate()));
        ((EVTotalBean) dest).setEarnedValue(nu.mine.kino.projects.utils.Utils
                .convert2Double(source.getEarnedValue()));
        ((EVTotalBean) dest).setBaseDate(source.getBaseDate());

        // 特殊な載せ替え処理 終了

    }

}
