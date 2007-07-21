/*******************************************************************************
 * Copyright (c) 2006 Masatomi KINO.
 * All rights reserved. 
 * $Id$
 *******************************************************************************/
//作成日: 2006/10/29
package nu.mine.kino.plugin.plugindocumentcreator;

/**
 * 拡張ポイントの情報を書き出すインタフェースです
 * 
 * @author Masatomi KINO
 * @version $Revision: 1.1 $
 */
public interface IExtensionPointWriter {
    /**
     * 指定された拡張ポイントIDの情報を書き出します。
     * 
     * @param extensionPointName
     */
    void write(String extensionPointName);
}
