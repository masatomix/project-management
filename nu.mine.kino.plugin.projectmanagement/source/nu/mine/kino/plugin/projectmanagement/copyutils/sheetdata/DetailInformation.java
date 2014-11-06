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

import net.java.amateras.xlsbeans.annotation.Column;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * JavaBeans Generatorの為のサンプルJavaBeansです。
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 * @version $Revision$
 */
public class DetailInformation implements java.io.Serializable,
        IDetailInformation  {

    private String destFieldName;

    private String sourceFieldName;

    private String logic;

    /* (non-Javadoc)
     * @see nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IDetailInformation#setDestFieldName(java.lang.String)
     */
    @Column(columnName = "コピー先フィールド名")//$NON-NLS-1$	
    public void setDestFieldName(String destFieldName) {
        this.destFieldName = destFieldName;
    }

    /* (non-Javadoc)
     * @see nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IDetailInformation#setSourceFieldName(java.lang.String)
     */
    @Column(columnName = "コピー元フィールド名")//$NON-NLS-1$	
    public void setSourceFieldName(String sourceFieldName) {
        this.sourceFieldName = sourceFieldName;
    }

    /* (non-Javadoc)
     * @see nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IDetailInformation#setLogic(java.lang.String)
     */
    @Column(columnName = "ロジックの場合")//$NON-NLS-1$	
    public void setLogic(String logic) {
        this.logic = logic;
    }

    /* (non-Javadoc)
     * @see nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IDetailInformation#getDestFieldName()
     */
    public String getDestFieldName() {
        return destFieldName;
    }

    /* (non-Javadoc)
     * @see nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IDetailInformation#getSourceFieldName()
     */
    public String getSourceFieldName() {
        return sourceFieldName;
    }

    /* (non-Javadoc)
     * @see nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IDetailInformation#getLogic()
     */
    public String getLogic() {
        return logic;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("", destFieldName)
                .append("", sourceFieldName).append("", logic).toString();
    }
}