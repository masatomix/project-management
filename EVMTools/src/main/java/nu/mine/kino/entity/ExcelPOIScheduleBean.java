/******************************************************************************
 * Copyright (c) 2008-2009 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/

package nu.mine.kino.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 * @version $Revision$
 */
public class ExcelPOIScheduleBean extends ExcelScheduleBean {

    /**
     * ﾀｽｸID
     */
    private String taskId;

    /**
     * 予定工数
     */
    private Double numberOfManDays;

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
    private Double progressRate;

    /**
     * 稼動予定日数
     */
    private Integer numberOfDays;

    /**
     * Planned Value
     */
    private Double plannedValue;

    /**
     * Earned Value
     */
    private Double earnedValue;

    /**
     * Actual Cost
     */
    private Double actualCost;

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
     * 予定工数をセットする。
     * 
     * @param numberOfManDays
     *            予定工数
     */
    public void setNumberOfManDays(Double numberOfManDays) {
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
    public void setProgressRate(Double progressRate) {
        this.progressRate = progressRate;
    }

    /**
     * 稼動予定日数をセットする。
     * 
     * @param numberOfDays
     *            稼動予定日数
     */
    public void setNumberOfDays(Integer numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    /**
     * Planned Valueをセットする。
     * 
     * @param plannedValue
     *            Planned Value
     */
    public void setPlannedValue(Double plannedValue) {
        this.plannedValue = plannedValue;
    }

    /**
     * Earned Valueをセットする。
     * 
     * @param earnedValue
     *            Earned Value
     */
    public void setEarnedValue(Double earnedValue) {
        this.earnedValue = earnedValue;
    }

    /**
     * Actual Costをセットする。
     * 
     * @param actualCost
     *            Actual Cost
     */
    public void setActualCost(Double actualCost) {
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
     * ﾀｽｸIDを取得する。
     * 
     * @return ﾀｽｸID
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * 予定工数を取得する。
     * 
     * @return 予定工数
     */
    public Double getNumberOfManDays() {
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
    public Double getProgressRate() {
        return progressRate;
    }

    /**
     * 稼動予定日数を取得する。
     * 
     * @return 稼動予定日数
     */
    public Integer getNumberOfDays() {
        return numberOfDays;
    }

    /**
     * Planned Valueを取得する。
     * 
     * @return Planned Value
     */
    public Double getPlannedValue() {
        return plannedValue;
    }

    /**
     * Earned Valueを取得する。
     * 
     * @return Earned Value
     */
    public Double getEarnedValue() {
        return earnedValue;
    }

    /**
     * Actual Costを取得する。
     * 
     * @return Actual Cost
     */
    public Double getActualCost() {
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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("ﾀｽｸID", taskId)
                .append("予定工数", numberOfManDays)
                .append("予定開始日", scheduledStartDate)
                .append("予定終了日", scheduledEndDate).append("実績開始日", startDate)
                .append("実績終了日", endDate).append("進捗率", progressRate)
                .append("稼動予定日数", numberOfDays)
                .append("Planned Value", plannedValue)
                .append("Earned Value", earnedValue)
                .append("Actual Cost", actualCost).append("基準日", baseDate)
                .toString();
    }
}