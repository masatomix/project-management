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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 */
public class BaseHoliday {

    /**
     * 日付
     */
    private java.util.Date date;

    /**
     * 曜日
     */
    private String dayOfWeek;

    /**
     * 祝日
     */
    private String name;

    /**
     * 祝日定義ルール
     */
    private String rule;

    /**
     * 振替
     */
    private String hurikae;

    /**
     * 日付をセットする。
     * 
     * @param date
     *            日付
     */
    public void setDate(java.util.Date date) {
        this.date = date;
    }

    /**
     * 曜日をセットする。
     * 
     * @param dayOfWeek
     *            曜日
     */
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * 祝日をセットする。
     * 
     * @param name
     *            祝日
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 祝日定義ルールをセットする。
     * 
     * @param rule
     *            祝日定義ルール
     */
    public void setRule(String rule) {
        this.rule = rule;
    }

    /**
     * 振替をセットする。
     * 
     * @param hurikae
     *            振替
     */
    public void setHurikae(String hurikae) {
        this.hurikae = hurikae;
    }

    /**
     * 日付を取得する。
     * 
     * @return 日付
     */
    public java.util.Date getDate() {
        return date;
    }

    /**
     * 曜日を取得する。
     * 
     * @return 曜日
     */
    public String getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * 祝日を取得する。
     * 
     * @return 祝日
     */
    public String getName() {
        return name;
    }

    /**
     * 祝日定義ルールを取得する。
     * 
     * @return 祝日定義ルール
     */
    public String getRule() {
        return rule;
    }

    /**
     * 振替を取得する。
     * 
     * @return 振替
     */
    public String getHurikae() {
        return hurikae;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("日付", date)
                .append("曜日", dayOfWeek).append("祝日", name)
                .append("祝日定義ルール", rule).append("振替", hurikae).toString();
    }
}