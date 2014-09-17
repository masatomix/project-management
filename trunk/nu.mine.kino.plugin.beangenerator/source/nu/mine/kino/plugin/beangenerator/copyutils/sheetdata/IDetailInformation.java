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

import net.java.amateras.xlsbeans.annotation.Column;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public interface IDetailInformation {

    /**
     * 
     * @param destFieldName
     */
    @Column(columnName = "コピー先フィールド名")//$NON-NLS-1$	
    public void setDestFieldName(String destFieldName);

    /**
     * 
     * @param sourceFieldName
     */
    @Column(columnName = "コピー元フィールド名")//$NON-NLS-1$	
    public void setSourceFieldName(String sourceFieldName);

    /**
     * 
     * @param logic
     */
    @Column(columnName = "ロジックの場合")//$NON-NLS-1$	
    public void setLogic(String logic);

    public String getDestFieldName();

    public String getSourceFieldName();

    public String getLogic();

}