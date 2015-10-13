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
public class TextScheduleBean {

    /**
     * ﾀｽｸID
     */
    private String taskId;

    /**
     * 分類
     */
    private String type;

    /**
     * タスクの＃に該当する列
     */
    private String taskSharp;

    /**
     * タスク名
     */
    private String taskName;

    /**
     * タスク名の1インデントされた列
     */
    private String taskName1;

    /**
     * タスク名の2インデントされた列
     */
    private String taskName2;

    /**
     * タスク名の3インデントされた列
     */
    private String taskName3;

    /**
     * 先行ﾀｽｸID
     */
    private String senkoTaskId;

    /**
     * 担当
     */
    private String personInCharge;

    /**
     * タスク概要
     */
    private String taskAbstract;

    /**
     * 主要アウトプット
     */
    private String output;

    /**
     * 先週の活動実績報告
     */
    private String reportOfLastWeek;

    /**
     * 今週の活動予定
     */
    private String reportOfThisWeek;

    /**
     * 状況
     */
    private String status;

    /**
     * 予定工数
     */
    private double numberOfManDays;

    /**
     * 予定開始日
     */
    private java.util.Date scheduledStartDate;

    /**
     * 予定終了日
     */
    private java.util.Date scheduledEndDate;

    /**
     * 実績開始日
     */
    private java.util.Date startDate;

    /**
     * 実績終了日
     */
    private java.util.Date endDate;

    /**
     * 進捗率
     */
    private double progressRate;

    /**
     * 稼動予定日数
     */
    private int numberOfDays;

    /**
     * Planned Value
     */
    private double plannedValue;

    /**
     * Earned Value
     */
    private double earnedValue;

    /**
     * Actual Cost
     */
    private double actualCost;

    /**
     * 備考
     */
    private String remarks;

    /**
     * 基準日
     */
    private java.util.Date baseDate;

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
     * タスク名の1インデントされた列をセットする。
     * 
     * @param taskName1
     *            タスク名の1インデントされた列
     */
    public void setTaskName1(String taskName1) {
        this.taskName1 = taskName1;
    }

    /**
     * タスク名の2インデントされた列をセットする。
     * 
     * @param taskName2
     *            タスク名の2インデントされた列
     */
    public void setTaskName2(String taskName2) {
        this.taskName2 = taskName2;
    }

    /**
     * タスク名の3インデントされた列をセットする。
     * 
     * @param taskName3
     *            タスク名の3インデントされた列
     */
    public void setTaskName3(String taskName3) {
        this.taskName3 = taskName3;
    }

    /**
     * 先行ﾀｽｸIDをセットする。
     * 
     * @param senkoTaskId
     *            先行ﾀｽｸID
     */
    public void setSenkoTaskId(String senkoTaskId) {
        this.senkoTaskId = senkoTaskId;
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
     * 主要アウトプットをセットする。
     * 
     * @param output
     *            主要アウトプット
     */
    public void setOutput(String output) {
        this.output = output;
    }

    /**
     * 先週の活動実績報告をセットする。
     * 
     * @param reportOfLastWeek
     *            先週の活動実績報告
     */
    public void setReportOfLastWeek(String reportOfLastWeek) {
        this.reportOfLastWeek = reportOfLastWeek;
    }

    /**
     * 今週の活動予定をセットする。
     * 
     * @param reportOfThisWeek
     *            今週の活動予定
     */
    public void setReportOfThisWeek(String reportOfThisWeek) {
        this.reportOfThisWeek = reportOfThisWeek;
    }

    /**
     * 状況をセットする。
     * 
     * @param status
     *            状況
     */
    public void setStatus(String status) {
        this.status = status;
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
     * 実績開始日をセットする。
     * 
     * @param startDate
     *            実績開始日
     */
    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }

    /**
     * 実績終了日をセットする。
     * 
     * @param endDate
     *            実績終了日
     */
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }

    /**
     * 進捗率をセットする。
     * 
     * @param progressRate
     *            進捗率
     */
    public void setProgressRate(double progressRate) {
        this.progressRate = progressRate;
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
     * Planned Valueをセットする。
     * 
     * @param plannedValue
     *            Planned Value
     */
    public void setPlannedValue(double plannedValue) {
        this.plannedValue = plannedValue;
    }

    /**
     * Earned Valueをセットする。
     * 
     * @param earnedValue
     *            Earned Value
     */
    public void setEarnedValue(double earnedValue) {
        this.earnedValue = earnedValue;
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
     * 備考をセットする。
     * 
     * @param remarks
     *            備考
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
     * ﾀｽｸIDを取得する。
     * 
     * @return ﾀｽｸID
     */
    public String getTaskId() {
        return taskId;
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
     * タスク名の1インデントされた列を取得する。
     * 
     * @return タスク名の1インデントされた列
     */
    public String getTaskName1() {
        return taskName1;
    }

    /**
     * タスク名の2インデントされた列を取得する。
     * 
     * @return タスク名の2インデントされた列
     */
    public String getTaskName2() {
        return taskName2;
    }

    /**
     * タスク名の3インデントされた列を取得する。
     * 
     * @return タスク名の3インデントされた列
     */
    public String getTaskName3() {
        return taskName3;
    }

    /**
     * 先行ﾀｽｸIDを取得する。
     * 
     * @return 先行ﾀｽｸID
     */
    public String getSenkoTaskId() {
        return senkoTaskId;
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
     * 主要アウトプットを取得する。
     * 
     * @return 主要アウトプット
     */
    public String getOutput() {
        return output;
    }

    /**
     * 先週の活動実績報告を取得する。
     * 
     * @return 先週の活動実績報告
     */
    public String getReportOfLastWeek() {
        return reportOfLastWeek;
    }

    /**
     * 今週の活動予定を取得する。
     * 
     * @return 今週の活動予定
     */
    public String getReportOfThisWeek() {
        return reportOfThisWeek;
    }

    /**
     * 状況を取得する。
     * 
     * @return 状況
     */
    public String getStatus() {
        return status;
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
     * 実績開始日を取得する。
     * 
     * @return 実績開始日
     */
    public java.util.Date getStartDate() {
        return startDate;
    }

    /**
     * 実績終了日を取得する。
     * 
     * @return 実績終了日
     */
    public java.util.Date getEndDate() {
        return endDate;
    }

    /**
     * 進捗率を取得する。
     * 
     * @return 進捗率
     */
    public double getProgressRate() {
        return progressRate;
    }

    /**
     * 稼動予定日数を取得する。
     * 
     * @return 稼動予定日数
     */
    public int getNumberOfDays() {
        return numberOfDays;
    }

    /**
     * Planned Valueを取得する。
     * 
     * @return Planned Value
     */
    public double getPlannedValue() {
        return plannedValue;
    }

    /**
     * Earned Valueを取得する。
     * 
     * @return Earned Value
     */
    public double getEarnedValue() {
        return earnedValue;
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
     * 備考を取得する。
     * 
     * @return 備考
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 基準日を取得する。
     * 
     * @return 基準日
     */
    public java.util.Date getBaseDate() {
        return baseDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("ﾀｽｸID", taskId)
                // .append("分類", type).append("タスクの＃に該当する列", taskSharp)
                .append("タスク名", taskName)
                // .append("タスク名の1インデントされた列", taskName1)
                // .append("タスク名の2インデントされた列", taskName2)
                // .append("タスク名の3インデントされた列", taskName3)
                .append("先行ﾀｽｸID", senkoTaskId)
                .append("担当", personInCharge)
                // .append("タスク概要", taskAbstract).append("主要アウトプット", output)
                // .append("先週の活動実績報告", reportOfLastWeek)
                // .append("今週の活動予定", reportOfThisWeek).append("状況", status)
                .append("予定工数", numberOfManDays)
                .append("予定開始日", scheduledStartDate)
                .append("予定終了日", scheduledEndDate)
                // .append("実績開始日", startDate)
                // .append("実績終了日", endDate)
                .append("進捗率", progressRate).append("稼動予定日数", numberOfDays)
                .append("Planned Value", plannedValue)
                .append("Earned Value", earnedValue)
                .append("Actual Cost", actualCost).append("備考", remarks)
                .append("基準日", baseDate).toString();
    }
}