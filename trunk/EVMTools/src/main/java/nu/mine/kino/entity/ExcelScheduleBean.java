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

import net.java.amateras.xlsbeans.annotation.MapColumns;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Excelデータを読み込んで管理するJavaBeans。ほぼPOIに取って代わられて、
 * POIで取得したJavaBeansからフィールドにデータをコピーしてもらっている。
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 */
public class ExcelScheduleBean extends ExcelPOIScheduleBean {

    /**
     * プロットされた□のデータ
     */
    private java.util.Map<String, String> plotDataMap;

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
     * ガントチャートを取得する。
     * 
     * @return ガントチャート
     */
    public java.util.Map<String, String> getPlotDataMap() {
        return plotDataMap;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("ガントチャート", plotDataMap)
                .toString();
    }
}