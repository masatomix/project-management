/*******************************************************************************
 * Copyright (c) 2006 Masatomi KINO.
 * All rights reserved. 
 * $Id: DefaultExtensionPointFormatter.java,v 1.1 2006/10/29 04:43:19 cvsuser Exp $
 *******************************************************************************/
//çÏê¨ì˙: 2006/10/22
package nu.mine.kino.plugin.plugindocumentcreator;

import org.eclipse.core.runtime.IConfigurationElement;

/**
 * @author Masatomi KINO
 * @version $Revision: 1.1 $
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
