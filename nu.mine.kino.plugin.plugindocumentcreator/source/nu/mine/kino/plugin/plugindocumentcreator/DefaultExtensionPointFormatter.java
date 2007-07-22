/*******************************************************************************
 * Copyright (c) 2006 Masatomi KINO.
 * All rights reserved. 
 * $Id$
 *******************************************************************************/
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
