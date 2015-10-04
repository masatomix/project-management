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

//import net.java.amateras.xlsbeans.annotation.Column;
//import net.java.amateras.xlsbeans.annotation.MapColumns;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Excelデータを読み込んで管理するJavaBeans。ほぼPOIに取って代わられて、
 * POIで取得したJavaBeansからフィールドにデータをコピーしてもらっている。
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 */
public class ExcelScheduleBean {

    /**
     * ﾀｽｸID
     */
    private String taskId;

    /**
     * プロットされた□のデータ
     */
    private java.util.Map<String, String> plotDataMap;

    /**
     * ﾀｽｸIDをセットする。
     * 
     * @param taskId
     *            ﾀｽｸID
     */
    //    @Column(columnName = "ﾀｽｸID")//$NON-NLS-1$
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * ガントチャートをセットする。
     * 
     * @param plotDataMap
     *            ガントチャート
     */
    // @MapColumns(previousColumnName = "備考")
    public void setPlotDataMap(java.util.Map<String, String> plotDataMap) {
        this.plotDataMap = plotDataMap;
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
     * ガントチャートを取得する。
     * 
     * @return ガントチャート
     */
    public java.util.Map<String, String> getPlotDataMap() {
        return plotDataMap;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("ﾀｽｸID", taskId)
                .append("ガントチャート", plotDataMap).toString();
    }
}