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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import junit.framework.TestCase;
import nu.mine.kino.csv.CSVSampleBean;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class CsvToBeanTest extends TestCase {

    /**
     * ColumnPositionMappingStrategyを使ったサンプル。
     * sample.csvを読み込んで、値を標準出力に出力するだけのサンプルです。 CSVの列の順番に、
     * JavaBeansのどのフィールドにマッピングすればいいかを指定します。
     */
    public void testParseMappingStrategy01() {
        final String CSV_FILE = "sample.csv";
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
                String lastName = bean.getLast_name();
                String firstName = bean.getFirst_name();
                String age = bean.getAge();
                System.out.println(bean);
                assertNotNull(lastName);
                assertNotNull(firstName);
                assertNotNull(age);
            }
        } catch (FileNotFoundException e) {
            fail(e.getMessage());
        }
    }
}
