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
//作成日: 2012/06/09

package nu.mine.kino.plugin.commons.utils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

/**
 * Commons HttpClientの Facade.
 * 
 * @author Masatomi KINO
 * @version $Revision$
 */
public class HttpClientUtils {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(HttpClientUtils.class);

    public static String doGet(String url, HttpHost proxy)
            throws ClientProtocolException, IOException {
        logger.debug("doGet(String) - start");

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        logger.info("doGet URL: " + httpget.getURI());

        if (proxy != null) {
            httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
                    proxy);
        }

        // Create a response handler
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpget, responseHandler);

        logger.debug("doGet(String) - end");
        return responseBody;
    }

    public static String doGet(String url) throws ClientProtocolException,
            IOException {
        return doGet(url, null);
    }

    public static HttpEntity getHttpEntity(String url, HttpHost proxy)
            throws ClientProtocolException, IOException {

        HttpGet httpget = new HttpGet(url);
        HttpClient httpclient = new DefaultHttpClient();

        if (proxy != null) {
            // HttpHost proxy = new HttpHost("127.0.0.1", 8008);
            httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
                    proxy);
        }

        HttpResponse httpResponse = httpclient.execute(httpget);
        HttpEntity entity = httpResponse.getEntity();
        return entity;
    }

    public static HttpEntity getHttpEntity(String url)
            throws ClientProtocolException, IOException {
        return getHttpEntity(url, null);
    }

    public static HttpEntity getHttpEntity(String url, String requestBody,
            String contentType, HttpHost proxy) throws ClientProtocolException,
            IOException {
        HttpPost httppost = new HttpPost(url);
        HttpClient httpclient = new DefaultHttpClient();

        if (proxy != null) {
            httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
                    proxy);
        }

        ContentType contentTypeObj = null;
        if (contentType != null) {
            contentTypeObj = ContentType.parse(contentType);
        }

        StringEntity postEntity = null;
        try {
            postEntity = new StringEntity(requestBody, contentTypeObj);
        } catch (Exception e) {
            logger.warn("ContentTypeを指定してPostしようとするとエラーになったので、指定しないでPostすることにする");
            postEntity = new StringEntity(requestBody);
        }

        httppost.setEntity(postEntity);
        HttpResponse httpResponse = httpclient.execute(httppost);

        HttpEntity entity = httpResponse.getEntity();
        return entity;
    }
}
