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
//作成日: 2012/06/07

package nu.mine.kino.plugin.webrecorder.filters;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * リクエストBodyを何度も読めるようにしたRequest Wrapper。
 * 
 * @author Masatomi KINO
 * @version $Revision$
 */
public class MultiReadHttpServletRequest extends HttpServletRequestWrapper {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(MultiReadHttpServletRequest.class);

    private byte[] body;

    public MultiReadHttpServletRequest(HttpServletRequest httpServletRequest)
            throws IOException {
        super(httpServletRequest);
        // Read the request body and save it as a byte array
        InputStream is = super.getInputStream();
        body = IOUtils.toByteArray(is);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStreamImpl(new ByteArrayInputStream(body));
    }

    @Override
    public BufferedReader getReader() throws IOException {
        String enc = getCharacterEncoding();
        if (enc == null)
            enc = "UTF-8";
        return new BufferedReader(new InputStreamReader(getInputStream(), enc));
    }

    private class ServletInputStreamImpl extends ServletInputStream {

        private InputStream is;

        public ServletInputStreamImpl(InputStream is) {
            this.is = is;
        }

        public int read() throws IOException {
            return is.read();
        }

        public boolean markSupported() {
            return false;
        }

        public synchronized void mark(int i) {
            throw new RuntimeException(new IOException(
                    "mark/reset not supported"));
        }

        public synchronized void reset() throws IOException {
            throw new IOException("mark/reset not supported");
        }
    }

}
