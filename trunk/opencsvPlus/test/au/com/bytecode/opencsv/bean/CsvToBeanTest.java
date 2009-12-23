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
//作成日: 2009/12/23

package au.com.bytecode.opencsv.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

import junit.framework.TestCase;
import nu.mine.kino.csv.CSVSampleBean;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class CsvToBeanTest extends TestCase {
    private static final String CSV_FILE = "sample.csv";

    private void assertNotNull(CSVSampleBean bean) {
        String lastName = bean.getLast_name();
        String firstName = bean.getFirst_name();
        String age = bean.getAge();
        assertNotNull(lastName);
        assertNotNull(firstName);
        assertNotNull(age);
        System.out.println(bean);
    }

    /**
     * ColumnPositionMappingStrategyを使ったサンプル。
     * sample.csvを読み込んで、値を標準出力に出力するだけのサンプルです。 CSVの列の順番に、
     * JavaBeansのどのフィールドにマッピングすればいいかを指定します。
     */
    public void testParseMappingStrategy01() {
        // final String CSV_FILE = "sample.csv";
        ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
        strat.setType(CSVSampleBean.class);
        // CSVの順番で、どのフィールドにマッピングすればいいかを指定する。
        String[] columns = new String[] { "last_name", "first_name", "age" };
        strat.setColumnMapping(columns);

        CsvToBean csv = new CsvToBean();
        try {
            List<CSVSampleBean> list = csv.parse(strat,
                    new FileReader(CSV_FILE));
            for (CSVSampleBean bean : list) {
                assertNotNull(bean);
            }
        } catch (FileNotFoundException e) {
            fail(e.getMessage());
        }
    }

    /**
     * sample.csvを読み込んで、値を標準出力に出力するだけのサンプルです。
     * このサンプルは、CSVの一行目をヘッダ行と見なし、ヘッダの名称とJavaBeansのフィールドをMapを使って関連づけることにより、
     * CSVをJavaBeansにマッピングします。
     * 
     * @throws Exception
     */
    public void testParseMappingStrategy02() throws Exception {
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
            assertNotNull(bean);
        }
    }

    private Map<String, String> createMapping() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("姓", "last_name");
        map.put("名", "first_name");
        map.put("年齢", "age");
        return map;
    }

    /**
     * sample.csvを読み込んで、値を標準出力に出力するだけのサンプルです。
     * このサンプルは、CSVの一行目をヘッダ行と見なし、ヘッダの名称とJavaBeansのフィールド名を設定ファイルで関連づけることにより、
     * CSVをJavaBeansにマッピングします。 このStrategyクラスは純正のOpenCsvにはなかったため、自作したクラスです。
     * 
     * @author Masatomi KINO
     * @version $Revision$
     */
    public void testParseMappingStrategy03() throws Exception {
        System.out.println("parseMappingStrategy03: start.");
        // HeaderColumnNameAutoTranslateMappingStrategy strat = new
        // HeaderColumnNameAutoTranslateMappingStrategy();
        // FileInputStream in = new FileInputStream(new File("hogehoge.txt"));
        // strat.setInputStream(in);
        HeaderColumnNameMappingStrategy strat = new HeaderColumnNameAutoTranslateMappingStrategy();
        strat.setType(CSVSampleBean.class);
        CsvToBean csv = new CsvToBean();
        List<CSVSampleBean> list = csv.parse(strat, new FileReader(CSV_FILE));
        for (CSVSampleBean bean : list) {
            assertNotNull(bean);
        }
        System.out.println("parseMappingStrategy03: end.");
    }

}
