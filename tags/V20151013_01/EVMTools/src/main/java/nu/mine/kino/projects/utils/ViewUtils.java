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
//作成日: 2014/10/30

package nu.mine.kino.projects.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nu.mine.kino.entity.ACBean;
import nu.mine.kino.entity.ACBean2PVACEVViewBean;
import nu.mine.kino.entity.EVBean;
import nu.mine.kino.entity.EVBean2PVACEVViewBean;
import nu.mine.kino.entity.PVACEVViewBean;
import nu.mine.kino.entity.PVBean;
import nu.mine.kino.entity.PVBean2PVACEVViewBean;
import nu.mine.kino.entity.PVViewBean;
import nu.mine.kino.entity.Project;
import nu.mine.kino.entity.Task;
import nu.mine.kino.entity.Task2PVACEVViewBean;
import nu.mine.kino.entity.Task2PVViewBean;
import nu.mine.kino.entity.TaskInformation;

import org.apache.commons.lang.time.DateUtils;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class ViewUtils {
    // 要注意タスクを探す上で大事なメソッド
    public static List<PVACEVViewBean> getPVACEVViewBeanList(Project project) {
        List<PVACEVViewBean> retList = new ArrayList<PVACEVViewBean>();
        TaskInformation[] todayTaskInfos = project.getTaskInformations();
        Date baseDate = project.getBaseDate();

        for (TaskInformation todayTaskInfo : todayTaskInfos) {
            PVACEVViewBean pvViewBean = getPVACEVViewBean(todayTaskInfo,
                    baseDate);
            retList.add(pvViewBean);
        }
        return retList;
    }

    public static PVACEVViewBean getPVACEVViewBean(
            TaskInformation todayTaskInfo, Date targetDate) {
        PVACEVViewBean bean = new PVACEVViewBean();

        Task task = todayTaskInfo.getTask();
        Task2PVACEVViewBean.convert(task, bean);

        PVBean pvBean = ProjectUtils.getPVBean(todayTaskInfo, targetDate);
        // ACBean acBean = ProjectUtils.getACBean(todayTaskInfo, baseTaskInfo);
        // EVBean evBean = ProjectUtils.getEVBean(todayTaskInfo, baseTaskInfo);

        PVBean2PVACEVViewBean.convert(pvBean, bean);
        // ACBean2PVACEVViewBean.convert(acBean, bean);
        // EVBean2PVACEVViewBean.convert(evBean, bean);
        bean.setProgressRate(Utils.round(todayTaskInfo.getEV()
                .getProgressRate()));

        PVBean pvBean_p1 = ProjectUtils.getPVBean(todayTaskInfo,
                DateUtils.addDays(targetDate, 1));
        bean.setPlannedValue_p1(pvBean_p1.getPlannedValue());
        // ココでチェックして、要注意タスクを表記すること
        // //////////////
        // スケジュール期限が基準日よりあとなのに、100%になっていないもの
        Date scheduledEndDate = bean.getScheduledEndDate();
        Date baseDate = bean.getBaseDate();
        if (scheduledEndDate != null) {
            // 予定日(scheEndDate)が、基準日(baseDate)より前(もしくは等しい)の場合
            boolean isDelay = scheduledEndDate.before(baseDate)
                    || scheduledEndDate.equals(baseDate);
            // 遅れていて、かつ完了していない場合。
            if (isDelay && bean.getProgressRate() != 1.0) {
                bean.setCheck(true);
            }
        }
        // //////////////
        return bean;
    }

    // 要注意タスクを探す上で大事なメソッド
    /**
     * 二点間のプロジェクトを比較して、差分のPV/AC/EVを計算する EVのうち進捗率は、直近時点の数値であり差分ではない。
     * 
     * @param project
     * @param base
     * @return
     */
    public static List<PVACEVViewBean> getPVACEVViewBeanList(Project project,
            Project base) {
        List<PVACEVViewBean> retList = new ArrayList<PVACEVViewBean>();
        TaskInformation[] todayTaskInfos = project.getTaskInformations();
        Date baseDate = project.getBaseDate();
        for (TaskInformation todayTaskInfo : todayTaskInfos) {
            TaskInformation baseTaskInfo = ProjectUtils.getTaskInformation(
                    base, todayTaskInfo.getTaskId());
            PVACEVViewBean pvViewBean = getPVACEVViewBean(todayTaskInfo,
                    baseTaskInfo, baseDate);
            retList.add(pvViewBean);
        }
        return retList;
    }

    public static PVACEVViewBean getPVACEVViewBean(
            TaskInformation todayTaskInfo, TaskInformation baseTaskInfo,
            Date targetDate) {
        PVACEVViewBean bean = new PVACEVViewBean();

        Task task = todayTaskInfo.getTask();
        Task2PVACEVViewBean.convert(task, bean);

        // PVBean pvBean = ProjectUtils.getPVBean(todayTaskInfo, targetDate);
        PVBean pvBean = ProjectUtils.getPVBean(todayTaskInfo, baseTaskInfo);
        ACBean acBean = ProjectUtils.getACBean(todayTaskInfo, baseTaskInfo);
        EVBean evBean = ProjectUtils.getEVBean(todayTaskInfo, baseTaskInfo);

        PVBean2PVACEVViewBean.convert(pvBean, bean);
        ACBean2PVACEVViewBean.convert(acBean, bean);
        EVBean2PVACEVViewBean.convert(evBean, bean);
        bean.setProgressRate(Utils.round(todayTaskInfo.getEV()
                .getProgressRate()));

        PVBean pvBean_p1 = ProjectUtils.getPVBean(todayTaskInfo,
                DateUtils.addDays(targetDate, 1));
        bean.setPlannedValue_p1(pvBean_p1.getPlannedValue());
        // ココでチェックして、要注意タスクを表記すること
        // //////////////
        // スケジュール期限が基準日よりあとなのに、100%になっていないもの
        Date scheduledEndDate = bean.getScheduledEndDate();
        Date baseDate = bean.getBaseDate();
        if (scheduledEndDate != null) {
            // 予定日(scheEndDate)が、基準日(baseDate)より前(もしくは等しい)の場合
            boolean isDelay = scheduledEndDate.before(baseDate)
                    || scheduledEndDate.equals(baseDate);
            // 遅れていて、かつ完了していない場合。
            if (isDelay && bean.getProgressRate() != 1.0) {
                bean.setCheck(true);
            }
        }
        // //////////////
        return bean;
    }

    public static List<PVACEVViewBean> getIsCheckPVACEVViewList(Project project) {
        List<PVACEVViewBean> isCheckList = new ArrayList<PVACEVViewBean>();
        List<PVACEVViewBean> list = ViewUtils.getPVACEVViewBeanList(project);
        for (PVACEVViewBean bean : list) {
            if (bean.isCheck()) {
                isCheckList.add(bean);
            }
        }
        return isCheckList;
    }

    public static List<PVViewBean> getPVViewBeanList(Project project)
            throws IllegalAccessException, InvocationTargetException {
        List<PVViewBean> retList = new ArrayList<PVViewBean>();
        TaskInformation[] informations = project.getTaskInformations();
        Date baseDate = project.getBaseDate();
        for (TaskInformation taskInfo : informations) {
            Date[] targetDates = new Date[7];
            targetDates[0] = DateUtils.addDays(baseDate, -3);
            targetDates[1] = DateUtils.addDays(baseDate, -2);
            targetDates[2] = DateUtils.addDays(baseDate, -1);
            targetDates[3] = baseDate;
            targetDates[4] = DateUtils.addDays(baseDate, 1);
            targetDates[5] = DateUtils.addDays(baseDate, 2);
            targetDates[6] = DateUtils.addDays(baseDate, 3);
            PVViewBean pvViewBean = getPVViewBean(project, taskInfo,
                    targetDates);
            retList.add(pvViewBean);
        }
        return retList;
    }

    public static PVViewBean getPVViewBean(Project project,
            TaskInformation taskInfo, Date... targetDates)
            throws IllegalAccessException, InvocationTargetException {

        PVViewBean bean = new PVViewBean();
        // StringBuffer buf = new StringBuffer();
        Task task = taskInfo.getTask();
        Task2PVViewBean.convert(task, bean);

        for (int index = 0; index < targetDates.length; index++) {
            Date targetDate = targetDates[index];
            double pv = ProjectUtils.calculatePV(task, targetDate);
            setPlannedValue(bean, pv, index);
        }

        // Date targetDate = null;
        // double pv = 0.0d;
        // targetDate = targetDates[3];
        // pv = ProjectUtils.calculatePV(task, targetDate);
        // if (!Double.isNaN(pv)) {
        // bean.setPlannedValue(pv);
        // }
        //
        // targetDate = targetDates[4];
        // pv = ProjectUtils.calculatePV(task, targetDate);
        // if (!Double.isNaN(pv)) {
        // bean.setPlannedValue_p1(pv);
        // }
        //
        // targetDate = targetDates[5];
        // pv = ProjectUtils.calculatePV(task, targetDate);
        // if (!Double.isNaN(pv)) {
        // bean.setPlannedValue_p2(pv);
        // }
        return bean;
    }

    private static void setPlannedValue(PVViewBean bean, double d, int index) {
        switch (index) {
        case 0:
            bean.setPlannedValue_m3(d);
            break;
        case 1:
            bean.setPlannedValue_m2(d);
            break;
        case 2:
            bean.setPlannedValue_m1(d);
            break;
        case 3:
            bean.setPlannedValue(d);
            break;
        case 4:
            bean.setPlannedValue_p1(d);
            break;
        case 5:
            bean.setPlannedValue_p2(d);
            break;
        case 6:
            bean.setPlannedValue_p3(d);
            break;
        default:
            break;
        }
    }

    private static double getPlannedValue(PVViewBean bean, int index) {
        double retValue = Double.NaN;
        switch (index) {
        case 0:
            retValue = bean.getPlannedValue_m3();
            break;
        case 1:
            retValue = bean.getPlannedValue_m2();
            break;
        case 2:
            retValue = bean.getPlannedValue_m1();
            break;
        case 3:
            retValue = bean.getPlannedValue();
            break;
        case 4:
            retValue = bean.getPlannedValue_p1();
            break;
        case 5:
            retValue = bean.getPlannedValue_p2();
            break;
        case 6:
            retValue = bean.getPlannedValue_p3();
            break;
        default:
            break;
        }
        return retValue;
    }

    //
    // /**
    // * PV/AC/EV/進捗率すべてが、NaNか 0 なら false そうでなければ(つまり、なんらかの情報があれば) true
    // *
    // * @param bean
    // * @return
    // */
    // public static boolean isValid(PVACEVViewBean bean) {
    // double pv = bean.getPlannedValue();
    // double ac = bean.getActualCost();
    // double ev = bean.getEarnedValue();
    // double progressRate = bean.getProgressRate();
    // double pv_p1 = bean.getPlannedValue_p1();
    //
    // if (Utils.isNonZeroNumeric(pv) || Utils.isNonZeroNumeric(ac)
    // || Utils.isNonZeroNumeric(ev)
    // || Utils.isNonZeroNumeric(progressRate)
    // || Utils.isNonZeroNumeric(pv_p1)) {
    // return true;
    // }
    // return false;
    // }

    public static PVACEVViewBean findPVACEVViewBeanByTaskId(
            PVACEVViewBean[] pvacevViews, String taskId) {
        for (PVACEVViewBean bean : pvacevViews) {
            if (bean.getTaskId().equals(taskId)) {
                return bean;
            }
        }
        return null;
    }

    // /**
    // * いま有効であるものにフィルタする
    // *
    // * @param original
    // * @return
    // */
    // public static List<PVViewBean> filterPVViewBean(List<PVViewBean>
    // original) {
    // List<PVViewBean> returnList = new ArrayList<PVViewBean>();
    // for (PVViewBean bean : original) {
    // if (bean.isValid()) {
    // returnList.add(bean);
    // }
    // }
    // return returnList;
    // }
    //
    // public static List<PVACEVViewBean> filterPVACEVViewBean(
    // List<PVACEVViewBean> original) {
    // List<PVACEVViewBean> retList = new ArrayList<PVACEVViewBean>();
    //
    // for (PVACEVViewBean bean : original) {
    // if (bean.isValid()) {
    // retList.add(bean);
    // }
    // }
    // return retList;
    // }
    //
    // public static List<ACViewBean> filterACViewBean(List<ACViewBean>
    // original) {
    // List<ACViewBean> retList = new ArrayList<ACViewBean>();
    // for (ACViewBean bean : original) {
    // if (bean.isValid()) {
    // retList.add(bean);
    // }
    // }
    // return retList;
    // }
    //
    // public static List<EVViewBean> filterEVViewBean(List<EVViewBean>
    // original) {
    // List<EVViewBean> retList = new ArrayList<EVViewBean>();
    // for (EVViewBean bean : original) {
    // if (bean.isValid()) {
    // retList.add(bean);
    // }
    // }
    // return retList;
    // }

}
