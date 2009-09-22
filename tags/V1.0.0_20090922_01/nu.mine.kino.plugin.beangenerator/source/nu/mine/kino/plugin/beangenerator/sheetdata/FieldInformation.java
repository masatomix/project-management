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

package nu.mine.kino.plugin.beangenerator.sheetdata;

import net.java.amateras.xlsbeans.annotation.Column;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * フィールド情報を格納するクラスです
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 * @version $Revision$
 */
public class FieldInformation implements IFieldInformation {

    /**
     * フィールド情報のリストです
     */
    private String fieldNameJ;

    /**
     * 日本語のクラス名を格納するフィールドです。
     */
    private String fieldName;

    /**
     * 説明のフィールドです
     */
    private String fieldType;

    /**
     * 説明のフィールドです
     */
    private String description;

    /**
     * フィールド名日本語をセットする。
     * 
     * @param fieldNameJ
     *            フィールド名日本語
     */
    @Column(columnName = "フィールド名日本語")//$NON-NLS-1$
    public void setFieldNameJ(String fieldNameJ) {
        this.fieldNameJ = fieldNameJ;
    }

    /**
     * フィールド名をセットする。
     * 
     * @param fieldName
     *            フィールド名
     */
    @Column(columnName = "フィールド名")//$NON-NLS-1$
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * 型をセットする。
     * 
     * @param fieldType
     *            型
     */
    @Column(columnName = "型")//$NON-NLS-1$
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    /**
     * 説明をセットする。
     * 
     * @param description
     *            説明
     */
    @Column(columnName = "説明")//$NON-NLS-1$
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * フィールド名日本語を取得する。
     * 
     * @return フィールド名日本語
     */
    public String getFieldNameJ() {
        return fieldNameJ;
    }

    /**
     * フィールド名を取得する。
     * 
     * @return フィールド名
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * 型を取得する。
     * 
     * @return 型
     */
    public String getFieldType() {
        return fieldType;
    }

    /**
     * 説明を取得する。
     * 
     * @return 説明
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("フィールド名日本語", fieldNameJ)
                .append("フィールド名", fieldName).append("型", fieldType).append(
                        "説明", description).toString();
    }
}