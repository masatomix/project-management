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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import nu.mine.kino.plugin.commons.utils.StringUtils;

/**
 * Accept Encodingを除去するRequest Wrapper。 クライアントがAccept-Encoding: gzip,deflate
 * を指定していると、Gzip圧縮されたコンテンツがおりてきて、ファイル保存のプログラム{@link ServletOutputStreamImpl}
 * が対応できていない。なので、暫定でAccept-Encoding ヘッダを除去する処理を入れている。
 * 
 * @author Masatomi KINO
 * @version $Revision$
 */
public class AcceptEncodingRemoveRequest extends HttpServletRequestWrapper {

    public AcceptEncodingRemoveRequest(HttpServletRequest httpServletRequest)
            throws IOException {
        super(httpServletRequest);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        if (!StringUtils.isEmpty(name) && name.equals("Accept-Encoding")) {
            return Collections.enumeration(Collections.EMPTY_LIST);
        }
        return super.getHeaders(name);
    }

    @Override
    public String getHeader(String name) {
        if (!StringUtils.isEmpty(name) && name.equals("Accept-Encoding")) {
            return null;
        }
        return super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> retList = new ArrayList<String>();
        Enumeration<String> headerNames = super.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = (String) headerNames.nextElement();
            if (!header.equals("Accept-Encoding")) {
                retList.add(header);
            }
        }
        return Collections.enumeration(retList);
    }
}
