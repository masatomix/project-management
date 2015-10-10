/******************************************************************************
 * Copyright (c) 2012 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//作成日: 2014/09/25

package nu.mine.kino.projects.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class Utils {

    public static double convetPercentStr2Double(String progressRate) {
        String target = progressRate;
        if (progressRate.endsWith("%")) {
            target = progressRate.substring(0, progressRate.length() - 1);
        }
        double convert2Double = convert2Double(target);
        return convert2Double / 100.0d;
    }

    public static double convert2Double(String str) {
        return isEmpty(str) ? Double.NaN : Double.valueOf(str);
    }

    static boolean isEmpty(String target) {
        return target == null || "".equals(target);
    }

    /**
     * // * 日付を作る。MM/dd形式の場合、仕方がないので年には今年をセットする。
     * 
     * @param dateStr
     * @return
     */
    public static Date str2Date(String dateStr, String... parsePatterns) {
        if (isEmpty(dateStr)) {
            return null;
        }

        // // 今年を取得
        // Calendar calendar = Calendar.getInstance();
        // calendar.setTime(new Date());
        // int year = calendar.get(Calendar.YEAR);
        //
        // try {
        // // String[] parsePatterns = new String[] { "MM/dd" };
        // Date dataDate = null;
        // DateUtils.parseDate(dateStr, parsePatterns);
        // if (parsePatterns == null || parsePatterns.length == 0) {
        // dataDate = DateUtils.parseDate(dateStr,
        // new String[] { "yyyy/MM/dd" });
        // }
        // dataDate = DateUtils.parseDate(dateStr, parsePatterns);
        // Calendar retCalendar = Calendar.getInstance();
        // retCalendar.setTime(dataDate);
        // retCalendar.set(Calendar.YEAR, year);
        // // System.out.println(retCalendar.getTime());
        // return retCalendar.getTime();
        // } catch (ParseException e) {
        // e.printStackTrace();
        // return null;
        // }
        String[] tmpParsePatterns = parsePatterns;
        if (parsePatterns.length == 0) {
            tmpParsePatterns = new String[] { "yyyyMMdd", "yyyy/MM/dd" };
        }

        try {
            return DateUtils.parseDate(dateStr, tmpParsePatterns);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Date excelSerialValue2Date(String serial) {
        BigDecimal dec = new BigDecimal(serial).subtract(
                new BigDecimal("25569")).subtract(new BigDecimal("0.375"));
        BigDecimal ans = dec.multiply(new BigDecimal("86400000"));
        return new Date(ans.longValue());
    }

    // private static String date2excelSerialValue(Date date) {
    // long time = date.getTime();
    // BigDecimal dec = new BigDecimal(time)
    // .divide(new BigDecimal("86400000"));
    // BigDecimal ans = dec.add(new BigDecimal("25569")).add(
    // new BigDecimal("0.375"));
    // return ans.toString();
    // }

    /**
     * baseDateがstartDate/endDateに含まれているかどうか。booleanの引数は、同日もOKと見なすか。いずれかが
     * nullの場合false
     * 
     * @param baseDate
     * @param startDate
     * @param endDate
     * @return
     */
    static boolean isBetween(Date baseDate, Date startDate, Date endDate,
            boolean startFlag, boolean endFlag) {
        // ちょっとロジックを回りくどく作っちゃったけど、とりあえず動く。
        if (baseDate == null || startDate == null || endDate == null) {
            return false;
        }
        boolean isAfter = baseDate.after(startDate)
                || baseDate.equals(startDate);
        boolean isBefore = baseDate.before(endDate) || baseDate.equals(endDate);
        boolean isBetween = isAfter && isBefore;
        if (isBetween) {
            // startFlagが立ってる場合 同日付はOKとしてtrue.立ってない場合、同日付はfalse(同日付でないならtrue)
            boolean startState = startFlag || !baseDate.equals(startDate);
            // endFlagが立ってる場合 同日付はOKとしてtrue.立ってない場合、同日付はfalse(同日付でないならtrue)
            boolean endState = endFlag || !baseDate.equals(endDate);
            return isBetween && startState && endState;
        }
        return false;
    }

    private static void print(Set<String>[] sets) {
        for (Set<String> set : sets) {
            System.out.println("-------");
            System.out.println(set);
            for (String serial : set) {
                Date targetDate = excelSerialValue2Date(serial);
                System.out.println(targetDate);
            }
            System.out.println("-------");
        }
    }

    public static String date2Str(Date date, String pattern) {
        // SimpleDateFormat fmt = new SimpleDateFormat();
        // fmt.applyPattern(pattern);
        // return fmt.format(date);
        if (date == null) {
            return null;
        }
        return DateFormatUtils.format(date, pattern);
    }

    public static boolean isNonZeroNumeric(double d) {
        return !Double.isNaN(d) && d != 0.0d;
    }

    public static double round(double d) {
        return round(d, 5);
    }

    public static double round(double d, int scale) {
        // if (!Double.isNaN(d)) {
        // d = Math.round(d * 1000.0) / 1000.0;
        // }
        // return d;
        if (Double.isNaN(d)) {
            return d;
        }
        // return d;
        return new BigDecimal(d).setScale(scale, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    public static Double round(Double d) {
        return round(d, 5);
    }

    public static Double round(Double d, int scale) {
        if (d == null || Double.isNaN(d)) {
            return d;
        }
        BigDecimal org = new BigDecimal(d);
        BigDecimal rounded = org.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return rounded.doubleValue();
    }

    public static String[] parseCommna(String prefixs) {
        String[] split = StringUtils.stripAll(StringUtils.split(prefixs, ','));
        // String[] split = prefixs.split(",");
        return split;
    }

    public static boolean contains(String input, String... strs) {
        if (input == null) {
            return false;
        }
        if (strs == null || strs.length == 0) {
            return false;
        }
        for (String str : strs) {
            if (input.contains(str)) {
                return true;
            }
        }
        return false;
    }
}
