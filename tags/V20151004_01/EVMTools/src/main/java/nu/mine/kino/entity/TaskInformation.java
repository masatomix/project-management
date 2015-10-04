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
public class TaskInformation {

    /**
     * id
     */
    private String id;

    /**
     * ﾀｽｸID
     */
    private String taskId;

    /**
     * タスク
     */
    private nu.mine.kino.entity.Task task;

    /**
     * PV
     */
    private nu.mine.kino.entity.PVTotalBean PV;

    /**
     * EV
     */
    private nu.mine.kino.entity.EVTotalBean EV;

    /**
     * AC
     */
    private nu.mine.kino.entity.ACTotalBean AC;

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
     * タスクをセットする。
     * 
     * @param task
     *            タスク
     */
    public void setTask(nu.mine.kino.entity.Task task) {
        this.task = task;
    }

    /**
     * PVをセットする。
     * 
     * @param PV
     *            PV
     */
    public void setPV(nu.mine.kino.entity.PVTotalBean PV) {
        this.PV = PV;
    }

    /**
     * EVをセットする。
     * 
     * @param EV
     *            EV
     */
    public void setEV(nu.mine.kino.entity.EVTotalBean EV) {
        this.EV = EV;
    }

    /**
     * ACをセットする。
     * 
     * @param AC
     *            AC
     */
    public void setAC(nu.mine.kino.entity.ACTotalBean AC) {
        this.AC = AC;
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
     * タスクを取得する。
     * 
     * @return タスク
     */
    public nu.mine.kino.entity.Task getTask() {
        return task;
    }

    /**
     * PVを取得する。
     * 
     * @return PV
     */
    public nu.mine.kino.entity.PVTotalBean getPV() {
        return PV;
    }

    /**
     * EVを取得する。
     * 
     * @return EV
     */
    public nu.mine.kino.entity.EVTotalBean getEV() {
        return EV;
    }

    /**
     * ACを取得する。
     * 
     * @return AC
     */
    public nu.mine.kino.entity.ACTotalBean getAC() {
        return AC;
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
                .append("ﾀｽｸID", taskId).append("タスク", task).append("PV", PV)
                .append("EV", EV).append("AC", AC).append("基準日", baseDate)
                .toString();
    }
}