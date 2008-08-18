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
//作成日: 2008/08/15
package nu.mine.kino.plugin.beangenerator.sheetdata;

import java.util.List;

import net.java.amateras.xlsbeans.annotation.IterateTables;
import net.java.amateras.xlsbeans.annotation.Sheet;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
@Sheet(name = "クラス") //$NON-NLS-1$
public class ClassInformationSheet {
    private List<ClassInformation> classInformation;

    @IterateTables(tableLabel = "クラス情報", tableClass = ClassInformation.class, bottom = 5) //$NON-NLS-1$
    public void setClassInformation(List<ClassInformation> classInformation) {
        this.classInformation = classInformation;
    }

    public List<ClassInformation> getClassInformation() {
        return classInformation;
    }
    
}
