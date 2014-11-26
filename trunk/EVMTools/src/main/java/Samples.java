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

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;

import net.java.amateras.xlsbeans.XLSBeans;
import net.java.amateras.xlsbeans.XLSBeansException;
import net.java.amateras.xlsbeans.annotation.Cell;
import net.java.amateras.xlsbeans.annotation.HorizontalRecords;
import net.java.amateras.xlsbeans.annotation.RecordTerminal;
import net.java.amateras.xlsbeans.annotation.Sheet;
import nu.mine.kino.entity.ExcelScheduleBean;
import nu.mine.kino.entity.ExcelScheduleBean2Task;
import nu.mine.kino.entity.Task;

import org.apache.commons.lang.time.DateUtils;

/**
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 * @version $Revision$
 */
//@Sheet(name = "ガントチャート（入力例）")//$NON-NLS-1$
@Sheet(number = 0)
public class Samples {
    private java.util.List<ExcelScheduleBean> instanceList;

    private Date baseDate;

    @HorizontalRecords(tableLabel = "#Gantt", recordClass = ExcelScheduleBean.class, terminal = RecordTerminal.Border)//$NON-NLS-1$
    public void setExcelScheduleBean(
            java.util.List<ExcelScheduleBean> instanceList) {
        this.instanceList = instanceList;
    }

    public java.util.List<ExcelScheduleBean> getExcelScheduleBean() {
        return instanceList;
    }

    @Cell(row = 0, column = 25)
    public void setBaseDate(String date) throws ParseException {
        this.baseDate = DateUtils.parseDate(date, new String[] { "yyyyMMdd",
                "yy/MM/dd" });
    }

    public Date getBaseDate() {
        return baseDate;
    }

    public static void main(String[] args) throws FileNotFoundException,
            XLSBeansException, IllegalAccessException,
            InvocationTargetException {
        java.io.InputStream in = new java.io.FileInputStream(
                "project_management_tools.xls");
        Samples sheet = new XLSBeans().load(in, Samples.class);
        java.util.List<ExcelScheduleBean> instanceList = sheet
                .getExcelScheduleBean();
        for (ExcelScheduleBean instance : instanceList) {
            Task task = ExcelScheduleBean2Task.convert(instance);

            if (!instance.getId().equals("")) {
                System.out.println(instance);
            }
        }
        System.out.println(instanceList.size());
        System.out.println(sheet.getBaseDate());
    }

}
