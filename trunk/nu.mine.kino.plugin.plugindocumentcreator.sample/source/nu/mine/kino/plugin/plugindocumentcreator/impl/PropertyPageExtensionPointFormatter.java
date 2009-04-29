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
public class PropertyPageExtensionPointFormatter implements
        IExtensionPointFormatter {

    public String[] getHeaders() {
        return new String[] { "プラグインのID", "ID", "名前", "実装クラス", "アイコン", "カテゴリ" };
    }

    public String[] getInformations(IConfigurationElement element) {
        String pluginId = element.getNamespaceIdentifier(); // プラグインのID
        if (element.getName().equals("page")) {
            String id = element.getAttribute("id");
            id = id == null ? "" : id;

            String clazz = element.getAttribute("class");
            clazz = clazz == null ? "" : clazz;

            String name = element.getAttribute("name");
            name = name == null ? "" : name;

            String icon = element.getAttribute("icon");
            icon = icon == null ? "" : icon;

            String category = element.getAttribute("category");
            category = category == null ? "" : category;

            return new String[] { pluginId, id, name, clazz, icon, category };
        }
        return new String[0];
    }
}
