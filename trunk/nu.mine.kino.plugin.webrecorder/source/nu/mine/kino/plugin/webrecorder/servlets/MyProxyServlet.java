/******************************************************************************
 * Copyright (c) 2010 Masatomi KINO and others. 
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import nu.mine.kino.plugin.webrecorder.WebRecorderPlugin;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.eclipse.jetty.servlets.ProxyServlet;
import org.eclipse.jetty.util.IO;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class MyProxyServlet extends ProxyServlet {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(MyProxyServlet.class);

    private static final String METHOD_GET = "GET";

    private static final String METHOD_POST = "POST";

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

        if (method.equals(METHOD_GET)) {
            super.service(request, response);
            executeGet(hRequest);
        } else if (method.equals(METHOD_POST)) {
            // ProxyServletの処理を呼んで、もう一度Postしたかったが、
            // ProxyServlet実行後はBodyのコンテンツを再度とれないので
            // 自分の処理を優先して、保存したコンテンツからResponseを返すようにした
            executePost(hRequest);
            // キャッシュから返す
            File file = WebRecorderPlugin.getDefault()
                    .getCachePathFromRequestForPost(request);
            if (file.exists()) {
                returnFromCache(file.getAbsolutePath(), response);
            }
        }
    }

    private void executePost(HttpServletRequest hRequest) throws IOException,
            ClientProtocolException {
        String requestURI = hRequest.getRequestURI();

        String body = WebRecorderPlugin.getDefault().getBody(hRequest);

        StringBuffer buf = new StringBuffer();
        buf.append(hRequest.getHeader("Host"));
        if (requestURI != null) {
            buf.append(requestURI);
        }
        HttpPost httppost = new HttpPost(hRequest.getScheme() + "://"
                + new String(buf));
        HttpClient httpclient = new DefaultHttpClient();

        String contentType = hRequest.getContentType();
        ContentType contentTypeObj = ContentType.parse(contentType);
        StringEntity postEntity = null;
        try {
            postEntity = new StringEntity(body, contentTypeObj);
        } catch (Exception e) {
            logger.warn("ContentTypeを指定してPostしようとするとエラーになったので、指定しないでPostすることにする");
            postEntity = new StringEntity(body);
        }

        httppost.setEntity(postEntity);
        HttpResponse httpResponse = httpclient.execute(httppost);

        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            File file = WebRecorderPlugin.getDefault()
                    .getCachePathFromRequestForPost(hRequest);
            file.getParentFile().mkdirs();
            streamToFile(entity.getContent(), file);
        }
    }

    private void executeGet(HttpServletRequest hRequest) throws IOException,
            ClientProtocolException {
        String queryString = hRequest.getQueryString();
        String requestURI = hRequest.getRequestURI();

        StringBuffer buf = new StringBuffer();
        buf.append(hRequest.getHeader("Host"));
        if (requestURI != null) {
            buf.append(requestURI);
        }
        if (queryString != null) {
            buf.append("?");
            buf.append(queryString);
        }
        logger.debug("URL: " + buf.toString());

        HttpGet httpget = new HttpGet(hRequest.getScheme() + "://"
                + new String(buf));
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse httpResponse = httpclient.execute(httpget);
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            File file = WebRecorderPlugin.getDefault().getCachePathFromRequest(
                    hRequest);
            file.getParentFile().mkdirs();
            streamToFile(entity.getContent(), file);
        }
        // Enumeration<String> headerNames = hRequest.getHeaderNames();
        // System.out.println("-----");
        // while (headerNames.hasMoreElements()) {
        // String string = (String) headerNames.nextElement();
        // System.out.println(string + ": " + hRequest.getHeader(string));
        // }

        //
        // HttpClient provides URIBuilder utility class to simplify creation and
        // modification of request URIs.
        //
        // URIBuilder builder = new URIBuilder();
        // builder.setHost(hRequest.getHeader("Host")).setPath(path);
        // builder.setScheme("http").setHost("www.google.com").setPath("/search")
        // .setParameter("q", "httpclient")
        // .setParameter("btnG", "Google Search")
        // .setParameter("aq", "f")
        // .setParameter("oq", "");
        // URI uri = builder.build();
        // HttpGet httpget = new HttpGet(uri);
        // System.out.println(httpget.getURI());
    }

    private void returnFromCache(String filePath, ServletResponse response)
            throws FileNotFoundException, IOException {
        String mime = servletContext.getMimeType(filePath);
        if (mime != null) {
            response.setContentType(mime);
        }
        IO.copy(new FileInputStream(filePath), response.getOutputStream());
    }

    // @Override
    // protected void doGet(HttpServletRequest request,
    // HttpServletResponse response) throws ServletException, IOException {
    // HttpServletRequest hRequest = (HttpServletRequest) request;
    // System.out.println("query: " + hRequest.getQueryString());
    // }
    //
    // @Override
    // protected void doPost(HttpServletRequest request,
    // HttpServletResponse response) throws ServletException, IOException {
    // HttpServletRequest hRequest = (HttpServletRequest) request;
    // System.out.println("query: " + hRequest.getQueryString());
    // BufferedReader reader = hRequest.getReader();
    // StringBuffer buf = new StringBuffer();
    // try {
    // String str = null;
    // while ((str = reader.readLine()) != null) {
    // buf.append(str);
    // }
    // } finally {
    // reader.close();
    // }
    // System.out.println("Body: " + buf.toString());
    // }

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
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
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
