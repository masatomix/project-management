/******************************************************************************
 * Copyright (c) 2012 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//作成日: 2014/11/04

package nu.mine.kino.projects;

import static nu.mine.kino.projects.utils.PoiUtils.getTaskId;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.java.amateras.xlsbeans.XLSBeans;
import net.java.amateras.xlsbeans.XLSBeansException;
import nu.mine.kino.entity.ACTotalBean;
import nu.mine.kino.entity.EVTotalBean;
import nu.mine.kino.entity.ExcelPOIScheduleBean;
import nu.mine.kino.entity.ExcelScheduleBean;
import nu.mine.kino.entity.ExcelScheduleBean2ACTotalBean;
import nu.mine.kino.entity.ExcelScheduleBean2EVTotalBean;
import nu.mine.kino.entity.ExcelScheduleBean2PVTotalBean;
import nu.mine.kino.entity.ExcelScheduleBean2Task;
import nu.mine.kino.entity.ExcelScheduleBean2TaskInformation;
import nu.mine.kino.entity.ExcelScheduleBeanSheet2Project;
import nu.mine.kino.entity.Holiday;
import nu.mine.kino.entity.PVTotalBean;
import nu.mine.kino.entity.Project;
import nu.mine.kino.entity.Row2ExcelPOIScheduleBean;
import nu.mine.kino.entity.Task;
import nu.mine.kino.entity.TaskInformation;
import nu.mine.kino.projects.utils.PoiUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.bbreak.excella.core.util.PoiUtil;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class ExcelProjectCreator extends InputStreamProjectCreator {

    // public ExcelProjectCreator(InputStream in) {
    // super(in);
    // }

    private final File file;

    public ExcelProjectCreator(File file) throws ProjectException {
        super(file);
        this.file = file;
    }

    /**
     * Excel フォーマットのFile(InputStream)からProjectを作成する
     * 
     * @see nu.mine.kino.projects.ProjectCreator#createProject(java.io.InputStream)
     */
    @Override
    public Project createProjectFromStream() throws ProjectException {

        Map<String, ExcelPOIScheduleBean> poiMap = new HashMap<String, ExcelPOIScheduleBean>();
        Holiday[] holidays = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            Workbook workbook = null;
            workbook = WorkbookFactory.create(in);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> e = sheet.rowIterator();
            int index = 0;
            int dataIndex = PoiUtils.getDataFirstRowNum(sheet);
            while (e.hasNext()) {
                // ヘッダが終わるまで飛ばす。
                if (index < dataIndex) {
                    e.next();
                    index++;
                    continue;
                }
                // データ部の処理
                Row row = e.next();
                Cell taskIdCell = row.getCell(1);
                String taskId = getTaskId(taskIdCell);
                poiMap.put(taskId, createPOIBean(row));
            }

            holidays = createHolidays(workbook);
            // for (Holiday holiday : holidays) {
            // System.out.println(holiday);
            // }

        } catch (InvalidFormatException e) {
            throw new ProjectException(e);
        } catch (FileNotFoundException e) {
            throw new ProjectException(e);
        } catch (IOException e) {
            throw new ProjectException(e);
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                }
        }

        try {
            List<TaskInformation> taskInfoList = new ArrayList<TaskInformation>();
            ExcelScheduleBeanSheet sheet = new XLSBeans().load(
                    getInputStream(), ExcelScheduleBeanSheet.class);
            // これだけで、
            // sheet.setExcelScheduleBean(
            // List<ExcelScheduleBean> instanceList);
            // sheet.setBaseDate(String date) ;
            // が呼ばれていると言うこと。
            java.util.List<ExcelScheduleBean> instanceList = sheet
                    .getExcelScheduleBean();

            for (ExcelScheduleBean instance : instanceList) {
                if (!instance.getId().equals("")) {
                    instance.setBaseDate(sheet.getBaseDate());
                    Task task = ExcelScheduleBean2Task.convert(instance);
                    if (StringUtils.isEmpty(task.getTaskId())) {
                        String message = String.format(
                                "id: %s のタスクIDが未記載です。必須項目のためエラーとして処理を終了します。",
                                task.getId());
                        throw new ProjectException(message);
                    }

                    PVTotalBean pvTotalBean = ExcelScheduleBean2PVTotalBean
                            .convert(instance);
                    ACTotalBean acTotalBean = ExcelScheduleBean2ACTotalBean
                            .convert(instance);
                    EVTotalBean evTotalBean = ExcelScheduleBean2EVTotalBean
                            .convert(instance);
                    TaskInformation taskInfo = ExcelScheduleBean2TaskInformation
                            .convert(instance);
                    taskInfo.setTask(task);
                    taskInfo.setPV(pvTotalBean);
                    taskInfo.setAC(acTotalBean);
                    taskInfo.setEV(evTotalBean);
                    taskInfoList.add(taskInfo);

                    ExcelPOIScheduleBean poiBean = poiMap.get(instance
                            .getTaskId());
                    // System.out.print("poi: ");
                    // System.out.println(poiBean);

                    // //// ココで、NULLでないばあいの載せ替えを実施。全部。
                    if (poiBean != null) {
                        setPV(poiBean, pvTotalBean);
                        setAC(poiBean, acTotalBean);
                        setEV(poiBean, evTotalBean);
                        setTask(poiBean, task);
                    }

                }
            }

            Project project = new Project();
            project.setTaskInformations(taskInfoList
                    .toArray(new TaskInformation[taskInfoList.size()]));

            ExcelScheduleBeanSheet2Project.convert(sheet, project);

            project.setHolidays(holidays);
            // System.out.println("---");
            // System.out.println(project.getProjectStartDate());
            // System.out.println(project.getProjectEndDate());
            // System.out.println("---");
            return project;
        } catch (XLSBeansException e) {
            throw new ProjectException(e);
            // } finally {
            // if (in != null) {
            // try {
            // in.close();
            // } catch (IOException e) {
            // throw new ProjectException(e);
            // }
            // }
        }
    }

    private Holiday[] createHolidays(Workbook workbook) {
        Sheet sheet = workbook.getSheet("休日テーブル");

        List<Holiday> arrayList = new ArrayList<Holiday>();
        Iterator<Row> e = sheet.rowIterator();
        while (e.hasNext()) {
            Row row = e.next();
            Holiday holiday = new Holiday();

            Cell dateCell = row.getCell(0);
            if (dateCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                if (PoiUtil.isCellDateFormatted(dateCell)) {
                    Date dateCellValue = dateCell.getDateCellValue();
                    holiday.setDate(dateCellValue);
                }
                arrayList.add(holiday);
            }

            // Cell cell1 = row.getCell(1);
            // if (cell1 != null && cell1.getCellType() ==
            // Cell.CELL_TYPE_FORMULA) {
            // holiday.setDayOfWeek((String) PoiUtils.getCellValue(cell1,
            // String.class));
            // }

            Cell cell2 = row.getCell(2);
            if (cell2 != null && cell2.getCellType() == Cell.CELL_TYPE_STRING) {
                holiday.setName(cell2.getStringCellValue());
            }
            Cell cell3 = row.getCell(3);
            if (cell3 != null && cell3.getCellType() == Cell.CELL_TYPE_STRING) {
                holiday.setRule(cell3.getStringCellValue());
            }
            Cell cell4 = row.getCell(4);
            if (cell4 != null && cell4.getCellType() == Cell.CELL_TYPE_STRING) {
                holiday.setHurikae(cell4.getStringCellValue());
            }

        }
        return arrayList.toArray(new Holiday[arrayList.size()]);
    }

    private void setTask(ExcelPOIScheduleBean source, Task dest) {
        if (source.getScheduledEndDate() != null) {
            dest.setScheduledEndDate(source.getScheduledEndDate());
        }
        if (source.getScheduledStartDate() != null) {
            dest.setScheduledStartDate(source.getScheduledStartDate());
        }
        if (source.getNumberOfDays() != null) {
            dest.setNumberOfDays(source.getNumberOfDays());
        }
        dest.setNumberOfManDays(source.getNumberOfManDays() == null ? Double.NaN
                : source.getNumberOfManDays());
    }

    private void setEV(ExcelPOIScheduleBean source, EVTotalBean dest) {
        dest.setEarnedValue(source.getEarnedValue() == null ? Double.NaN
                : source.getEarnedValue());
        dest.setProgressRate(source.getProgressRate() == null ? Double.NaN
                : source.getProgressRate());
        if (source.getEndDate() != null) {
            dest.setEndDate(source.getEndDate());
        }
        if (source.getStartDate() != null) {
            dest.setStartDate(source.getStartDate());
        }
    }

    private void setAC(ExcelPOIScheduleBean source, ACTotalBean dest) {
        dest.setActualCost(source.getActualCost() == null ? Double.NaN : source
                .getActualCost());
    }

    private void setPV(ExcelPOIScheduleBean source, PVTotalBean dest) {
        dest.setPlannedValue(source.getPlannedValue() == null ? Double.NaN
                : source.getPlannedValue());
    }

    private ExcelPOIScheduleBean createPOIBean(Row row) {
        // Cell taskIdCell = row.getCell(1);
        // String taskId = getTaskId(taskIdCell);
        // // 15 予定工数
        // // 20 進捗率 0.8とかそういう値が取れる
        // // 21 稼働予定日数
        // // 22 PV
        // // 23 EV
        // // 24 AC
        // // PoiUtils.getCellValue(row.getCell(15), Double.class);
        // // PoiUtils.getCellValue(row.getCell(20), Double.class);
        // // PoiUtils.getCellValue(row.getCell(22), Double.class);
        // // PoiUtils.getCellValue(row.getCell(23), Double.class);
        // // PoiUtils.getCellValue(row.getCell(24), Double.class);
        // System.out.printf("[%s],[%s],[%s],[%s],[%s],[%s],", taskId,
        // PoiUtils.getCellValue(row.getCell(15), Double.class),
        // PoiUtils.getCellValue(row.getCell(20), Double.class),
        // PoiUtils.getCellValue(row.getCell(21), Integer.class),
        // PoiUtils.getCellValue(row.getCell(22), Double.class),
        // PoiUtils.getCellValue(row.getCell(23), Double.class),
        // PoiUtils.getCellValue(row.getCell(24), Double.class));
        // // row.getCell(15), row.getCell(20), row.getCell(22),
        // // row.getCell(23), row.getCell(24));
        //
        // // 16 予定開始日
        // // 17 予定終了日
        // // 18 実績開始日
        // // 19 実績終了日
        // Date sDate = getDate(row.getCell(16));
        // Date eDate = getDate(row.getCell(17));
        // Date asDate = getDate(row.getCell(18));
        // Date aeDate = getDate(row.getCell(19));
        //
        // String pattern = "yyyy/MM/dd";
        // System.out.printf("[%s],[%s],[%s],[%s],[%s]\n", taskId,
        // Utils.date2Str(sDate, pattern), Utils.date2Str(eDate, pattern),
        // Utils.date2Str(asDate, pattern),
        // Utils.date2Str(aeDate, pattern));
        ExcelPOIScheduleBean bean = Row2ExcelPOIScheduleBean.convert(row);
        return bean;
    }
}
