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
 * 画面で使用するAC情報
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 */
public class BaseACViewBean {

    /**
     * id
     */
    private String id;

    /**
     * ﾀｽｸID
     */
    private String taskId;

    /**
     * Actual Cost
     */
    private double actualCost;

    /**
     * 基準日
     */
    private java.util.Date baseDate;

    /**
     * 分類 (From Task)
     */
    private String type;

    /**
     * タスクの＃に該当する列 (From Task)
     */
    private String taskSharp;

    /**
     * タスク名 (From Task)
     */
    private String taskName;

    /**
     * 担当 (From Task)
     */
    private String personInCharge;

    /**
     * タスク概要 (From Task)
     */
    private String taskAbstract;

    /**
     * 予定工数 (From Task)
     */
    private double numberOfManDays;

    /**
     * 予定開始日 (From Task)
     */
    private java.util.Date scheduledStartDate;

    /**
     * 予定終了日 (From Task)
     */
    private java.util.Date scheduledEndDate;

    /**
     * 稼動予定日数 (From Task)
     */
    private int numberOfDays;

    /**
     * idをセットする。
     * 
     * @param id
     *            id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * ﾀｽｸIDをセットする。
     * 
     * @param taskId
     *            ﾀｽｸID
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * Actual Costをセットする。
     * 
     * @param actualCost
     *            Actual Cost
     */
    public void setActualCost(double actualCost) {
        this.actualCost = actualCost;
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
     * 分類をセットする。
     * 
     * @param type
     *            分類
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * タスクの＃に該当する列をセットする。
     * 
     * @param taskSharp
     *            タスクの＃に該当する列
     */
    public void setTaskSharp(String taskSharp) {
        this.taskSharp = taskSharp;
    }

    /**
     * タスク名をセットする。
     * 
     * @param taskName
     *            タスク名
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * 担当をセットする。
     * 
     * @param personInCharge
     *            担当
     */
    public void setPersonInCharge(String personInCharge) {
        this.personInCharge = personInCharge;
    }

    /**
     * タスク概要をセットする。
     * 
     * @param taskAbstract
     *            タスク概要
     */
    public void setTaskAbstract(String taskAbstract) {
        this.taskAbstract = taskAbstract;
    }

    /**
     * 予定工数をセットする。
     * 
     * @param numberOfManDays
     *            予定工数
     */
    public void setNumberOfManDays(double numberOfManDays) {
        this.numberOfManDays = numberOfManDays;
    }

    /**
     * 予定開始日をセットする。
     * 
     * @param scheduledStartDate
     *            予定開始日
     */
    public void setScheduledStartDate(java.util.Date scheduledStartDate) {
        this.scheduledStartDate = scheduledStartDate;
    }

    /**
     * 予定終了日をセットする。
     * 
     * @param scheduledEndDate
     *            予定終了日
     */
    public void setScheduledEndDate(java.util.Date scheduledEndDate) {
        this.scheduledEndDate = scheduledEndDate;
    }

    /**
     * 稼動予定日数をセットする。
     * 
     * @param numberOfDays
     *            稼動予定日数
     */
    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    /**
     * idを取得する。
     * 
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * ﾀｽｸIDを取得する。
     * 
     * @return ﾀｽｸID
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * Actual Costを取得する。
     * 
     * @return Actual Cost
     */
    public double getActualCost() {
        return actualCost;
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
     * 分類を取得する。
     * 
     * @return 分類
     */
    public String getType() {
        return type;
    }

    /**
     * タスクの＃に該当する列を取得する。
     * 
     * @return タスクの＃に該当する列
     */
    public String getTaskSharp() {
        return taskSharp;
    }

    /**
     * タスク名を取得する。
     * 
     * @return タスク名
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * 担当を取得する。
     * 
     * @return 担当
     */
    public String getPersonInCharge() {
        return personInCharge;
    }

    /**
     * タスク概要を取得する。
     * 
     * @return タスク概要
     */
    public String getTaskAbstract() {
        return taskAbstract;
    }

    /**
     * 予定工数を取得する。
     * 
     * @return 予定工数
     */
    public double getNumberOfManDays() {
        return numberOfManDays;
    }

    /**
     * 予定開始日を取得する。
     * 
     * @return 予定開始日
     */
    public java.util.Date getScheduledStartDate() {
        return scheduledStartDate;
    }

    /**
     * 予定終了日を取得する。
     * 
     * @return 予定終了日
     */
    public java.util.Date getScheduledEndDate() {
        return scheduledEndDate;
    }

    /**
     * 稼動予定日数を取得する。
     * 
     * @return 稼動予定日数
     */
    public int getNumberOfDays() {
        return numberOfDays;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id)
                .append("ﾀｽｸID", taskId).append("Actual Cost", actualCost)
                .append("基準日", baseDate).append("分類", type)
                .append("タスクの＃に該当する列", taskSharp).append("タスク名", taskName)
                .append("担当", personInCharge).append("タスク概要", taskAbstract)
                .append("予定工数", numberOfManDays)
                .append("予定開始日", scheduledStartDate)
                .append("予定終了日", scheduledEndDate)
                .append("稼動予定日数", numberOfDays).toString();
    }
}