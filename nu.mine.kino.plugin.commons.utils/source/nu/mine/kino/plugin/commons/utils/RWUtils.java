/******************************************************************************
 * Copyright (c) 2012 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//çÏê¨ì˙: 2012/06/09

package nu.mine.kino.plugin.commons.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class RWUtils {

    public static byte[] stream2ByteArray(InputStream stream)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int j;
        while ((j = stream.read(b)) != -1) {
            baos.write(b, 0, j);
        }
        byte[] pix = baos.toByteArray();
        return pix;
    }

    public static String stream2String(InputStream stream, String charset)
            throws IOException {
        byte[] pix = stream2ByteArray(stream);
        String body = new String(pix, charset);
        return body;
    }

    public static void streamToFile(InputStream stream, File file)
            throws IOException {
        byte[] pix = stream2ByteArray(stream);
        write(pix, file);
    }

    public static void write(byte[] b, File file) throws IOException {
        BufferedOutputStream stream = null;
        try {
            stream = new BufferedOutputStream(new FileOutputStream(file));
            stream.write(b);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }
    }

}
