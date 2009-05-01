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
//作成日: 2009/05/01
package nu.mine.kino.plugin.plugindocumentcreator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class Utils {
    /**
     * 指定した拡張ポイントに対して、複数種類のサブ要素がある場合、idで指定したサブ要素の{@link IConfigurationElement}
     * のListを返すメソッドです。
     * 
     * @param extensionPointId
     *            指定した拡張ポイント
     * @param id
     *            サブ要素の名称
     * @return
     */
    public static List<IConfigurationElement> getConfigurationElements(
            String extensionPointId, String id) {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtensionPoint point = registry.getExtensionPoint(extensionPointId);
        IExtension[] extensions = point.getExtensions();

        List<IConfigurationElement> ansList = new ArrayList<IConfigurationElement>();
        // このforは extensionタグ の繰り返し
        for (int i = 0; i < extensions.length; i++) {
            IConfigurationElement[] elements = extensions[i]
                    .getConfigurationElements();
            // このforは その下のタグの繰り返し
            for (int j = 0; j < elements.length; j++) {
                // nullだったら、addしちゃう。
                if (id == null || elements[j].getName().equals(id)) {
                    ansList.add(elements[j]);
                }
            }
        }
        return ansList;
    }

    public static List<IConfigurationElement> getConfigurationElements(
            String extensionPointId) {
        return getConfigurationElements(extensionPointId, null);
    }
}
