/******************************************************************************
 * Copyright (c) 2009 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//çÏê¨ì˙: 2006/10/22
package nu.mine.kino.plugin.plugindocumentcreator;

import org.eclipse.core.runtime.IConfigurationElement;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class DefaultExtensionPointFormatter implements IExtensionPointFormatter {

    public String[] getInformations(IConfigurationElement element) {
        return new String[] { element.toString() };
    }

    /*
     * (non-Javadoc)
     * 
     * @see nu.mine.kino.plugin.plugindocumentcreator.IExtensionPointFormatter#getHeaders()
     */
    public String[] getHeaders() {
        return new String[] { "default" };
    }
}
