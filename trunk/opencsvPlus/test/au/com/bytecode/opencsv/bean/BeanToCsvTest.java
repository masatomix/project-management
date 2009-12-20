/******************************************************************************
 * Copyright (c) 2008 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//çÏê¨ì˙: 2009/12/21

package au.com.bytecode.opencsv.bean;

import java.io.FileReader;

import au.com.bytecode.opencsv.CSVReader;
import junit.framework.TestCase;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class BeanToCsvTest extends TestCase {

    public void testMethod() throws Exception {
        CSVReader reader = new CSVReader(new FileReader("sample.csv"));
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            // nextLine[] is an array of values from the line
            System.out.println(nextLine[0] + " " + nextLine[1] + " etc...");
        }
    }
}
