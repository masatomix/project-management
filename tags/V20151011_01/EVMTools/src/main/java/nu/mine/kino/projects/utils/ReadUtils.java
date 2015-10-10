/******************************************************************************
 * Copyright (c) 2014 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//çÏê¨ì˙: 2015/01/27

package nu.mine.kino.projects.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class ReadUtils {

    public static String readFile(File file) throws IOException {
        StringWriter out = new StringWriter();
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            int i;
            while ((i = in.read()) != -1) {
                out.write(i);
            }
        } finally {
            in.close();
        }
        return out.toString();

        // StringBuffer buffer = new StringBuffer();
        // BufferedReader reader = null;
        // try {
        // reader = new BufferedReader(new FileReader(file));
        // String line;
        // while ((line = reader.readLine()) != null) {
        // buffer.append(line);
        // }
        // } finally {
        // if (reader != null) {
        // try {
        // reader.close();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // reader = null;
        // }
        // }
        // String string = new String(buffer);
        // return string;
    }
}
