/******************************************************************************
 * Copyright (c) 2012 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//作成日: 2014/09/11

package nu.mine.kino.plugin.beangenerator.copyutils.sheetdata;

import net.java.amateras.xlsbeans.annotation.HorizontalRecords;
import net.java.amateras.xlsbeans.annotation.LabelledCell;
import net.java.amateras.xlsbeans.annotation.LabelledCellType;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public interface IUtilInformation {

    /**
     * 
     * @param packageName
     */
    @LabelledCell(label = "パッケージ名", type = LabelledCellType.Right)//$NON-NLS-1$    	
    public void setPackageName(String packageName);

    /**
     * 
     * @param sourceIFName
     */
    @LabelledCell(label = "コピー元インタフェース名", type = LabelledCellType.Right)//$NON-NLS-1$    	
    public void setSourceIFName(String sourceIFName);

    /**
     * 
     * @param destIFName
     */
    @LabelledCell(label = "コピー先インタフェース名", type = LabelledCellType.Right)//$NON-NLS-1$    	
    public void setDestIFName(String destIFName);

    /**
     * 
     * @param destClassName
     */
    @LabelledCell(label = "コピー先クラス名", type = LabelledCellType.Right)//$NON-NLS-1$    	
    public void setDestClassName(String destClassName);

    /**
     * 
     * @param className
     */
    @LabelledCell(label = "クラス名", type = LabelledCellType.Right)//$NON-NLS-1$    	
    public void setClassName(String className);

    /**
     * 
     * @param methodName
     */
    @LabelledCell(label = "メソッド名", type = LabelledCellType.Right)//$NON-NLS-1$    	
    public void setMethodName(String methodName);

    /**
     * 
     * @param useBeanUtils
     */
    @LabelledCell(label = "BeanUtilsも使用する", type = LabelledCellType.Right)//$NON-NLS-1$    	
    public void setUseBeanUtils(String useBeanUtils);

    /**
     * 
     * @param methods
     */
    @LabelledCell(label = "追加メソッド", type = LabelledCellType.Right)//$NON-NLS-1$    	
    public void setMethods(String methods);

    /**
     * 
     * @param Details
     */
    @HorizontalRecords(tableLabel = "載せ替えユーティリティ情報", recordClass = DetailInformation.class)//$NON-NLS-1$	
    public void setDetails(java.util.List<IDetailInformation> Details);

    public String getPackageName();

    public String getSourceIFName();

    public String getDestIFName();

    public String getDestClassName();

    public String getClassName();

    public String getMethodName();

    public String getUseBeanUtils();

    public String getMethods();

    public java.util.List<IDetailInformation> getDetails();

}