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
//作成日: 2009/06/05
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
 * CSVのヘッダ行のヘッダ名と、JavaBeansのフィールド名の対応付けを、外部ファイルを用いて行えるようにしたStrategyクラス。
 * 
 * @author Masatomi KINO
 * @version $Revision$
 * @see HeaderColumnNameTranslateMappingStrategy
 */
public class HeaderColumnNameAutoTranslateMappingStrategy extends
        HeaderColumnNameTranslateMappingStrategy {

    private InputStream in;

    /**
     * ファイルへのストリームを設定します。setTypeより先に実行してください。
     * 
     * @param in
     */
    public void setInputStream(InputStream in) {
        this.in = in;
    }

    public HeaderColumnNameAutoTranslateMappingStrategy() {
        super();
    }

    @Override
    public void setType(Class clazz) {
        super.setType(clazz);
        setColumnMapping(MapUtils.createMapping(in, clazz));
    }

    // private Map<String, String> createMapping() {
    // Map<String, String> map = new LinkedHashMap<String, String>();
    // Class clazz = getType();
    // String name = clazz.getSimpleName();
    // URL url = clazz.getResource(name + ".txt");
    //
    // BufferedReader reader = null;
    // try {
    // String charsetName = "JISAutoDetect";
    // if (in != null) {
    // reader = new BufferedReader(new InputStreamReader(in,
    // charsetName));
    // } else {
    // reader = new BufferedReader(new InputStreamReader(url
    // .openStream(), charsetName));
    // }
    // String line;
    // while ((line = reader.readLine()) != null) {
    // // String[] split = StringUtils.split(line, '=');
    // String[] split = line.split("=");
    // map.put(split[0], split[1]);
    // }
    // } catch (FileNotFoundException e) {
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // } finally {
    // if (reader != null) {
    // try {
    // reader.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // reader = null;
    // }
    // if (in != null) {
    // try {
    // in.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // in = null;
    // }
    // }
    // return map;
    // }
}
