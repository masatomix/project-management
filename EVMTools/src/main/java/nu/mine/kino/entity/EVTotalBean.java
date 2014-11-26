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
public class EVTotalBean {

    /**
     * id
     */
    private String id;

    /**
     * ﾀｽｸID
     */
    private String taskId;

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
     * Earned Value の基準日時点までの累積
     */
    private double earnedValue;

    /**
     * 基準日
     */
    private java.util.Date baseDate;

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
     * Earned Valueをセットする。
     * 
     * @param earnedValue
     *            Earned Value
     */
    public void setEarnedValue(double earnedValue) {
        this.earnedValue = earnedValue;
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
     * Earned Valueを取得する。
     * 
     * @return Earned Value
     */
    public double getEarnedValue() {
        return earnedValue;
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
        return new ToStringBuilder(this).append("id", id)
                .append("ﾀｽｸID", taskId).append("実績開始日", startDate)
                .append("実績終了日", endDate).append("進捗率", progressRate)
                .append("Earned Value", earnedValue).append("基準日", baseDate)
                .toString();
    }
}