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

import net.java.amateras.xlsbeans.annotation.Column;
import net.java.amateras.xlsbeans.annotation.MapColumns;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 * @version $Revision$
 */
public class ExcelScheduleBean {

    /**
     * id
     */
    private String id;

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
     * 備考
     */
    private String remarks;

    /**
     * 基準日
     */
    private java.util.Date baseDate;

    /**
     * プロットされた□のデータ
     */
    private java.util.Map<String, String> plotDataMap;

    /**
     * idをセットする。
     * 
     * @param id
     *            id
     */
    @Column(columnName = "#")//$NON-NLS-1$
    public void setId(String id) {
        this.id = id;
    }

    /**
     * ﾀｽｸIDをセットする。
     * 
     * @param taskId
     *            ﾀｽｸID
     */
    @Column(columnName = "ﾀｽｸID")//$NON-NLS-1$
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * 分類をセットする。
     * 
     * @param type
     *            分類
     */
    @Column(columnName = "分類")//$NON-NLS-1$
    public void setType(String type) {
        this.type = type;
    }

    /**
     * タスクの＃に該当する列をセットする。
     * 
     * @param taskSharp
     *            タスクの＃に該当する列
     */
    @Column(columnName = "a")//$NON-NLS-1$
    public void setTaskSharp(String taskSharp) {
        this.taskSharp = taskSharp;
    }

    /**
     * タスク名をセットする。
     * 
     * @param taskName
     *            タスク名
     */
    @Column(columnName = "タスク")//$NON-NLS-1$
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * タスク名の1インデントされた列をセットする。
     * 
     * @param taskName1
     *            タスク名の1インデントされた列
     */
    @Column(columnName = "b")//$NON-NLS-1$
    public void setTaskName1(String taskName1) {
        this.taskName1 = taskName1;
    }

    /**
     * タスク名の2インデントされた列をセットする。
     * 
     * @param taskName2
     *            タスク名の2インデントされた列
     */
    @Column(columnName = "c")//$NON-NLS-1$
    public void setTaskName2(String taskName2) {
        this.taskName2 = taskName2;
    }

    /**
     * タスク名の3インデントされた列をセットする。
     * 
     * @param taskName3
     *            タスク名の3インデントされた列
     */
    @Column(columnName = "d")//$NON-NLS-1$
    public void setTaskName3(String taskName3) {
        this.taskName3 = taskName3;
    }

    /**
     * 先行ﾀｽｸIDをセットする。
     * 
     * @param senkoTaskId
     *            先行ﾀｽｸID
     */
    @Column(columnName = "先行ﾀｽｸID")//$NON-NLS-1$
    public void setSenkoTaskId(String senkoTaskId) {
        this.senkoTaskId = senkoTaskId;
    }

    /**
     * 担当をセットする。
     * 
     * @param personInCharge
     *            担当
     */
    @Column(columnName = "担当")//$NON-NLS-1$
    public void setPersonInCharge(String personInCharge) {
        this.personInCharge = personInCharge;
    }

    /**
     * タスク概要をセットする。
     * 
     * @param taskAbstract
     *            タスク概要
     */
    @Column(columnName = "タスク概要")//$NON-NLS-1$
    public void setTaskAbstract(String taskAbstract) {
        this.taskAbstract = taskAbstract;
    }

    /**
     * 主要アウトプットをセットする。
     * 
     * @param output
     *            主要アウトプット
     */
    @Column(columnName = "主要アウトプット")//$NON-NLS-1$
    public void setOutput(String output) {
        this.output = output;
    }

    /**
     * 先週の活動実績報告をセットする。
     * 
     * @param reportOfLastWeek
     *            先週の活動実績報告
     */
    @Column(columnName = "先週の活動実績報告")//$NON-NLS-1$
    public void setReportOfLastWeek(String reportOfLastWeek) {
        this.reportOfLastWeek = reportOfLastWeek;
    }

    /**
     * 今週の活動予定をセットする。
     * 
     * @param reportOfThisWeek
     *            今週の活動予定
     */
    @Column(columnName = "今週の活動予定")//$NON-NLS-1$
    public void setReportOfThisWeek(String reportOfThisWeek) {
        this.reportOfThisWeek = reportOfThisWeek;
    }

    /**
     * 状況をセットする。
     * 
     * @param status
     *            状況
     */
    @Column(columnName = "状況")//$NON-NLS-1$
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 備考をセットする。
     * 
     * @param remarks
     *            備考
     */
    @Column(columnName = "備考")//$NON-NLS-1$
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
     * ガントチャートをセットする。
     * 
     * @param plotDataMap
     *            ガントチャート
     */
    @MapColumns(previousColumnName = "備考")
    public void setPlotDataMap(java.util.Map<String, String> plotDataMap) {
        this.plotDataMap = plotDataMap;
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

    /**
     * ガントチャートを取得する。
     * 
     * @return ガントチャート
     */
    public java.util.Map<String, String> getPlotDataMap() {
        return plotDataMap;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id)
                .append("ﾀｽｸID", taskId).append("分類", type)
                .append("タスクの＃に該当する列", taskSharp).append("タスク名", taskName)
                .append("タスク名の1インデントされた列", taskName1)
                .append("タスク名の2インデントされた列", taskName2)
                .append("タスク名の3インデントされた列", taskName3)
                .append("先行ﾀｽｸID", senkoTaskId).append("担当", personInCharge)
                .append("タスク概要", taskAbstract).append("主要アウトプット", output)
                .append("先週の活動実績報告", reportOfLastWeek)
                .append("今週の活動予定", reportOfThisWeek).append("状況", status)
                .append("備考", remarks).append("基準日", baseDate)
                .append("ガントチャート", plotDataMap).toString();
    }
}