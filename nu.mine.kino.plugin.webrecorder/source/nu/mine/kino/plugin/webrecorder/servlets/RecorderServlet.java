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
//作成日: 2012/06/04

package nu.mine.kino.plugin.webrecorder.servlets;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import nu.mine.kino.plugin.commons.utils.HttpClientUtils;
import nu.mine.kino.plugin.webrecorder.HttpRequestUtils;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.eclipse.jetty.servlets.ProxyServlet;

/**
 * 基本的にはJettyのプロキシ機能を呼び出すServletです。 ProxyServlet処理後、
 * 再度Get/Post処理を自らおこない、ローカルに結果を保存する機能があります。
 * 
 * @author Masatomi KINO
 * @version $Revision$
 */
public class RecorderServlet extends ProxyServlet {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(RecorderServlet.class);

    public static final String METHOD_GET = "GET";

    public static final String METHOD_POST = "POST";

    private ServletContext servletContext;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        servletContext = config.getServletContext();
    }

    @Override
    public void service(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {

        HttpServletRequest hRequest = (HttpServletRequest) request;
        String method = hRequest.getMethod();

        // Getなら親Servletを実行し、再度Getを自ら行ってデータを保存する
        super.service(request, response);

        HttpRequestUtils.printInfo(hRequest);

        if (method.equals(METHOD_GET)) {
            executeGet(hRequest);
        } else if (method.equals(METHOD_POST)) {
            // Postも同じく親ServletであるProxyServletの処理を呼んで、もう一度Postしたかったが、
            // ProxyServlet実行後はRequestBodyのコンテンツを再取得できなくて自分で取りに行く処理で困った。
            // なので、自分の処理を優先して、保存したコンテンツからResponseを返すようにした
            // 2012/06/07追記:
            // 再取得できるようFilterを調整したので、super.service()に戻しました!
            executePost(hRequest);
        }
    }

    private void executePost(HttpServletRequest hRequest) throws IOException,
            ClientProtocolException {
        String url = HttpRequestUtils.getURLBase(hRequest);
        String body = HttpRequestUtils.getBody(hRequest);

        String contentType = hRequest.getContentType();
        HttpEntity entity = HttpClientUtils.getHttpEntity(url, body,
                contentType, null);
        if (entity != null) {
            File file = HttpRequestUtils
                    .getCachePathFromRequestForPost(hRequest);
            file.getParentFile().mkdirs();
            streamToFile(entity.getContent(), file);
        }
    }

    private void executeGet(HttpServletRequest hRequest) throws IOException,
            ClientProtocolException {
        String url = HttpRequestUtils.getURLWithQuery(hRequest);

        HttpEntity entity = HttpClientUtils.getHttpEntity(url);
        if (entity != null) {
            File file = HttpRequestUtils.getCachePathFromRequest(hRequest);
            file.getParentFile().mkdirs();
            streamToFile(entity.getContent(), file);
        }
    }

    private void streamToFile(InputStream in, File file) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int j;
        while ((j = in.read(b)) != -1)
            baos.write(b, 0, j);
        byte[] pix = baos.toByteArray();
        write(pix, file);
    }

    private void write(byte[] b, File file) {
        BufferedOutputStream stream = null;
        try {
            stream = new BufferedOutputStream(new FileOutputStream(file));
            stream.write(b);
        } catch (IOException e) {
            e.printStackTrace();
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
