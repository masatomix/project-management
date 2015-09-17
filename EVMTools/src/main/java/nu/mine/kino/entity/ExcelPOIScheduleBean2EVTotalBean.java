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

public class ExcelPOIScheduleBean2EVTotalBean {

    /**
     * 引数のオブジェクトのプロパティからデータをコピーして戻り値のオブジェクトを生成して返すメソッド。
     * 
     * @param source
     * @return
     */
    public static EVTotalBean convert(ExcelPOIScheduleBean source) {
        EVTotalBean dest = new EVTotalBean();

        // 必要に応じて特殊な載せ替え処理 開始
        ((EVTotalBean) dest).setId(source.getId());
        ((EVTotalBean) dest).setTaskId(source.getTaskId());
        ((EVTotalBean) dest).setBaseDate(source.getBaseDate());
        ((EVTotalBean) dest).setStartDate(startDate(source, dest));
        ((EVTotalBean) dest).setEndDate(endDate(source, dest));
        ((EVTotalBean) dest)
                .setEarnedValue(source.getEarnedValue() == null ? Double.NaN
                        : source.getEarnedValue());
        ((EVTotalBean) dest)
                .setProgressRate(source.getProgressRate() == null ? Double.NaN
                        : source.getProgressRate());

        // 特殊な載せ替え処理 終了

        return dest;
    }

    /**
     * 第一引数から第二引数へプロパティをコピーするメソッド。
     * 
     * @param source
     * @param dest
     */
    public static void convert(ExcelPOIScheduleBean source, EVTotalBean dest) {
        // 必要に応じて特殊な載せ替え処理 開始
        ((EVTotalBean) dest).setId(source.getId());
        ((EVTotalBean) dest).setTaskId(source.getTaskId());
        ((EVTotalBean) dest).setBaseDate(source.getBaseDate());
        ((EVTotalBean) dest).setStartDate(startDate(source, dest));
        ((EVTotalBean) dest).setEndDate(endDate(source, dest));
        ((EVTotalBean) dest)
                .setEarnedValue(source.getEarnedValue() == null ? Double.NaN
                        : source.getEarnedValue());
        ((EVTotalBean) dest)
                .setProgressRate(source.getProgressRate() == null ? Double.NaN
                        : source.getProgressRate());

        // 特殊な載せ替え処理 終了

    }

    private static java.util.Date startDate(ExcelPOIScheduleBean source,
            EVTotalBean dest) {
        return source.getStartDate() != null ? source.getStartDate() : dest
                .getStartDate();
    }

    private static java.util.Date endDate(ExcelPOIScheduleBean source,
            EVTotalBean dest) {
        return source.getEndDate() != null ? source.getEndDate() : dest
                .getEndDate();
    }

}
