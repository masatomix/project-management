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
 * 画面で使用するEVM情報
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 */
public class BaseEVMViewBean {

    /**
     * Planned Value
     */
    private double plannedValue;

    /**
     * Actual Cost
     */
    private double actualCost;

    /**
     * 進捗率
     */
    private double progressRate;

    /**
     * Earned Value
     */
    private double earnedValue;

    /**
     * 完了時工数
     */
    private double bac;

    /**
     * スケジュールの差異 (EV-PV)
     */
    private double sv;

    /**
     * コストの差異 (EV-AC)
     */
    private double cv;

    /**
     * スケジュール効率指数 (EV/PV)
     */
    private double spi;

    /**
     * コスト効率指数 (EV/AC)
     */
    private double cpi;

    /**
     * 残作業コスト予測 （BAC－EV）/CPI
     */
    private double etc;

    /**
     * 完了時コスト予測 (AC＋ETC)
     */
    private double eac;

    /**
     * 完了時コスト差異 (BAC-EAC)
     */
    private double vac;

    /**
     * 基準日
     */
    private java.util.Date baseDate;

    /**
     * SPIの状況を表すアイコンのパス
     */
    private String spiIconFileName;

    /**
     * CPIの状況を表すアイコンのパス
     */
    private String cpiIconFileName;

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
     * 進捗率をセットする。
     * 
     * @param progressRate
     *            進捗率
     */
    public void setProgressRate(double progressRate) {
        this.progressRate = progressRate;
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
     * BAC (Budget at Completion)をセットする。
     * 
     * @param bac
     *            BAC (Budget at Completion)
     */
    public void setBac(double bac) {
        this.bac = bac;
    }

    /**
     * SV (Scheduled Variance)をセットする。
     * 
     * @param sv
     *            SV (Scheduled Variance)
     */
    public void setSv(double sv) {
        this.sv = sv;
    }

    /**
     * CV (Cost Variance)をセットする。
     * 
     * @param cv
     *            CV (Cost Variance)
     */
    public void setCv(double cv) {
        this.cv = cv;
    }

    /**
     * SPI (Schedule Performance Index)をセットする。
     * 
     * @param spi
     *            SPI (Schedule Performance Index)
     */
    public void setSpi(double spi) {
        this.spi = spi;
    }

    /**
     * CPI (Cost Performance Index)をセットする。
     * 
     * @param cpi
     *            CPI (Cost Performance Index)
     */
    public void setCpi(double cpi) {
        this.cpi = cpi;
    }

    /**
     * ETC(Estimate To Completion)をセットする。
     * 
     * @param etc
     *            ETC(Estimate To Completion)
     */
    public void setEtc(double etc) {
        this.etc = etc;
    }

    /**
     * EAC(Estimate At Completion)をセットする。
     * 
     * @param eac
     *            EAC(Estimate At Completion)
     */
    public void setEac(double eac) {
        this.eac = eac;
    }

    /**
     * VAC(Variance At Completion)をセットする。
     * 
     * @param vac
     *            VAC(Variance At Completion)
     */
    public void setVac(double vac) {
        this.vac = vac;
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
     * SPI Icon File Nameをセットする。
     * 
     * @param spiIconFileName
     *            SPI Icon File Name
     */
    public void setSpiIconFileName(String spiIconFileName) {
        this.spiIconFileName = spiIconFileName;
    }

    /**
     * CPI Icon File Nameをセットする。
     * 
     * @param cpiIconFileName
     *            CPI Icon File Name
     */
    public void setCpiIconFileName(String cpiIconFileName) {
        this.cpiIconFileName = cpiIconFileName;
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
     * 進捗率を取得する。
     * 
     * @return 進捗率
     */
    public double getProgressRate() {
        return progressRate;
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
     * BAC (Budget at Completion)を取得する。
     * 
     * @return BAC (Budget at Completion)
     */
    public double getBac() {
        return bac;
    }

    /**
     * SV (Scheduled Variance)を取得する。
     * 
     * @return SV (Scheduled Variance)
     */
    public double getSv() {
        return sv;
    }

    /**
     * CV (Cost Variance)を取得する。
     * 
     * @return CV (Cost Variance)
     */
    public double getCv() {
        return cv;
    }

    /**
     * SPI (Schedule Performance Index)を取得する。
     * 
     * @return SPI (Schedule Performance Index)
     */
    public double getSpi() {
        return spi;
    }

    /**
     * CPI (Cost Performance Index)を取得する。
     * 
     * @return CPI (Cost Performance Index)
     */
    public double getCpi() {
        return cpi;
    }

    /**
     * ETC(Estimate To Completion)を取得する。
     * 
     * @return ETC(Estimate To Completion)
     */
    public double getEtc() {
        return etc;
    }

    /**
     * EAC(Estimate At Completion)を取得する。
     * 
     * @return EAC(Estimate At Completion)
     */
    public double getEac() {
        return eac;
    }

    /**
     * VAC(Variance At Completion)を取得する。
     * 
     * @return VAC(Variance At Completion)
     */
    public double getVac() {
        return vac;
    }

    /**
     * 基準日を取得する。
     * 
     * @return 基準日
     */
    public java.util.Date getBaseDate() {
        return baseDate;
    }

    /**
     * SPI Icon File Nameを取得する。
     * 
     * @return SPI Icon File Name
     */
    public String getSpiIconFileName() {
        return spiIconFileName;
    }

    /**
     * CPI Icon File Nameを取得する。
     * 
     * @return CPI Icon File Name
     */
    public String getCpiIconFileName() {
        return cpiIconFileName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("Planned Value", plannedValue)
                .append("Actual Cost", actualCost).append("進捗率", progressRate)
                .append("Earned Value", earnedValue)
                .append("BAC (Budget at Completion)", bac)
                .append("SV (Scheduled Variance)", sv)
                .append("CV (Cost Variance)", cv)
                .append("SPI (Schedule Performance Index)", spi)
                .append("CPI (Cost Performance Index)", cpi)
                .append("ETC(Estimate To Completion)", etc)
                .append("EAC(Estimate At Completion)", eac)
                .append("VAC(Variance At Completion)", vac)
                .append("基準日", baseDate)
                .append("SPI Icon File Name", spiIconFileName)
                .append("CPI Icon File Name", cpiIconFileName).toString();
    }
}