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
import org.bbreak.excella.core.util.PoiUtil;

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
                .setNumberOfManDays((Double) nu.mine.kino.projects.utils.PoiUtils
                        .getCellValue(source.getCell(15), Double.class));
        ((ExcelPOIScheduleBean) dest)
                .setProgressRate((Double) nu.mine.kino.projects.utils.PoiUtils
                        .getCellValue(source.getCell(20), Double.class));
        ((ExcelPOIScheduleBean) dest)
                .setNumberOfDays((Integer) nu.mine.kino.projects.utils.PoiUtils
                        .getCellValue(source.getCell(21), Integer.class));
        ((ExcelPOIScheduleBean) dest)
                .setPlannedValue((Double) nu.mine.kino.projects.utils.PoiUtils
                        .getCellValue(source.getCell(22), Double.class));
        ((ExcelPOIScheduleBean) dest)
                .setEarnedValue((Double) nu.mine.kino.projects.utils.PoiUtils
                        .getCellValue(source.getCell(23), Double.class));
        ((ExcelPOIScheduleBean) dest)
                .setActualCost((Double) nu.mine.kino.projects.utils.PoiUtils
                        .getCellValue(source.getCell(24), Double.class));
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
        ((ExcelPOIScheduleBean) dest).setId((String) PoiUtil.getCellValue(
                source.getCell(0), String.class));
        ((ExcelPOIScheduleBean) dest).setType((String) PoiUtil.getCellValue(
                source.getCell(2), String.class));
        ((ExcelPOIScheduleBean) dest).setTaskSharp((String) PoiUtil
                .getCellValue(source.getCell(3), String.class));
        ((ExcelPOIScheduleBean) dest).setTaskName((String) PoiUtil
                .getCellValue(source.getCell(4), String.class));
        ((ExcelPOIScheduleBean) dest).setTaskName1((String) PoiUtil
                .getCellValue(source.getCell(5), String.class));
        ((ExcelPOIScheduleBean) dest).setTaskName2((String) PoiUtil
                .getCellValue(source.getCell(6), String.class));
        ((ExcelPOIScheduleBean) dest).setTaskName3((String) PoiUtil
                .getCellValue(source.getCell(7), String.class));
        ((ExcelPOIScheduleBean) dest).setSenkoTaskId((String) PoiUtil
                .getCellValue(source.getCell(8), String.class));
        ((ExcelPOIScheduleBean) dest).setPersonInCharge((String) PoiUtil
                .getCellValue(source.getCell(9), String.class));
        ((ExcelPOIScheduleBean) dest).setTaskAbstract((String) PoiUtil
                .getCellValue(source.getCell(10), String.class));
        ((ExcelPOIScheduleBean) dest).setOutput((String) PoiUtil.getCellValue(
                source.getCell(11), String.class));
        ((ExcelPOIScheduleBean) dest).setReportOfLastWeek((String) PoiUtil
                .getCellValue(source.getCell(12), String.class));
        ((ExcelPOIScheduleBean) dest).setReportOfThisWeek((String) PoiUtil
                .getCellValue(source.getCell(13), String.class));
        ((ExcelPOIScheduleBean) dest).setStatus((String) PoiUtil.getCellValue(
                source.getCell(14), String.class));
        ((ExcelPOIScheduleBean) dest).setRemarks((String) PoiUtil.getCellValue(
                source.getCell(25), String.class));

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
                .setNumberOfManDays((Double) nu.mine.kino.projects.utils.PoiUtils
                        .getCellValue(source.getCell(15), Double.class));
        ((ExcelPOIScheduleBean) dest)
                .setProgressRate((Double) nu.mine.kino.projects.utils.PoiUtils
                        .getCellValue(source.getCell(20), Double.class));
        ((ExcelPOIScheduleBean) dest)
                .setNumberOfDays((Integer) nu.mine.kino.projects.utils.PoiUtils
                        .getCellValue(source.getCell(21), Integer.class));
        ((ExcelPOIScheduleBean) dest)
                .setPlannedValue((Double) nu.mine.kino.projects.utils.PoiUtils
                        .getCellValue(source.getCell(22), Double.class));
        ((ExcelPOIScheduleBean) dest)
                .setEarnedValue((Double) nu.mine.kino.projects.utils.PoiUtils
                        .getCellValue(source.getCell(23), Double.class));
        ((ExcelPOIScheduleBean) dest)
                .setActualCost((Double) nu.mine.kino.projects.utils.PoiUtils
                        .getCellValue(source.getCell(24), Double.class));
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
        ((ExcelPOIScheduleBean) dest).setId((String) PoiUtil.getCellValue(
                source.getCell(0), String.class));
        ((ExcelPOIScheduleBean) dest).setType((String) PoiUtil.getCellValue(
                source.getCell(2), String.class));
        ((ExcelPOIScheduleBean) dest).setTaskSharp((String) PoiUtil
                .getCellValue(source.getCell(3), String.class));
        ((ExcelPOIScheduleBean) dest).setTaskName((String) PoiUtil
                .getCellValue(source.getCell(4), String.class));
        ((ExcelPOIScheduleBean) dest).setTaskName1((String) PoiUtil
                .getCellValue(source.getCell(5), String.class));
        ((ExcelPOIScheduleBean) dest).setTaskName2((String) PoiUtil
                .getCellValue(source.getCell(6), String.class));
        ((ExcelPOIScheduleBean) dest).setTaskName3((String) PoiUtil
                .getCellValue(source.getCell(7), String.class));
        ((ExcelPOIScheduleBean) dest).setSenkoTaskId((String) PoiUtil
                .getCellValue(source.getCell(8), String.class));
        ((ExcelPOIScheduleBean) dest).setPersonInCharge((String) PoiUtil
                .getCellValue(source.getCell(9), String.class));
        ((ExcelPOIScheduleBean) dest).setTaskAbstract((String) PoiUtil
                .getCellValue(source.getCell(10), String.class));
        ((ExcelPOIScheduleBean) dest).setOutput((String) PoiUtil.getCellValue(
                source.getCell(11), String.class));
        ((ExcelPOIScheduleBean) dest).setReportOfLastWeek((String) PoiUtil
                .getCellValue(source.getCell(12), String.class));
        ((ExcelPOIScheduleBean) dest).setReportOfThisWeek((String) PoiUtil
                .getCellValue(source.getCell(13), String.class));
        ((ExcelPOIScheduleBean) dest).setStatus((String) PoiUtil.getCellValue(
                source.getCell(14), String.class));
        ((ExcelPOIScheduleBean) dest).setRemarks((String) PoiUtil.getCellValue(
                source.getCell(25), String.class));

        // 特殊な載せ替え処理 終了

    }

}
