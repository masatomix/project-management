/******************************************************************************
 * Copyright (c) 2014 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//作成日: 2014/11/03

package nu.mine.kino.projects.utils;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import nu.mine.kino.entity.ExcelScheduleBean;

/**
 * プロジェクト情報を計算するためのBaseとなるデータを取るためのUtils
 * 
 * @author Masatomi KINO
 * @version $Revision$
 */
public class BaseDataUtils {
    // //////////////// 稼働予定日数 //////////////////////////////
    /**
     * Excel上のプロットデータのMapから、稼働予定日数を計算。 プロットされている数をカウントしている。
     * 
     * @param plotDataMap
     *            Excel上のプロットデータのマップ
     * @return 稼働予定日数
     */
    private static int plotCount(Map<String, String> plotDataMap) {
        String[] strArray = plotDataMap.values().toArray(
                new String[plotDataMap.size()]);
        int plotNumber = 0;
        for (String target : strArray) {
            if (!Utils.isEmpty(target)) {
                plotNumber++;
            }
        }
        return plotNumber;
    }

    /**
     * Excel上の行オブジェクトから稼働予定日数を計算。プロットされている数をカウントしている。
     * 
     * @param instance
     *            Excel上の行オブジェクト
     * @return 稼働予定日数
     */
    public static int calculateNumberOfDays(ExcelScheduleBean instance) {
        return plotCount(instance.getPlotDataMap());
    }

    // //////////////// 稼働予定日数 //////////////////////////////

    // //////////////// プロジェクトの開始日・終了日 //////////////////////////////

    /**
     * Excel上のプロットデータのMap から最小の日付を検索。ようするに開始日。
     * 
     * @param plotDataMap
     *            Excel上のプロットデータのMap
     * @return 最小の日付
     */
    private static Date minDate(Map<String, String> plotDataMap) {
        boolean exist = false;
        Set<String> keySet = plotDataMap.keySet();
        long ans = Long.MAX_VALUE;
        for (String serial : keySet) {
            if (!Utils.isEmpty(plotDataMap.get(serial))) {
                Date targetDate = Utils.excelSerialValue2Date(serial);
                ans = Math.min(ans, targetDate.getTime());
                exist = true;
            }
        }
        return exist ? new Date(ans) : null;
    }

    /**
     * Excel上のプロットデータのMap から最大の日付を検索。ようするに終了日。
     * 
     * @param plotDataMap
     *            Excel上のプロットデータのMap
     * @return 最大の日付
     */
    private static Date maxDate(Map<String, String> plotDataMap) {
        boolean exist = false;
        Set<String> keySet = plotDataMap.keySet();
        long ans = Long.MIN_VALUE;
        for (String serial : keySet) {
            if (!Utils.isEmpty(plotDataMap.get(serial))) {
                Date targetDate = Utils.excelSerialValue2Date(serial);
                ans = Math.max(ans, targetDate.getTime());
                exist = true;
            }
        }
        return exist ? new Date(ans) : null;

    }

    /**
     * Excel上の行オブジェクトから 最小の日付を検索。ようするに開始日。
     * 
     * @param instance
     *            Excel上の行オブジェクト
     * @return 開始日付
     */
    public static Date calculateMinDate(ExcelScheduleBean instance) {
        return minDate(instance.getPlotDataMap());
    }

    /**
     * Excel上の行オブジェクトから 最大の日付を検索。ようするに終了日。
     * 
     * @param instance
     *            Excel上の行オブジェクト
     * @return 終了日付
     */
    public static Date calculateMaxDate(ExcelScheduleBean instance) {
        return maxDate(instance.getPlotDataMap());
    }

    // //////////////// プロジェクトの開始日・終了日 //////////////////////////////

    /**
     * Excel上のプロットデータのMap を指定した日付で分割する。 指定した日付とおなじ日付は、左に入れる。
     * 
     * @param baseDate
     *            指定した日付
     * @param plotDataMap
     * @return 分割視された日付セットの配列。一つ目が左。二つ目が右。データはExcelのシリアル値。
     */
    static Set<String>[] split(Date baseDate, Map<String, String> plotDataMap) {

        Set<String> leftSet = new TreeSet<String>();
        Set<String> rightSet = new TreeSet<String>();

        Date startDate = minDate(plotDataMap);
        Date endDate = maxDate(plotDataMap);
        Set<String> keySet = plotDataMap.keySet();
        for (String serial : keySet) {
            if (!Utils.isEmpty(plotDataMap.get(serial))) {
                Date targetDate = Utils.excelSerialValue2Date(serial);
                boolean betweenLeft = Utils.isBetween(targetDate, startDate,
                        baseDate, true, true);
                boolean betweenRight = Utils.isBetween(targetDate, baseDate,
                        endDate, false, true);
                if (betweenLeft) {
                    leftSet.add(serial);
                }

                if (betweenRight) {
                    rightSet.add(serial);
                }
            }
        }
        return new Set[] { leftSet, rightSet };
    }

    /**
     * Excel上の行オブジェクト(のリスト)から、このExcelファイルのプロジェクト開始日付、終了日付を取得する。
     * 実際は、一つ目の行オブジェクトの日付を調べている。
     * 
     * @param instanceList
     * @return
     */
    public static Date[] getProjectRange(
            java.util.List<ExcelScheduleBean> instanceList) {
        Date[] range = getProjectRange(instanceList.get(0));
        return range;
    }

    private static Date[] getProjectRange(ExcelScheduleBean instance) {
        return getProjectRange(instance.getPlotDataMap());
    }

    static Date[] getProjectRange(Map<String, String> plotDataMap) {
        Set<String> keySet = plotDataMap.keySet();
        long maxAns = Long.MIN_VALUE;
        long minAns = Long.MAX_VALUE;
        for (String serial : keySet) {
            Date targetDate = Utils.excelSerialValue2Date(serial);
            maxAns = Math.max(maxAns, targetDate.getTime());
            minAns = Math.min(minAns, targetDate.getTime());
        }
        return new Date[] { new Date(minAns), new Date(maxAns) };
    }

    // //////////////// プロジェクトの開始日・終了日 //////////////////////////////

    /**
     * 引数のbaseDateの日付について、 引数のplogDataMapのうち、プロットのマスが存在かどうかをチェックする。
     * 
     * @param baseDate
     * @param plotDataMap
     * @return
     */
    static boolean existsPlot(Date baseDate, Map<String, String> plotDataMap) {
        Set<String> keySet = plotDataMap.keySet();
        for (String serial : keySet) {
            // valueがnull値でない日付について、
            if (!Utils.isEmpty(plotDataMap.get(serial))) {
                Date targetDate = Utils.excelSerialValue2Date(serial);
                if (targetDate.equals(baseDate)) {
                    return true;
                }
            }
        }
        return false;
    }
    // private static void printDate(Set<String> keySet) {
    // for (String serial : keySet) {
    // System.out.println(serial);
    // System.out.println(excelSerialValue2Date(serial));
    // double d = (Double.valueOf(serial) - 25569 - 0.375) * 86400000;
    // }
    // }

}
