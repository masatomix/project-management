import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import nu.mine.kino.csv.CSVSampleBean;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.BeanToCsv;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameAutoTranslateMappingStrategy;

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
//作成日: 2009/06/08
/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class CSVWriterSample04 {
    private static final String CSV_FILE = "sampleOut.csv";

    public static void main(String[] args) throws IOException {
        HeaderColumnNameAutoTranslateMappingStrategy strat = new HeaderColumnNameAutoTranslateMappingStrategy();
        FileInputStream in = new FileInputStream(new File("hogehoge.txt"));
        strat.setInputStream(in);
        strat.setType(CSVSampleBean.class);
        BeanToCsv csv = new BeanToCsv();
        List<CSVSampleBean> list = getList();
        // カンマ区切りで、""で囲まない。
        csv.writeAll(strat, new CSVWriter(new FileWriter(CSV_FILE), ',',
                '\u0000'), list);
    }

    public static List<CSVSampleBean> getList() {
        try {
            HeaderColumnNameAutoTranslateMappingStrategy strat = new HeaderColumnNameAutoTranslateMappingStrategy();
            strat.setType(CSVSampleBean.class);
            CsvToBean csv = new CsvToBean();
            List<CSVSampleBean> list = csv.parse(strat, new FileReader(
                    "sample.csv"));
            return list;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
