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
package nu.mine.kino.plugin.beangenerator.sheetdata.anno;

import java.util.List;

import net.java.amateras.xlsbeans.annotation.IterateTables;
import net.java.amateras.xlsbeans.annotation.Sheet;
import nu.mine.kino.plugin.beangenerator.sheetdata.IClassInformation;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
@Sheet(name = "クラス")//$NON-NLS-1$
public class ClassInformationSheetWithAnno {
    private List<IClassInformation> classInformation;

    @IterateTables(tableLabel = "クラス情報", tableClass = ClassInformationWithAnno.class, bottom = 6)//$NON-NLS-1$
    public void setClassInformation(List<IClassInformation> classInformation) {
        this.classInformation = classInformation;
    }

    public List<IClassInformation> getClassInformation() {
        return classInformation;
    }

}
