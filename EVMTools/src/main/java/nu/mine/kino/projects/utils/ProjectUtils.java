/******************************************************************************
 * Copyright (c) 2014 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//作成日: 2014/09/17
package nu.mine.kino.projects.utils;

import static nu.mine.kino.projects.utils.PoiUtils.getTaskId;
import static nu.mine.kino.projects.utils.Utils.isNonZeroNumeric;
import static nu.mine.kino.projects.utils.Utils.round;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nu.mine.kino.entity.ACBean;
import nu.mine.kino.entity.ACTotalBean;
import nu.mine.kino.entity.ACTotalBean2ACBean;
import nu.mine.kino.entity.EVBean;
import nu.mine.kino.entity.EVMViewBean;
import nu.mine.kino.entity.EVTotalBean;
import nu.mine.kino.entity.EVTotalBean2EVBean;
import nu.mine.kino.entity.ExcelPOIScheduleBean;
import nu.mine.kino.entity.Holiday;
import nu.mine.kino.entity.PVBean;
import nu.mine.kino.entity.PVTotalBean;
import nu.mine.kino.entity.PVTotalBean2PVBean;
import nu.mine.kino.entity.Project;
import nu.mine.kino.entity.Row2ExcelPOIScheduleBean;
import nu.mine.kino.entity.Task;
import nu.mine.kino.entity.TaskInformation;
import nu.mine.kino.entity.Validatable;
import nu.mine.kino.projects.ProjectException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.bbreak.excella.core.util.PoiUtil;

/**
 * Project系の様々なUtilityメソッド
 * 
 * @author Masatomi KINO
 * @version $Revision$
 */
public class ProjectUtils {

    public static TaskInformation getTaskInformation(Project project,
            String taskId) {
        TaskInformation[] infos = project.getTaskInformations();
        for (TaskInformation info : infos) {
            String targetTaskId = info.getTaskId();
            if (Utils.isEmpty(targetTaskId)) {
                break;
            }
            if (targetTaskId.equals(taskId)) {
                return info;
            }
        }
        return null;
    }

    /**
     * プロジェクトの開始日付から終了日付までのPV
     * 
     * @param project
     * @return
     */
    public static Map<Date, Double> calculateTotalPVOfProject(Project project) {
        Map<Date, Double> map = new HashMap<Date, Double>();
        Date startDate = project.getProjectStartDate();
        Date endDate = project.getProjectEndDate();
        Date cursor = startDate;
        while (cursor.getTime() <= endDate.getTime()) {
            // System.out.println(cursor);
            double value = ProjectUtils.calculateTotalPVOfProject(project,
                    cursor);
            // System.out.println(value);
            map.put(cursor, value);
            cursor = DateUtils.addDays(cursor, 1);
        }
        return map;
    }

    /**
     * プロジェクトの開始日付から終了日付までのPV
     * 
     * @param project
     * @return
     */
    public static Map<Date, Double> calculateBACOfProject(Project project,
            Map<Date, EVMViewBean> actionsMap) {

        Map<Date, Double> map = new HashMap<Date, Double>();
        Date startDate = project.getProjectStartDate();
        Date endDate = project.getProjectEndDate();
        Date cursor = startDate;
        // ぐるぐる進んで、
        while (cursor.getTime() <= endDate.getTime()) {
            // System.out.println(cursor);
            // ココを書き換え
            double value = ProjectUtils.calculateTotalBACOfProject(project,
                    cursor, actionsMap);
            // System.out.println(value);
            map.put(cursor, value);
            cursor = DateUtils.addDays(cursor, 1);
        }
        return map;
    }

    /**
     * 指定した日付までのプロジェクトのトータルPV
     * 
     * @param project
     * @param baseDate
     */
    public static double calculateTotalPVOfProject(Project project,
            Date baseDate) {
        double totalPV = 0.0d;
        TaskInformation[] taskInformations = project.getTaskInformations();
        for (TaskInformation info : taskInformations) {
            double tmp = calculatePVs(info, baseDate);
            totalPV += (Double.isNaN(tmp) ? 0.0 : tmp);
        }
        return round(totalPV, 1);
    }

    /**
     * 渡されたプロジェクトについて、ある基準日時点のBACを返す。基準日より未来で、{@link EVMViewBean}
     * の中で一番近いオブジェクトのBACを返すようにする。基準日より未来のオブジェクトがない場合はプロジェクトのBACでよい
     * 
     * @param project
     * @param baseDate
     * @param actionsMap
     */
    public static double calculateTotalBACOfProject(Project project,
            Date baseDate, Map<Date, EVMViewBean> actionsMap) {
        String msg = "基準日 %s はプロジェクト最終日の値を使う %f\n";

        // ある基準日において、一番直近の未来のactionsMapの値を取る。
        Set<Date> keySet = actionsMap.keySet();
        Date ans = project.getProjectEndDate();// はじめは端っこから
        for (Date mapDate : keySet) {
            if (baseDate.getTime() < mapDate.getTime()) {
                ans = mapDate; // 自分(baseDate)より大きい実績値があったら採用。
                msg = "基準日 %s は直近基準日のプロジェクトの値を使う %f\n";
                break;
            }
            if (baseDate.getTime() == mapDate.getTime()) {
                ans = mapDate; // 自分(baseDate)より大きい実績値があったら採用。
                msg = "基準日 %s はその日基準日のプロジェクトの値を使う %f\n";
                break;
            }
        }
        EVMViewBean bean = null;
        if (actionsMap.containsKey(ans)) {
            bean = actionsMap.get(ans);
        } else {
            bean = ProjectUtils.createEVMViewBean(project, ans);
        }
        System.out.printf(msg, DateFormatUtils.format(baseDate, "yyyy/MM/dd"),
                bean.getBac());
        return bean.getBac();

    }

    /**
     * 基準日までの累積PVを返す。 タスクが始まっていなかったら0. なんらかの理由で、稼働予定日数=Nanとか0の場合Double.NaN
     * なんらかの理由で、予定工数がNaNの場合も、Double.NaN。
     * 
     * @param task
     * @param baseDate
     * @return
     */
    public static double calculatePVs(Task task, Date baseDate) {
        double numOfDays = task.getNumberOfDays();// 稼働予定の日数を取得 N
        double numOfManDays = task.getNumberOfManDays();// 予定工数を取得 M
        if (numOfDays != Double.NaN && numOfDays != 0.0d
                && numOfManDays != Double.NaN) {

            // 予定の工数Mを、稼働予定日数Nで割る(M/N)ことで、一日あたりの予定工数を出す。
            double numOfManDaysPerDay = numOfManDays / numOfDays;

            // System.out.printf("[%f]\n", numOfManDaysPerDay);

            // 現状過ぎた時間(基準日より左にあるプロットされたマスの個数)を計算。
            int passedDate = BaseDataUtils.split(baseDate,
                    task.getPlotDataMap())[0].size();

            double pvs = passedDate * numOfManDaysPerDay;
            // System.out.printf("PV: %f\n", pvs);

            return pvs;
        }
        return Double.NaN;

    }

    public static double calculatePVs(TaskInformation taskInfo, Date baseDate) {
        return calculatePVs(taskInfo.getTask(), baseDate);
    }

    /**
     * 基準日(その日のみの)のPVを計算する。その日に タスクがなければ0. なんらかの理由で、稼働予定日数=Nanとか0の場合Double.NaN
     * なんらかの理由で、予定工数がNaNの場合も、Double.NaN。
     * 
     * @param task
     * @param baseDate
     * @return
     */
    public static double calculatePV(Task task, Date baseDate) {
        double numOfDays = task.getNumberOfDays();// 稼働予定の日数を取得 N
        double numOfManDays = task.getNumberOfManDays();// 予定工数を取得 M
        if (numOfDays != Double.NaN && numOfDays != 0.0d
                && numOfManDays != Double.NaN) {

            // 予定の工数Mを、稼働予定日数Nで割る(M/N)ことで、一日あたりの予定工数を出す。
            double numOfManDaysPerDay = numOfManDays / numOfDays;
            // System.out.printf("[%f]\n", numOfManDaysPerDay);

            if (Utils.isBetween(baseDate, task.getScheduledStartDate(),
                    task.getScheduledEndDate(), true, true)) {
                // plotがnullじゃない場合は、存在するかをチェックする。
                if (task.getPlotDataMap() == null
                        || BaseDataUtils.existsPlot(baseDate,
                                task.getPlotDataMap())) {
                    numOfManDaysPerDay = round(numOfManDaysPerDay);
                    return numOfManDaysPerDay;
                }
            }
            return 0.0;
        }
        return Double.NaN;
    }

    /**
     * 基準日(その日のみの)のPVを計算する。その日に タスクがなければ0. なんらかの理由で、稼働予定日数=Nanとか0の場合Double.NaN
     * なんらかの理由で、予定工数がNaNの場合も、Double.NaN。
     * 
     * @param task
     * @param baseDate
     * @return
     */
    public static double calculatePV(TaskInformation taskInfo, Date baseDate) {
        return calculatePV(taskInfo.getTask(), baseDate);
    }

    /**
     * @param taskInfo
     * @param baseDate
     * @return
     */
    @Deprecated
    public static double calculateEVs(TaskInformation taskInfo, Date baseDate) {
        double numberOfDays = taskInfo.getTask().getNumberOfManDays();
        double progressRate = taskInfo.getEV().getProgressRate();
        double d = numberOfDays * progressRate;
        System.out.println(taskInfo.getTaskId() + ": EV(Java )；" + d);
        System.out.println(taskInfo.getTaskId() + ": EV(Excel)；"
                + taskInfo.getEV().getEarnedValue());
        return d;
    }

    // public Date[] containedDate(Date startDate, Date endDate, Date[] targets)
    // {
    // List<Date> returnList = new ArrayList<Date>();
    // for (int i = 0; i < targets.length; i++) {
    // Date target = targets[i];
    // if (isBetween(target, startDate, endDate)) {
    // returnList.add(target);
    // }
    // }
    // return returnList.toArray(new Date[returnList.size()]);
    // }

    public static List<PVBean> getCurrentPVList(Project project)
            throws ProjectException {
        List<PVBean> retList = new ArrayList<PVBean>();
        TaskInformation[] informations = project.getTaskInformations();
        Date targetDate = project.getBaseDate();
        for (TaskInformation taskInfo : informations) {
            // String pvForPrint = getPvForPrint(project, taskInfo);
            PVBean bean = getPVBean(taskInfo, targetDate);
            retList.add(bean);
        }
        return retList;
    }

    public static List<PVBean>[] getPVListArray(Project project,
            Date... targetDates) throws ProjectException {
        List<List<PVBean>> retList = new ArrayList<List<PVBean>>();
        for (Date targetDate : targetDates) {
            List<PVBean> instance = getPVListByDay(project, targetDate);
            retList.add(instance);
        }
        return (List<PVBean>[]) retList.toArray();
    }

    /**
     * 指定した日付その日のみのPVの配列を取得する。
     * 
     * @param project
     * @param targetDate
     * @return
     * @throws ProjectException
     */
    public static List<PVBean> getPVListByDay(Project project, Date targetDate)
            throws ProjectException {
        List<PVBean> retList = new ArrayList<PVBean>();
        TaskInformation[] informations = project.getTaskInformations();
        for (TaskInformation taskInfo : informations) {
            PVBean bean = getPVBean(taskInfo, targetDate);
            retList.add(bean);
        }
        return retList;
    }

    // 丸め誤差対応として、とりあえずRound処理を入れたが、本来ちゃんと対応すべきか。
    /**
     * 指定したタスク、日付の、その日のみのPVを算出。 その日にタスクがなければPVはゼロ。
     * 
     * @param taskInfo
     * @param targetDate
     * @return
     */
    public static PVBean getPVBean(TaskInformation taskInfo, Date targetDate) {
        Task task = taskInfo.getTask();
        PVBean bean = new PVBean();
        bean.setTaskId(task.getTaskId());
        bean.setId(task.getId());
        bean.setBaseDate(targetDate);
        bean.setPlannedValue(ProjectUtils.calculatePV(task, targetDate));
        return bean;
    }

    // 丸め誤差対応として、とりあえずRound処理を入れたが、本来ちゃんと対応すべきか。
    public static PVBean getPVBean(TaskInformation todayInfo,
            TaskInformation baseInfo) {
        PVTotalBean beanT = todayInfo.getPV();
        PVBean pvbean = PVTotalBean2PVBean.convert(beanT);
        if (baseInfo != null) {
            PVTotalBean beanB = baseInfo.getPV();

            // PVについては、第二項がNaNである可能性があるので、NaNでないときだけ減算
            double actualCost = beanT.getPlannedValue();
            if (!Double.isNaN(beanB.getPlannedValue())) {
                actualCost = beanT.getPlannedValue() - beanB.getPlannedValue();
                // とりあえず処理
                actualCost = round(actualCost);
                // とりあえず処理
            }
            pvbean.setPlannedValue(actualCost);
            return pvbean;
        }
        return pvbean;
    }

    // 丸め誤差対応として、とりあえずRound処理を入れたが、本来ちゃんと対応すべきか。
    public static ACBean getACBean(TaskInformation todayInfo,
            TaskInformation baseInfo) {
        ACTotalBean acTotalBeanT = todayInfo.getAC();
        ACBean acbean = ACTotalBean2ACBean.convert(acTotalBeanT);
        if (baseInfo != null) {
            ACTotalBean acTotalBeanB = baseInfo.getAC();

            // ACについては、第二項がNaNである可能性があるので、NaNでないときだけ減算
            double actualCost = acTotalBeanT.getActualCost();
            if (!Double.isNaN(acTotalBeanB.getActualCost())) {
                actualCost = acTotalBeanT.getActualCost()
                        - acTotalBeanB.getActualCost();
                // とりあえず処理
                actualCost = round(actualCost);
                // とりあえず処理
            }
            acbean.setActualCost(actualCost);
            return acbean;
        }
        return acbean;
    }

    // 丸め誤差対応として、とりあえずRound処理を入れたが、本来ちゃんと対応すべきか。
    public static EVBean getEVBean(TaskInformation todayInfo,
            TaskInformation baseInfo) {
        EVTotalBean evTotalBeanT = todayInfo.getEV();
        EVBean evbean = EVTotalBean2EVBean.convert(evTotalBeanT);
        if (baseInfo != null) {
            EVTotalBean evTotalBeanB = baseInfo.getEV();

            double earnedValue = evTotalBeanT.getEarnedValue();
            double earnedValue2 = evTotalBeanB.getEarnedValue();

            if (!Double.isNaN(earnedValue2)) {
                earnedValue = evTotalBeanT.getEarnedValue()
                        - evTotalBeanB.getEarnedValue();
                // とりあえず処理
                earnedValue = round(earnedValue);
                // とりあえず処理
            }
            evbean.setEarnedValue(earnedValue);

            // 進捗率については、第二項がNaNである可能性があるので、NaNでないときだけ減算
            // EVについては、前日は0.0であるためその考慮は不要。
            double progressRate = evTotalBeanT.getProgressRate();
            if (!Double.isNaN(evTotalBeanB.getProgressRate())) {
                progressRate = evTotalBeanT.getProgressRate()
                        - evTotalBeanB.getProgressRate();
                // とりあえず処理
                progressRate = round(progressRate);
                // とりあえず処理
            }
            evbean.setProgressRate(progressRate);
            return evbean;
        }
        return evbean;
    }

    /**
     * その基準日時点の{@link EVMViewBean} を返す。 ライブラリの都合上、icon名はセットしていない。
     * 
     * @param project
     * @param baseDate
     * @return
     */
    public static EVMViewBean createEVMViewBean(Project project, Date baseDate) {
        System.out.printf("EVMViewBean getCurrentPVACEV() 実際に作成開始\n");
        double pv = 0.0d;
        double ac = 0.0d;
        double ev = 0.0d;
        double bac = 0.0d;

        TaskInformation[] taskInformations = project.getTaskInformations();

        for (TaskInformation info : taskInformations) {
            double calculatePVs = ProjectUtils.calculatePVs(info, baseDate);
            pv += (Double.isNaN(calculatePVs) ? 0.0d : calculatePVs);
            ac += (Double.isNaN(info.getAC().getActualCost()) ? 0.0d : info
                    .getAC().getActualCost());
            ev += (Double.isNaN(info.getEV().getEarnedValue()) ? 0.0d : info
                    .getEV().getEarnedValue());
            double bacPerTask = info.getTask().getNumberOfManDays();
            // 予定開始日・終了日どちらかに値がない場合、このタスクはBAC(総工数)に計上しない考慮
            if (info.getTask().getScheduledStartDate() == null
                    || info.getTask().getScheduledEndDate() == null) {
                bacPerTask = Double.NaN;
            }
            bac += (Double.isNaN(bacPerTask) ? 0.0d : bacPerTask);
        }
        // System.out.println("------");

        EVMViewBean bean = new EVMViewBean();

        bean.setPlannedValue(round(pv, 2));
        bean.setActualCost(round(ac, 2));
        bean.setEarnedValue(round(ev, 2));
        bean.setBac(round(bac, 2));
        bean.setBaseDate(baseDate);

        double sv = Double.NaN;
        double cv = Double.NaN;
        double spi = Double.NaN;
        double cpi = Double.NaN;
        double etc = Double.NaN;
        double eac = Double.NaN;
        double vac = Double.NaN;

        if (isNonZeroNumeric(pv) && isNonZeroNumeric(ac)
                && isNonZeroNumeric(ev)) {
            sv = ev - pv;
            cv = ev - ac;
            spi = ev / pv;
            cpi = ev / ac;
            etc = (bac - ev) / cpi;
            eac = ac + etc;
            vac = bac - eac;
        }

        bean.setSv(round(sv, 2));
        bean.setCv(round(cv, 2));

        bean.setSpi(round(spi, 3));
        bean.setCpi(round(cpi, 3));

        bean.setEtc(round(etc, 2));
        bean.setEac(round(eac, 2));
        bean.setVac(round(vac, 2));
        // bean.setSpiIconFileName(PMUtils.choiceWeatherIconFileName(spi));
        // bean.setCpiIconFileName(PMUtils.choiceWeatherIconFileName(cpi));

        System.out.printf("EVMViewBean getCurrentPVACEV() 実際の作成完了\n");
        return bean;
    }

    // /**
    // * いま有効であるものにフィルタする
    // *
    // * @param original
    // * @return
    // */
    // public static List<PVBean> filter(List<PVBean> original) {
    // List<PVBean> returnList = new ArrayList<PVBean>();
    // for (PVBean bean : original) {
    // if (bean.isValid()) {
    // returnList.add(bean);
    // }
    // }
    // return returnList;
    // }
    //
    // public static List<ACBean> filterAC(List<ACBean> original) {
    // List<ACBean> returnList = new ArrayList<ACBean>();
    // for (ACBean bean : original) {
    // if (bean.isValid()) {
    // returnList.add(bean);
    // }
    // }
    // return returnList;
    // }
    //
    // public static List<EVBean> filterEV(List<EVBean> original) {
    // List<EVBean> returnList = new ArrayList<EVBean>();
    // for (EVBean bean : original) {
    // if (bean.isValid()) {
    // returnList.add(bean);
    // }
    // }
    // return returnList;
    // }

    // public static <T extends Validatable> List<T> filterList(List<T>
    // original) {
    // return filterList(original, new Object[0]);
    // }

    public static <T extends Validatable> List<T> filterList(List<T> original,
            Object... conditions) {
        List<T> returnList = new ArrayList<T>();
        for (T bean : original) {
            if (bean.isValid(conditions)) {
                returnList.add(bean);
            }
        }
        return returnList;
    }

    public static File findJSONFilePath(File input) {
        return new File(input.getParentFile(),
                findJSONFileName(input.getName()));
    }

    public static String findJSONFileName(String input) {
        return new String(new StringBuffer().append(input).append(".json"));
    }

    /**
     * そのプロジェクト上、土日祝日であるかどうかをチェックする
     * 
     * @param project
     * @param targetDate
     * @return
     */
    public static boolean isHoliday(Project project, Date targetDate) {
        Holiday[] holidays = project.getHolidays();
        if (holidays != null) {//
            for (Holiday holiday : holidays) {
                if (holiday.getDate().getTime() == targetDate.getTime()) {
                    return true;
                }
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(targetDate);
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
        case Calendar.SATURDAY:
        case Calendar.SUNDAY:
            return true;
        default:
            return false;
        }
    }

    public static Map<String, ExcelPOIScheduleBean> createExcelPOIScheduleBeanMap(
            Workbook workbook, Date baseDate) throws ProjectException {
        Map<String, ExcelPOIScheduleBean> poiMap = new HashMap<String, ExcelPOIScheduleBean>();
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
            ExcelPOIScheduleBean poiBean = createPOIBean(row);
            poiBean.setBaseDate(baseDate);
            poiMap.put(taskId, poiBean);

        }
        return poiMap;
    }

    public static Date createBaseDate(Workbook workbook, Sheet sheet) {
        Date baseDate;
        Name name = workbook.getName("雷線基準日");
        CellReference cellReference = new CellReference(
                name.getRefersToFormula());
        Cell baseDateCell = sheet.getRow(cellReference.getRow()).getCell(
                cellReference.getCol());
        baseDate = PoiUtils.getDate(baseDateCell);
        return baseDate;
    }

    public static Holiday[] createHolidays(Workbook workbook) {
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

    private static ExcelPOIScheduleBean createPOIBean(Row row)
            throws ProjectException {
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
        // Idが入っているのに、taskIdが空だったら、NG
        if (!StringUtils.isEmpty(bean.getId())
                && StringUtils.isEmpty(bean.getTaskId())) {
            String message = String.format(
                    "id: %s のタスクIDが未記載です。必須項目のためエラーとして処理を終了します。", bean.getId());
            throw new ProjectException(message);
        }
        return bean;
    }

    public static Date nextTradingDate(Date date, Project project) {
        Date resultDate = date;
        do {
            Date tmp = resultDate;
            resultDate = DateUtils.addDays(tmp, 1);
        } while (isHoliday(project, resultDate));
        return resultDate;
    }

    public static Date nextTradingDate(Project project) {
        return nextTradingDate(project.getBaseDate(), project);
    }

    private static Map<Date, Holiday> toMap(Holiday[] holidays) {
        Map<Date, Holiday> resultMap = new HashMap<Date, Holiday>();
        for (Holiday holiday : holidays) {
            resultMap.put(holiday.getDate(), holiday);
        }
        return resultMap;
    }

}
