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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;

/**
 * 拡張ポイントの情報をCSV形式で書き出すクラスです。
 * 
 * @author Masatomi KINO
 * @version $Revision$
 */
public class ExtensionPointWriter implements IExtensionPointWriter {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(ExtensionPointWriter.class);

    private static final String LINE_SEPARATOR = System
            .getProperty("line.separator");

    private StringBuffer csvData;

    private BufferedWriter out;

    /**
     * @param file
     * @throws FileNotFoundException
     */
    public ExtensionPointWriter(File file) throws FileNotFoundException {
        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                file)));
        csvData = new StringBuffer();
    }

    /**
     * 指定された拡張ポイントの情報を書き出すメソッドです。
     * 
     * @param id
     *            拡張ポイントIDでなく、ID
     */
    public void write(String id) {// 現状ID。拡張ポイントIDじゃない
        logger.debug("write(String) - start");
        // 引数(id)の設定から、フォーマッタークラスを取得し、
        IExtensionPointFormatter formatter = new ExtensionPointFormatterManager()
                .getExtensionFormatter(id);
        // StringBufferに情報を書き出していく。
        // formatterから、ヘッダ文字列の配列を取得し、
        String[] headers = formatter.getHeaders();
        // ""などの整形処理を経由して
        String headerString = convertArray2CSV(headers);

        // CSVにappend
        csvData.append(headerString);
        csvData.append(LINE_SEPARATOR);

        // ここでいったんID->拡張ポイントIDに変換が必要。
        String extensionPointId = id2ExtensionPointId(id);

        // extensionPointId を使用しているプラグイン一覧を取得。
        List<IConfigurationElement> configurationElements = Utils
                .getConfigurationElements(extensionPointId);

        for (IConfigurationElement element : configurationElements) {
            String[] informations = formatter.getInformations(element);
            String rowData = convertArray2CSV(informations);
            csvData.append(rowData);
            csvData.append(LINE_SEPARATOR);
        }
        try {
            // bufferをファイルに書き出しー
            out.write(csvData.toString());
        } catch (IOException e) {
            logger.error("write(String)", e);
            Activator.logException(e, false);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                logger.error("write(String)", e);
                Activator.logException(e, false);
            }
        }
        logger.debug("write(String) - end");
    }

    private String id2ExtensionPointId(String id) {
        List<IConfigurationElement> list = Utils.getConfigurationElements(
                "nu.mine.kino.plugin.plugindocumentcreator.formatters",
                "formatter");
        for (IConfigurationElement element : list) {
            String idInXml = element.getAttribute("id");
            if (idInXml.equals(id)) {
                logger.debug(id + " の設定が見つかりました。");
                String extension_point_id = element
                        .getAttribute("extension-point-id");
                logger.debug("拡張ポイントIDは " + extension_point_id + " ですね。");
                return extension_point_id;
            }
        }
        return null;
    }

    private String convertArray2CSV(String[] strArray) {
        StringBuffer retData = new StringBuffer();
        retData.append('"');
        for (int i = 0; i < strArray.length; i++) {
            String header = strArray[i];
            if (i > 0) {
                retData.append("\",\"");
            }
            retData.append(header);
        }
        retData.append("\"");
        return new String(retData);
    }

    // /**
    // * 引数の拡張ポイントを使っているプラグインを返す。但し取得されるIExtensionインタフェース (プラグイン)には、
    // * その拡張ポイントの情報しか入ってない。
    // *
    // * @param extensionPointName
    // * @return
    // */
    // private IExtension[] getPlugin(String extensionPointName) {
    // // プラグインのレジストリ取得
    // IExtensionRegistry registry = Platform.getExtensionRegistry();
    // // レジストリから、拡張ポイント名で拡張ポイントを取得
    // IExtensionPoint point = registry.getExtensionPoint(extensionPointName);
    // // この拡張ポイントを使っているプラグイン一覧を取得。
    // if (point != null) {
    // IExtension[] extensions = point.getExtensions();
    // return extensions;
    // }
    // return new IExtension[0];
    // }

}
