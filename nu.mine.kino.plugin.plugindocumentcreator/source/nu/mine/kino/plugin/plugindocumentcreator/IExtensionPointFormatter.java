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
 * @version $Revision: 1.2 $
 */
public interface IExtensionPointFormatter {

    String[] getInformations(IConfigurationElement element);

    /**
     * @return
     */
    String[] getHeaders();

}
