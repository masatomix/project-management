package nu.mine.kino.projects;

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

import java.text.ParseException;
import java.util.Date;

import net.java.amateras.xlsbeans.annotation.Cell;
import net.java.amateras.xlsbeans.annotation.HorizontalRecords;
import net.java.amateras.xlsbeans.annotation.RecordTerminal;
import net.java.amateras.xlsbeans.annotation.Sheet;
import nu.mine.kino.entity.ExcelScheduleBean;
import nu.mine.kino.projects.utils.BaseDataUtils;

import org.apache.commons.lang.time.DateUtils;

/**
 * Excelシートからプロジェクト情報を取得するクラス。
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 * @version $Revision$
 */
//@Sheet(name = "ガントチャート（入力例）")//$NON-NLS-1$
@Sheet(number = 0)
public class ExcelScheduleBeanSheet {
    private java.util.List<ExcelScheduleBean> instanceList;

    private Date baseDate;

    private Date projectStartDate;

    private Date projectEndDate;

    @HorizontalRecords(tableLabel = "#Gantt", recordClass = ExcelScheduleBean.class, terminal = RecordTerminal.Border)//$NON-NLS-1$
    public void setExcelScheduleBean(
            java.util.List<ExcelScheduleBean> instanceList) {
        this.instanceList = instanceList;
        instanceList.remove(0);// 一行目はタイトルの邪魔なので、除外。
        if (!instanceList.isEmpty()) {
            Date[] range = BaseDataUtils.getProjectRange(instanceList);
            projectStartDate = range[0];
            projectEndDate = range[1];
        }
    }

    public java.util.List<ExcelScheduleBean> getExcelScheduleBean() {
        return instanceList;
    }

//    // 場所決め打ちで、基準日を取得する。
//    @Cell(row = 0, column = 25)
//    public void setBaseDate(String date) throws ParseException {
//        this.baseDate = DateUtils.parseDate(date, new String[] { "yy/MM/dd" });
//    }
    

    public Date getBaseDate() {
        return baseDate;
    }

    public Date getProjectStartDate() {
        return projectStartDate;
    }

    public Date getProjectEndDate() {
        return projectEndDate;
    }

    public void setBaseDate(Date baseDate) {
        this.baseDate = baseDate;
    }

}
