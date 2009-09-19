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
//作成日: 2009/04/29
package nu.mine.kino.plugin.plugindocumentcreator.impl;

import nu.mine.kino.plugin.plugindocumentcreator.IExtensionPointFormatter;

import org.eclipse.core.runtime.IConfigurationElement;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class HandlerExtensionPointFormatter implements IExtensionPointFormatter {

    public String[] getHeaders() {
        return new String[] { "プラグインのID", "コマンドID", "実装クラス" };
    }

    public String[] getInformations(IConfigurationElement element) {
        String pluginId = element.getNamespaceIdentifier(); // プラグインのID
        if (element.getName().equals("handler")) {
            String commandId = element.getAttribute("commandId");
            commandId = commandId == null ? "" : commandId;

            String clazz = element.getAttribute("class");
            clazz = clazz == null ? "" : clazz;

            return new String[] { pluginId, commandId, clazz };
        }
        return new String[0];
    }
}
