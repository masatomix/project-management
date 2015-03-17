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
public class Project {

    /**
     * タスク情報
     */
    private nu.mine.kino.entity.TaskInformation[] taskInformations;

    /**
     * プロジェクト開始日
     */
    private java.util.Date projectStartDate;

    /**
     * プロジェクト終了日
     */
    private java.util.Date projectEndDate;

    /**
     * 基準日
     */
    private java.util.Date baseDate;

    /**
     * 休日カレンダー
     */
    private nu.mine.kino.entity.Holiday[] holidays;

    /**
     * タスク情報をセットする。
     * 
     * @param taskInformations
     *            タスク情報
     */
    public void setTaskInformations(
            nu.mine.kino.entity.TaskInformation[] taskInformations) {
        this.taskInformations = taskInformations;
    }

    /**
     * プロジェクト開始日をセットする。
     * 
     * @param projectStartDate
     *            プロジェクト開始日
     */
    public void setProjectStartDate(java.util.Date projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    /**
     * プロジェクト終了日をセットする。
     * 
     * @param projectEndDate
     *            プロジェクト終了日
     */
    public void setProjectEndDate(java.util.Date projectEndDate) {
        this.projectEndDate = projectEndDate;
    }

    /**
     * 基準日をセットする。
     * 
     * @param baseDate
     *            基準日
     */
    public void setBaseDate(java.util.Date baseDate) {
        this.baseDate = baseDate;
    }

    /**
     * 休日カレンダーをセットする。
     * 
     * @param holidays
     *            休日カレンダー
     */
    public void setHolidays(nu.mine.kino.entity.Holiday[] holidays) {
        this.holidays = holidays;
    }

    /**
     * タスク情報を取得する。
     * 
     * @return タスク情報
     */
    public nu.mine.kino.entity.TaskInformation[] getTaskInformations() {
        return taskInformations;
    }

    /**
     * プロジェクト開始日を取得する。
     * 
     * @return プロジェクト開始日
     */
    public java.util.Date getProjectStartDate() {
        return projectStartDate;
    }

    /**
     * プロジェクト終了日を取得する。
     * 
     * @return プロジェクト終了日
     */
    public java.util.Date getProjectEndDate() {
        return projectEndDate;
    }

    /**
     * 基準日を取得する。
     * 
     * @return 基準日
     */
    public java.util.Date getBaseDate() {
        return baseDate;
    }

    /**
     * 休日カレンダーを取得する。
     * 
     * @return 休日カレンダー
     */
    public nu.mine.kino.entity.Holiday[] getHolidays() {
        return holidays;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("タスク情報", taskInformations)
                .append("プロジェクト開始日", projectStartDate)
                .append("プロジェクト終了日", projectEndDate).append("基準日", baseDate)
                .append("休日カレンダー", holidays).toString();
    }
}