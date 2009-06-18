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
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nu.mine.kino.csv.CSVSampleBean;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

/**
 * sample.csvを読み込んで、値を標準出力に出力するだけのサンプルです。
 * このサンプルは、CSVの一行目をヘッダ行と見なし、ヘッダの名称とJavaBeansのフィールドをMapを使って関連づけることにより、
 * CSVをJavaBeansにマッピングします。
 * 
 * @author Masatomi KINO
 * @version $Revision$
 */
public class HeaderColumnNameTranslateSample02 {
    private static final String CSV_FILE = "sample.csv";

    public static void main(String[] args) throws IOException {
        HeaderColumnNameTranslateMappingStrategy strat = new HeaderColumnNameTranslateMappingStrategy();
        strat.setType(CSVSampleBean.class);
        // CSVのヘッダ名が、どのフィールドにマッピングすればいいかを指定する。
        // map.put("[ヘッダ名]", "[フィールド名]");
        // この場合は、ヘッダ行が無視される。
        Map<String, String> map = createMapping();
        strat.setColumnMapping(map);

        CsvToBean csv = new CsvToBean();
        List<CSVSampleBean> list = csv.parse(strat, new FileReader(CSV_FILE));
        for (CSVSampleBean bean : list) {
            System.out.println(bean);
        }
    }

    private static Map<String, String> createMapping() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("姓", "last_name");
        map.put("名", "first_name");
        map.put("年齢", "age");
        return map;
    }
}
