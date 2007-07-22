/*******************************************************************************
 * Copyright (c) 2006 Masatomi KINO.
 * All rights reserved. 
 * $Id$
 *******************************************************************************/
//作成日: 2006/10/22
package nu.mine.kino.plugin.plugindocumentcreator;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

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
     * 引数の拡張ポイントIDから、拡張ポイントフォーマッターを取得するメソッドです。
     * 
     * @param extensionPointName
     * @return
     */
    public IExtensionPointFormatter getExtensionFormatter(
            String extensionPointName) {
        logger.debug("getExtensionFormatter(String) - start");

        // プラグインのレジストリ取得
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        // レジストリから、拡張ポイント名で拡張ポイントを取得
        IExtensionPoint point = registry
                .getExtensionPoint("nu.mine.kino.plugin.plugindocumentcreator.formatters");
        // nu.mine.kino.plugin.plugindocumentcreator.formatters 拡張ポイント
        // を使っているプラグイン一覧を取得。
        IConfigurationElement[] configurationElements = point
                .getConfigurationElements();
        // ぐるぐる回って、引数の拡張ポイントIDと同じ記述をさがし、フォーマッタを作成して返す。
        // マッチする記述は、2006/10/29現在、一つしかない想定。
        for (int i = 0; i < configurationElements.length; i++) {
            IConfigurationElement element = configurationElements[i];
            String id = element.getAttribute("extension-point-id");
            if (id.equals(extensionPointName)) {
                logger.debug(extensionPointName + "用のフォーマッタを生成します");
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
        logger.debug(extensionPointName + "にはデフォルトのフォーマッタを使用します");
        logger.debug("getExtensionFormatter(String) - end");
        return new DefaultExtensionPointFormatter();
    }
}
