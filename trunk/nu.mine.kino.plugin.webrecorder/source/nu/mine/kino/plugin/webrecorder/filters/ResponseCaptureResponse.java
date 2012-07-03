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
//çÏê¨ì˙: 2012/06/27

package nu.mine.kino.plugin.webrecorder.filters;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class ResponseCaptureResponse extends HttpServletResponseWrapper {

    private final File cachePath;

    public ResponseCaptureResponse(HttpServletResponse response, File cachePath) {
        super(response);
        this.cachePath = cachePath;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStreamImpl(super.getOutputStream(), cachePath);
    }

}