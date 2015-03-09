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

import static nu.mine.kino.projects.utils.Utils.round;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nu.mine.kino.entity.ACBean;
import nu.mine.kino.entity.ACTotalBean;
import nu.mine.kino.entity.ACTotalBean2ACBean;
import nu.mine.kino.entity.EVBean;
import nu.mine.kino.entity.EVTotalBean;
import nu.mine.kino.entity.EVTotalBean2EVBean;
import nu.mine.kino.entity.PVBean;
import nu.mine.kino.entity.PVTotalBean;
import nu.mine.kino.entity.PVTotalBean2PVBean;
import nu.mine.kino.entity.Project;
import nu.mine.kino.entity.Task;
import nu.mine.kino.entity.TaskInformation;
import nu.mine.kino.entity.Validatable;
import nu.mine.kino.projects.ProjectException;

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
}
