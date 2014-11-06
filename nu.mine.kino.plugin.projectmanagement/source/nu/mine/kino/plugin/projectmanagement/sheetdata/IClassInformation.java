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
//çÏê¨ì˙: 2008/08/20
package nu.mine.kino.plugin.projectmanagement.sheetdata;

import java.util.List;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public interface IClassInformation {

    public void setFieldInformations(List<IFieldInformation> fieldInformations);

    public void setClassNameJ(String classNameJ);

    public void setDescription(String description);

    public void setPackageName(String packageName);

    public void setClassName(String className);

    public void setToString(String toString);

    public void setAddedInfo(String addedInfo);

    public List<IFieldInformation> getFieldInformations();

    public String getClassNameJ();

    public String getDescription();

    public String getPackageName();

    public String getClassName();

    public String getToString();

    public String getAddedInfo();

}
