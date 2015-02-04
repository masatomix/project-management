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

import org.apache.poi.ss.usermodel.Row;

public class Row2ExcelPOIScheduleBean {

    /**
     * 引数のオブジェクトのプロパティからデータをコピーして戻り値のオブジェクトを生成して返すメソッド。
     * 
     * @param source
     * @return
     */
    public static ExcelPOIScheduleBean convert(Row source) {
        ExcelPOIScheduleBean dest = new ExcelPOIScheduleBean();

        // 必要に応じて特殊な載せ替え処理 開始
        ((ExcelPOIScheduleBean) dest)
                .setTaskId(nu.mine.kino.projects.utils.PoiUtils
                        .getTaskId(source.getCell(1)));
        ((ExcelPOIScheduleBean) dest)
                .setNumberOfManDays(nu.mine.kino.projects.utils.Utils.round((Double) nu.mine.kino.projects.utils.PoiUtils
                        .getCellValue(source.getCell(15), Double.class)));
        ((ExcelPOIScheduleBean) dest)
                .setProgressRate(nu.mine.kino.projects.utils.Utils.round((Double) nu.mine.kino.projects.utils.PoiUtils
                        .getCellValue(source.getCell(20), Double.class)));
        ((ExcelPOIScheduleBean) dest)
                .setNumberOfDays((Integer) nu.mine.kino.projects.utils.PoiUtils
                        .getCellValue(source.getCell(21), Integer.class));
        ((ExcelPOIScheduleBean) dest)
                .setPlannedValue(nu.mine.kino.projects.utils.Utils.round((Double) nu.mine.kino.projects.utils.PoiUtils
                        .getCellValue(source.getCell(22), Double.class)));
        ((ExcelPOIScheduleBean) dest)
                .setEarnedValue(nu.mine.kino.projects.utils.Utils.round((Double) nu.mine.kino.projects.utils.PoiUtils
                        .getCellValue(source.getCell(23), Double.class)));
        ((ExcelPOIScheduleBean) dest)
                .setActualCost(nu.mine.kino.projects.utils.Utils.round((Double) nu.mine.kino.projects.utils.PoiUtils
                        .getCellValue(source.getCell(24), Double.class)));
        ((ExcelPOIScheduleBean) dest)
                .setScheduledStartDate(nu.mine.kino.projects.utils.PoiUtils
                        .getDate(source.getCell(16)));
        ((ExcelPOIScheduleBean) dest)
                .setScheduledEndDate(nu.mine.kino.projects.utils.PoiUtils
                        .getDate(source.getCell(17)));
        ((ExcelPOIScheduleBean) dest)
                .setStartDate(nu.mine.kino.projects.utils.PoiUtils
                        .getDate(source.getCell(18)));
        ((ExcelPOIScheduleBean) dest)
                .setEndDate(nu.mine.kino.projects.utils.PoiUtils.getDate(source
                        .getCell(19)));

        // 特殊な載せ替え処理 終了

        return dest;
    }

    /**
     * 第一引数から第二引数へプロパティをコピーするメソッド。
     * 
     * @param source
     * @param dest
     */
    public static void convert(Row source, ExcelPOIScheduleBean dest) {
        // 必要に応じて特殊な載せ替え処理 開始
        ((ExcelPOIScheduleBean) dest)
                .setTaskId(nu.mine.kino.projects.utils.PoiUtils
                        .getTaskId(source.getCell(1)));
        ((ExcelPOIScheduleBean) dest)
                .setNumberOfManDays(nu.mine.kino.projects.utils.Utils.round((Double) nu.mine.kino.projects.utils.PoiUtils
                        .getCellValue(source.getCell(15), Double.class)));
        ((ExcelPOIScheduleBean) dest)
                .setProgressRate(nu.mine.kino.projects.utils.Utils.round((Double) nu.mine.kino.projects.utils.PoiUtils
                        .getCellValue(source.getCell(20), Double.class)));
        ((ExcelPOIScheduleBean) dest)
                .setNumberOfDays((Integer) nu.mine.kino.projects.utils.PoiUtils
                        .getCellValue(source.getCell(21), Integer.class));
        ((ExcelPOIScheduleBean) dest)
                .setPlannedValue(nu.mine.kino.projects.utils.Utils.round((Double) nu.mine.kino.projects.utils.PoiUtils
                        .getCellValue(source.getCell(22), Double.class)));
        ((ExcelPOIScheduleBean) dest)
                .setEarnedValue(nu.mine.kino.projects.utils.Utils.round((Double) nu.mine.kino.projects.utils.PoiUtils
                        .getCellValue(source.getCell(23), Double.class)));
        ((ExcelPOIScheduleBean) dest)
                .setActualCost(nu.mine.kino.projects.utils.Utils.round((Double) nu.mine.kino.projects.utils.PoiUtils
                        .getCellValue(source.getCell(24), Double.class)));
        ((ExcelPOIScheduleBean) dest)
                .setScheduledStartDate(nu.mine.kino.projects.utils.PoiUtils
                        .getDate(source.getCell(16)));
        ((ExcelPOIScheduleBean) dest)
                .setScheduledEndDate(nu.mine.kino.projects.utils.PoiUtils
                        .getDate(source.getCell(17)));
        ((ExcelPOIScheduleBean) dest)
                .setStartDate(nu.mine.kino.projects.utils.PoiUtils
                        .getDate(source.getCell(18)));
        ((ExcelPOIScheduleBean) dest)
                .setEndDate(nu.mine.kino.projects.utils.PoiUtils.getDate(source
                        .getCell(19)));

        // 特殊な載せ替え処理 終了

    }

}
