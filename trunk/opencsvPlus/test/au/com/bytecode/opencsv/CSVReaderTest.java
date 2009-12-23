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

package au.com.bytecode.opencsv;

import java.io.FileReader;

import junit.framework.TestCase;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class CSVReaderTest extends TestCase {
    /**
     * sample.csvを読み込んで、値を標準出力に出力するだけのサンプルです。
     * 
     * @throws Exception
     */
    public void testReadNext01() throws Exception {

        CSVReader reader = new CSVReader(new FileReader("sample.csv"));
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            // nextLine[] is an array of values from the line
            System.out.println(nextLine[0] + " " + nextLine[1] + " etc...");
        }
    }
}
