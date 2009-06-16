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
//作成日: 2009/06/16
package au.com.bytecode.opencsv.bean;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class MapUtils {
    /**
     * ストリームもしくは引数のClassクラスのあるパスより設定ファイルを取得し、key,valueを保持するMapを取得するメソッド。
     * 
     * @param in
     *            ストリーム
     * @param clazz
     * @return
     */
    public static Map<String, String> createMapping(InputStream in,
            Class<?> clazz) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        String name = clazz.getSimpleName();
        URL url = clazz.getResource(name + ".txt");

        BufferedReader reader = null;
        try {
            String charsetName = "JISAutoDetect";
            if (in != null) {
                reader = new BufferedReader(new InputStreamReader(in,
                        charsetName));
            } else {
                reader = new BufferedReader(new InputStreamReader(url
                        .openStream(), charsetName));
            }
            String line;
            while ((line = reader.readLine()) != null) {
                // String[] split = StringUtils.split(line, '=');
                String[] split = line.split("=");
                map.put(split[0], split[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                reader = null;
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
        }
        return map;
    }
}
