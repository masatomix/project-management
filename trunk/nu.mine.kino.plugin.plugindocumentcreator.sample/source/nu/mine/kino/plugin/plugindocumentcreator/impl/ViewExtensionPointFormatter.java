/*******************************************************************************
 * Copyright (c) 2006 Masatomi KINO.
 * All rights reserved. 
 * $Id$
 *******************************************************************************/
//作成日: 2006/10/22
package nu.mine.kino.plugin.plugindocumentcreator.impl;

import nu.mine.kino.plugin.plugindocumentcreator.IExtensionPointFormatter;

import org.eclipse.core.runtime.IConfigurationElement;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class ViewExtensionPointFormatter implements IExtensionPointFormatter {

    public String[] getInformations(IConfigurationElement element) {
        String pluginId = element.getNamespaceIdentifier(); // プラグインのID
        String id = element.getAttribute("id");// ビューのIDやカテゴリIDなど
        if (element.getName().equals("view")) {
            String category = element.getAttribute("category");
            category = category == null ? "" : category;
            return new String[] { pluginId, "ビュー", id,
                    element.getAttribute("name"),
                    element.getAttribute("class"), category };
        } else if (element.getName().equals("category")) {
            return new String[] { pluginId, "カテゴリ", id,
                    element.getAttribute("name") };
        } else if (element.getName().equals("stickyView")) {
            return new String[] { pluginId, "Stickyなビュー", id };
        }
        return new String[0];

    }

    /*
     * (non-Javadoc)
     * 
     * @see nu.mine.kino.plugin.plugindocumentcreator.IExtensionPointFormatter#getHeaders()
     */
    public String[] getHeaders() {
        return new String[] { "プラグインID", "種別", "ID", "ビュー名", "実装クラス",
                "属するカテゴリID" };
    }
}
