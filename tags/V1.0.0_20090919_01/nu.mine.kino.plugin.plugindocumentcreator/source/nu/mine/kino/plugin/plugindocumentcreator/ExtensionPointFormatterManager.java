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
//作成日: 2006/10/22
package nu.mine.kino.plugin.plugindocumentcreator;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

/**
 * 拡張ポイント情報を出力するフォーマッタクラスを管理するクラスです。
 * 
 * @author Masatomi KINO
 * @version $Revision$
 */
public class ExtensionPointFormatterManager {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(ExtensionPointFormatterManager.class);

    /**
     * 引数のIDから、拡張ポイントフォーマッターを取得するメソッドです。
     * このIDは設定上のキーとなっているIDであり、拡張ポイントIDではないことに注意。
     * 
     * @param id
     *            拡張ポイントのIDではない。
     * @return
     */
    public IExtensionPointFormatter getExtensionFormatter(String id) {
        logger.debug("getExtensionFormatter(String) - start");
        // 拡張ポイントIDと、サブ要素の名称を使って、サブ要素のリストを取得するUtilsメソッド。
        List<IConfigurationElement> list = Utils.getConfigurationElements(
                "nu.mine.kino.plugin.plugindocumentcreator.formatters",
                "formatter");
        // ぐるぐる回って、引数のIDの設定をさがし、フォーマッタを作成して返す。
        for (IConfigurationElement element : list) {
            String keyId = element.getAttribute("id");
            // 一つのキーと、引数のキーが一致した場合、
            if (keyId.equals(id)) {
                logger.debug(id + "用のフォーマッタを生成します");
                try {
                    // 拡張ポイント独特のインスタンス生成方法。
                    IExtensionPointFormatter formatter = (IExtensionPointFormatter) element
                            .createExecutableExtension("class");
                    logger.debug("getExtensionFormatter(String) - end");
                    return formatter;
                } catch (CoreException e) {
                    logger.error("getExtensionFormatter(String)", e);
                    Activator.logException(e, false);
                    break;
                }
            }
        }
        logger.debug(id + "にはデフォルトのフォーマッタを使用します");
        logger.debug("getExtensionFormatter(String) - end");
        return new DefaultExtensionPointFormatter();
    }
}
