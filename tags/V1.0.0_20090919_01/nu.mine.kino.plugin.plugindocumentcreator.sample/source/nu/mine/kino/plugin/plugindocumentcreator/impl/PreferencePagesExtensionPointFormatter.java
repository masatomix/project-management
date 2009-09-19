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

import org.eclipse.core.runtime.IConfigurationElement;

import nu.mine.kino.plugin.plugindocumentcreator.IExtensionPointFormatter;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class PreferencePagesExtensionPointFormatter implements
        IExtensionPointFormatter {

    public String[] getHeaders() {
        return new String[] { "プラグインのID", "カテゴリID", "name", "id", "実装class" };
    }

    public String[] getInformations(IConfigurationElement element) {
        String pluginId = element.getNamespaceIdentifier(); // プラグインのID
        if (element.getName().equals("page")) {
            String id = element.getAttribute("id");
            id = id == null ? "" : id;

            String category = element.getAttribute("category");
            category = category == null ? "" : category;

            String name = element.getAttribute("name");
            name = name == null ? "" : name;

            String clazz = element.getAttribute("class");
            clazz = clazz == null ? "" : clazz;

            return new String[] { pluginId, category, name, id, clazz };
        }
        return new String[0];
    }
}
