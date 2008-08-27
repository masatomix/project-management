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
// 作成日: 2008/08/15
package nu.mine.kino.plugin.beangenerator.sheetdata.anno;

import net.java.amateras.xlsbeans.annotation.Column;
import nu.mine.kino.plugin.beangenerator.sheetdata.IFieldInformation;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class FieldInformationWithAnno implements IFieldInformation {

    private String fieldNameJ;

    private String fieldName;

    private String fieldType;

    private String description;

    private String annotation;

    private String annotationName;

    private String merge;

    private String customAnnotation;

    @Column(columnName = "フィールド名日本語")//$NON-NLS-1$
    public void setFieldNameJ(String fieldNameJ) {
        this.fieldNameJ = fieldNameJ;
    }

    @Column(columnName = "フィールド名")//$NON-NLS-1$
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Column(columnName = "型")//$NON-NLS-1$
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    @Column(columnName = "説明")//$NON-NLS-1$
    public void setDescription(String description) {
        this.description = description;
    }

    @Column(columnName = "Annotation")//$NON-NLS-1$
    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    @Column(columnName = "Annotation名")//$NON-NLS-1$
    public void setAnnotationName(String annotationName) {
        this.annotationName = annotationName;
    }

    @Column(columnName = "マージ")//$NON-NLS-1$
    public void setMerge(String merge) {
        this.merge = merge;
    }

    @Column(columnName = "独自のアノテーション")//$NON-NLS-1$
    public void setCustomAnnotation(String customAnnotation) {
        this.customAnnotation = customAnnotation;
    }

    public String getFieldNameJ() {
        return fieldNameJ;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("フィールド名日本語", fieldNameJ) //$NON-NLS-1$
                .append("説明", description).append("フィールド名", fieldName).append( //$NON-NLS-1$ //$NON-NLS-2$
                        "型", fieldType).toString(); //$NON-NLS-1$
    }

    public String getAnnotation() {
        return annotation;
    }

    public String getAnnotationName() {
        return annotationName;
    }

    public String getMerge() {
        return merge;
    }

    public String getCustomAnnotation() {
        return customAnnotation;
    }

}
