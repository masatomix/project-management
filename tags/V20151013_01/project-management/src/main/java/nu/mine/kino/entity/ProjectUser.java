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
public class ProjectUser {

    /**
     * id
     */
    private String id;

    /**
     * あなたの名前
     */
    private String fullName;

    /**
     * 説明
     */
    private String description;

    /**
     * E-mail
     */
    private String emailAddress;

    /**
     * URL
     */
    private String url;

    /**
     * 集計されたPlanned Value
     */
    private double plannedValue;

    /**
     * 集計されたActual Cost
     */
    private double actualCost;

    /**
     * 集計されたEarned Value
     */
    private double earnedValue;

    /**
     * 集計されたPlanned Value
     */
    private double plannedValue_p1;

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
     * あなたの名前をセットする。
     * 
     * @param fullName
     *            あなたの名前
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * 説明をセットする。
     * 
     * @param description
     *            説明
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * E-mailをセットする。
     * 
     * @param emailAddress
     *            E-mail
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * URLをセットする。
     * 
     * @param url
     *            URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Planned Valueをセットする。
     * 
     * @param plannedValue
     *            Planned Value
     */
    public void setPlannedValue(double plannedValue) {
        this.plannedValue = plannedValue;
    }

    /**
     * Actual Costをセットする。
     * 
     * @param actualCost
     *            Actual Cost
     */
    public void setActualCost(double actualCost) {
        this.actualCost = actualCost;
    }

    /**
     * Earned Valueをセットする。
     * 
     * @param earnedValue
     *            Earned Value
     */
    public void setEarnedValue(double earnedValue) {
        this.earnedValue = earnedValue;
    }

    /**
     * Planned Valueをセットする。
     * 
     * @param plannedValue_p1
     *            Planned Value
     */
    public void setPlannedValue_p1(double plannedValue_p1) {
        this.plannedValue_p1 = plannedValue_p1;
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
     * あなたの名前を取得する。
     * 
     * @return あなたの名前
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * 説明を取得する。
     * 
     * @return 説明
     */
    public String getDescription() {
        return description;
    }

    /**
     * E-mailを取得する。
     * 
     * @return E-mail
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * URLを取得する。
     * 
     * @return URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Planned Valueを取得する。
     * 
     * @return Planned Value
     */
    public double getPlannedValue() {
        return plannedValue;
    }

    /**
     * Actual Costを取得する。
     * 
     * @return Actual Cost
     */
    public double getActualCost() {
        return actualCost;
    }

    /**
     * Earned Valueを取得する。
     * 
     * @return Earned Value
     */
    public double getEarnedValue() {
        return earnedValue;
    }

    /**
     * Planned Valueを取得する。
     * 
     * @return Planned Value
     */
    public double getPlannedValue_p1() {
        return plannedValue_p1;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id)
                .append("あなたの名前", fullName).append("説明", description)
                .append("E-mail", emailAddress).append("URL", url)
                .append("Planned Value", plannedValue)
                .append("Actual Cost", actualCost)
                .append("Earned Value", earnedValue)
                .append("Planned Value", plannedValue_p1).toString();
    }
}