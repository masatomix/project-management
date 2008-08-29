/******************************************************************************
 * Copyright (c) 2008 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/

package nu.mine.kino.plugin.beangenerator.sheetdata;

import java.util.List;

import net.java.amateras.xlsbeans.annotation.HorizontalRecords;
import net.java.amateras.xlsbeans.annotation.LabelledCell;
import net.java.amateras.xlsbeans.annotation.LabelledCellType;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * クラス情報を格納するクラスです
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 * @version $Revision$
 */
public class ClassInformation implements IClassInformation {

    /**
     * フィールド情報のリストです
     */
    private List<IFieldInformation> fieldInformations;

    /**
     * 日本語のクラス名を格納するフィールドです。
     */
    private String classNameJ;

    /**
     * 説明のフィールドです
     */
    private String description;

    /**
     * パッケージ名のフィールドです
     */
    private String packageName;

    /**
     * 実際のクラス名です。
     */
    private String className;

    /**
     * toStringを出力するか、を格納するフィールドです
     */
    private String toString;

    /**
     * extendsやimplementsなどの情報を格納するフィールドです
     */
    private String addedInfo;

    /**
     * フィールド情報のリストをセットする。
     * 
     * @param fieldInformations
     *            フィールド情報のリスト
     */
    @HorizontalRecords(tableLabel = "クラス情報", recordClass = FieldInformation.class)//$NON-NLS-1$	
    public void setFieldInformations(List<IFieldInformation> fieldInformations) {
        this.fieldInformations = fieldInformations;
    }

    /**
     * クラス名日本語フィールドをセットする。
     * 
     * @param classNameJ
     *            クラス名日本語フィールド
     */
    @LabelledCell(label = "クラス名日本語", type = LabelledCellType.Right)//$NON-NLS-1$	
    public void setClassNameJ(String classNameJ) {
        this.classNameJ = classNameJ;
    }

    /**
     * 説明フィールドをセットする。
     * 
     * @param description
     *            説明フィールド
     */
    @LabelledCell(label = "説明", type = LabelledCellType.Right)//$NON-NLS-1$	
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * パッケージ名フィールドをセットする。
     * 
     * @param packageName
     *            パッケージ名フィールド
     */
    @LabelledCell(label = "パッケージ名", type = LabelledCellType.Right)//$NON-NLS-1$	
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * クラス名フィールドをセットする。
     * 
     * @param className
     *            クラス名フィールド
     */
    @LabelledCell(label = "クラス名", type = LabelledCellType.Right)//$NON-NLS-1$	
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * toStringフィールドをセットする。
     * 
     * @param toString
     *            toStringフィールド
     */
    @LabelledCell(label = "toString", type = LabelledCellType.Right)//$NON-NLS-1$	
    public void setToString(String toString) {
        this.toString = toString;
    }

    /**
     * 付加情報フィールドをセットする。
     * 
     * @param addedInfo
     *            付加情報フィールド
     */
    @LabelledCell(label = "付加情報", type = LabelledCellType.Right)//$NON-NLS-1$	
    public void setAddedInfo(String addedInfo) {
        this.addedInfo = addedInfo;
    }

    /**
     * フィールド情報のリストを取得する。
     * 
     * @return フィールド情報のリスト
     */
    public List<IFieldInformation> getFieldInformations() {
        return fieldInformations;
    }

    /**
     * クラス名日本語フィールドを取得する。
     * 
     * @return クラス名日本語フィールド
     */
    public String getClassNameJ() {
        return classNameJ;
    }

    /**
     * 説明フィールドを取得する。
     * 
     * @return 説明フィールド
     */
    public String getDescription() {
        return description;
    }

    /**
     * パッケージ名フィールドを取得する。
     * 
     * @return パッケージ名フィールド
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * クラス名フィールドを取得する。
     * 
     * @return クラス名フィールド
     */
    public String getClassName() {
        return className;
    }

    /**
     * toStringフィールドを取得する。
     * 
     * @return toStringフィールド
     */
    public String getToString() {
        return toString;
    }

    /**
     * 付加情報フィールドを取得する。
     * 
     * @return 付加情報フィールド
     */
    public String getAddedInfo() {
        return addedInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("フィールド情報のリスト",
                fieldInformations).append("クラス名日本語フィールド", classNameJ).append(
                "説明フィールド", description).append("パッケージ名フィールド", packageName)
                .append("クラス名フィールド", className).append("toStringフィールド",
                        toString).append("付加情報フィールド", addedInfo).toString();
    }
}