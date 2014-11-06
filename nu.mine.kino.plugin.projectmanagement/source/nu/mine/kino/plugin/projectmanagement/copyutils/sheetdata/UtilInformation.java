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

package nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata;

import net.java.amateras.xlsbeans.annotation.HorizontalRecords;
import net.java.amateras.xlsbeans.annotation.LabelledCell;
import net.java.amateras.xlsbeans.annotation.LabelledCellType;

/**
 * JavaBeans Generatorの為のサンプルJavaBeansです。
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 * @version $Revision$
 */
public class UtilInformation implements java.io.Serializable, IUtilInformation  {

    private String packageName;

    private String sourceIFName;

    private String destIFName;

    private String destClassName;

    private String className;

    private String methodName;

    private String useBeanUtils;

    private String methods;

    private java.util.List<IDetailInformation> Details;

    /* (non-Javadoc)
     * @see nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IUtilInformation#setPackageName(java.lang.String)
     */
    @LabelledCell(label = "パッケージ名", type = LabelledCellType.Right)//$NON-NLS-1$    	
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /* (non-Javadoc)
     * @see nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IUtilInformation#setSourceIFName(java.lang.String)
     */
    @LabelledCell(label = "コピー元インタフェース名", type = LabelledCellType.Right)//$NON-NLS-1$    	
    public void setSourceIFName(String sourceIFName) {
        this.sourceIFName = sourceIFName;
    }

    /* (non-Javadoc)
     * @see nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IUtilInformation#setDestIFName(java.lang.String)
     */
    @LabelledCell(label = "コピー先インタフェース名", type = LabelledCellType.Right)//$NON-NLS-1$    	
    public void setDestIFName(String destIFName) {
        this.destIFName = destIFName;
    }

    /* (non-Javadoc)
     * @see nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IUtilInformation#setDestClassName(java.lang.String)
     */
    @LabelledCell(label = "コピー先クラス名", type = LabelledCellType.Right)//$NON-NLS-1$    	
    public void setDestClassName(String destClassName) {
        this.destClassName = destClassName;
    }

    /* (non-Javadoc)
     * @see nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IUtilInformation#setClassName(java.lang.String)
     */
    @LabelledCell(label = "クラス名", type = LabelledCellType.Right)//$NON-NLS-1$    	
    public void setClassName(String className) {
        this.className = className;
    }

    /* (non-Javadoc)
     * @see nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IUtilInformation#setMethodName(java.lang.String)
     */
    @LabelledCell(label = "メソッド名", type = LabelledCellType.Right)//$NON-NLS-1$    	
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /* (non-Javadoc)
     * @see nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IUtilInformation#setUseBeanUtils(java.lang.String)
     */
    @LabelledCell(label = "BeanUtilsも使用する", type = LabelledCellType.Right)//$NON-NLS-1$    	
    public void setUseBeanUtils(String useBeanUtils) {
        this.useBeanUtils = useBeanUtils;
    }

    /* (non-Javadoc)
     * @see nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IUtilInformation#setMethods(java.lang.String)
     */
    @LabelledCell(label = "追加メソッド", type = LabelledCellType.Right)//$NON-NLS-1$    	
    public void setMethods(String methods) {
        this.methods = methods;
    }

    /* (non-Javadoc)
     * @see nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IUtilInformation#setDetails(java.util.List)
     */
    @HorizontalRecords(tableLabel = "載せ替えユーティリティ情報", recordClass = DetailInformation.class)//$NON-NLS-1$	
    public void setDetails(java.util.List<IDetailInformation> Details) {
        this.Details = Details;
    }

    /* (non-Javadoc)
     * @see nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IUtilInformation#getPackageName()
     */
    public String getPackageName() {
        return packageName;
    }

    /* (non-Javadoc)
     * @see nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IUtilInformation#getSourceIFName()
     */
    public String getSourceIFName() {
        return sourceIFName;
    }

    /* (non-Javadoc)
     * @see nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IUtilInformation#getDestIFName()
     */
    public String getDestIFName() {
        return destIFName;
    }

    /* (non-Javadoc)
     * @see nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IUtilInformation#getDestClassName()
     */
    public String getDestClassName() {
        return destClassName;
    }

    /* (non-Javadoc)
     * @see nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IUtilInformation#getClassName()
     */
    public String getClassName() {
        return className;
    }

    /* (non-Javadoc)
     * @see nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IUtilInformation#getMethodName()
     */
    public String getMethodName() {
        return methodName;
    }

    /* (non-Javadoc)
     * @see nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IUtilInformation#getUseBeanUtils()
     */
    public String getUseBeanUtils() {
        return useBeanUtils;
    }

    /* (non-Javadoc)
     * @see nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IUtilInformation#getMethods()
     */
    public String getMethods() {
        return methods;
    }

    /* (non-Javadoc)
     * @see nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IUtilInformation#getDetails()
     */
    public java.util.List<IDetailInformation> getDetails() {
        return Details;
    }
}