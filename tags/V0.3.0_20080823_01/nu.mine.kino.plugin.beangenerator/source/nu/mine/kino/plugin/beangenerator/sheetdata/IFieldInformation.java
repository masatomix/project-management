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
// çÏê¨ì˙: 2008/08/15
package nu.mine.kino.plugin.beangenerator.sheetdata;


/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public interface IFieldInformation {

    public void setFieldNameJ(String fieldNameJ);

    public void setFieldName(String fieldName);

    public void setFieldType(String fieldType);

    public void setDescription(String description);

    public String getFieldNameJ();

    public String getFieldName();

    public String getFieldType();

    public String getDescription();
}
