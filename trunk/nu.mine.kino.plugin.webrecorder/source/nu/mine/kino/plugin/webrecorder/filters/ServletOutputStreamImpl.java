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
//çÏê¨ì˙: 2012/06/28

package nu.mine.kino.plugin.webrecorder.filters;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class ServletOutputStreamImpl extends ServletOutputStream {

    private FileOutputStream fileStream;

    private final ServletOutputStream delegate;

    public ServletOutputStreamImpl(ServletOutputStream outputStream,
            File cachePath) {
        this.delegate = outputStream;
        try {
            cachePath.getParentFile().mkdirs();
            fileStream = new FileOutputStream(cachePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(int i) throws IOException {
        fileStream.write(i);
        delegate.write(i);
    }

}
