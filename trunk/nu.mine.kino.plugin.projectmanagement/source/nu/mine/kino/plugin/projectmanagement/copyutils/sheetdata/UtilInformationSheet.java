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

import net.java.amateras.xlsbeans.annotation.IterateTables;
import net.java.amateras.xlsbeans.annotation.Sheet;

/**
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 * @version $Revision$
 */
@Sheet(name = "UtilInformation")//$NON-NLS-1$
public class UtilInformationSheet {
    private java.util.List<IUtilInformation> instanceList;

    @IterateTables(tableLabel = "載せ替えユーティリティ情報", tableClass = UtilInformation.class, bottom = 10)//$NON-NLS-1$
    public void setUtilInformation(java.util.List<IUtilInformation> instanceList) {
        this.instanceList = instanceList;
    }

    public java.util.List<IUtilInformation> getUtilInformation() {
        return instanceList;
    }

}
