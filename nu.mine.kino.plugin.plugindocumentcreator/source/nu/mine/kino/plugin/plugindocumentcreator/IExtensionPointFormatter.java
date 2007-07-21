/*******************************************************************************
 * Copyright (c) 2006 Masatomi KINO.
 * All rights reserved. 
 * $Id: IExtensionPointFormatter.java,v 1.2 2006/10/25 17:32:24 cvsuser Exp $
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
