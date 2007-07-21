/*******************************************************************************
 * Copyright (c) 2006 Masatomi KINO.
 * All rights reserved. 
 * $Id: ExtensionPointWriter.java,v 1.6 2006/11/04 12:52:05 cvsuser Exp $
 *******************************************************************************/
//作成日: 2006/10/22
package nu.mine.kino.plugin.plugindocumentcreator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * 拡張ポイントの情報をCSV形式で書き出すクラスです。
 * 
 * @author Masatomi KINO
 * @version $Revision: 1.6 $
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
     * @param extensionPointName
     *            拡張ポイントID
     */
    public void write(String extensionPointName) {
        logger.debug("write(String) - start");
        // 引数(extensionPointName)の拡張ポイント用のフォーマッタークラスを取得し、
        IExtensionPointFormatter formatter = new ExtensionPointFormatterManager()
                .getExtensionFormatter(extensionPointName);
        // StringBufferに情報を書き出していく。
        // formatterから、ヘッダ文字列の配列を取得し、
        String[] headers = formatter.getHeaders();
        // ""などの整形処理を経由して
        String headerString = convertArray2CSV(headers);

        // CSVにappend
        csvData.append(headerString);
        csvData.append(LINE_SEPARATOR);

        // extensionPointName を使用しているプラグイン一覧を取得。
        IExtension[] plugins = getPlugin(extensionPointName);
        for (int i = 0; i < plugins.length; i++) {
            // プラグイン名
            IConfigurationElement[] elements = plugins[i]
                    .getConfigurationElements();
            for (int j = 0; j < elements.length; j++) {
                IConfigurationElement element = elements[j];
                String[] informations = formatter.getInformations(element);
                String rowData = convertArray2CSV(informations);
                csvData.append(rowData);
                csvData.append(LINE_SEPARATOR);
            }
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

    /**
     * 引数の拡張ポイントを使っているプラグインを返す。但し取得されるIExtensionインタフェース (プラグイン)には、
     * その拡張ポイントの情報しか入ってない。
     * 
     * @param extensionPointName
     * @return
     */
    private IExtension[] getPlugin(String extensionPointName) {
        // プラグインのレジストリ取得
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        // レジストリから、拡張ポイント名で拡張ポイントを取得
        IExtensionPoint point = registry.getExtensionPoint(extensionPointName);
        // この拡張ポイントを使っているプラグイン一覧を取得。
        if (point != null) {
            IExtension[] extensions = point.getExtensions();
            return extensions;
        }
        return new IExtension[0];
    }

}
