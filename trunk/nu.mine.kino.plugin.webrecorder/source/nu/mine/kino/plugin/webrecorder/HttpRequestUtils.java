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
//作成日: 2012/06/12

package nu.mine.kino.plugin.webrecorder;

import static nu.mine.kino.plugin.webrecorder.ProxyConstant.POST_BODY_FLAG;
import static nu.mine.kino.plugin.webrecorder.ProxyConstant.TRIM_FLAG;
import static nu.mine.kino.plugin.webrecorder.ProxyConstant.TRIM_LENGTH;
import static nu.mine.kino.plugin.webrecorder.ProxyConstant.TRIM_START_INDEX;
import static nu.mine.kino.plugin.webrecorder.ProxyConstant.METHOD_GET;
import static nu.mine.kino.plugin.webrecorder.ProxyConstant.METHOD_POST;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import nu.mine.kino.plugin.commons.utils.StringUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class HttpRequestUtils {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(HttpRequestUtils.class);

    public static String getBody(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuffer bodyBuf = new StringBuffer();
        try {
            String str = null;
            while ((str = reader.readLine()) != null) {
                bodyBuf.append(str);
            }
        } finally {
            reader.close();
        }
        String body = new String(bodyBuf);
        return body;
    }

    private static void addBodyForSuffix(HttpServletRequest request,
            StringBuffer buf) {
        // Bodyがあれば付けるただしSha1ハッシュして
        try {
            String body = getBody(request);
            logger.info("Req body: " + body);

            boolean trimFlag = WebRecorderPlugin.getDefault()
                    .getPreferenceStore().getBoolean(TRIM_FLAG);
            logger.debug("trim?: " + trimFlag);
            if (trimFlag) {
                int startIndex = WebRecorderPlugin.getDefault()
                        .getPreferenceStore().getInt(TRIM_START_INDEX);
                int length = WebRecorderPlugin.getDefault()
                        .getPreferenceStore().getInt(TRIM_LENGTH);
                logger.debug("startIndex: " + startIndex);
                logger.debug("length: " + length);
                if (length <= 0) {
                    body = body.substring(startIndex);
                } else {
                    body = body.substring(startIndex, startIndex + length);
                }
                logger.debug("body : " + body);
            }
            if (!StringUtils.isEmpty(body)) {
                String shaHex = DigestUtils.shaHex(body.getBytes());
                logger.info("bodyをSHA1ハッシュ: " + shaHex);
                buf.append("_");
                buf.append(shaHex);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getCachePathFromRequest(ServletRequest request) {
        HttpServletRequest hRequest = (HttpServletRequest) request;
        String requestURI = hRequest.getRequestURI();
        String host = hRequest.getHeader("Host");
        host = host.replace(':', '/');

        File hostDir = new File(WebRecorderPlugin.getDefault()
                .getCacheBasepath(), host);
        StringBuffer buf = new StringBuffer();
        buf.append(requestURI);
        // queryがあれば付けるただしSha1ハッシュして
        String queryString = hRequest.getQueryString();
        if (queryString != null && !"".equals(queryString)) {
            String shaHex = DigestUtils.shaHex(queryString.getBytes());
            buf.append("_");
            buf.append(shaHex);
        }

        String fileName = Utils.appendPrefix(new String(buf), "get_");
        File file = new File(hostDir, fileName);

        WebRecorderPlugin.getDefault().printURLConsole(
                "保存先:" + file.getAbsolutePath());
        return file;
    }

    public static File getCachePathFromRequestForPost(ServletRequest request) {
        HttpServletRequest hRequest = (HttpServletRequest) request;
        String requestURI = hRequest.getRequestURI();
        String host = hRequest.getHeader("Host");
        host = host.replace(':', '/');

        File hostDir = new File(WebRecorderPlugin.getDefault()
                .getCacheBasepath(), host);

        StringBuffer buf = new StringBuffer();
        buf.append(requestURI);
        // PostをリクエストBodyまで考慮してファイル名を決めるかフラグ。
        boolean postBodyFlag = WebRecorderPlugin.getDefault()
                .getPreferenceStore().getBoolean(POST_BODY_FLAG);
        if (postBodyFlag) {
            addBodyForSuffix(hRequest, buf);
        }

        String fileName = Utils.appendPrefix(new String(buf), "post_");
        File file = new File(hostDir, fileName);

        WebRecorderPlugin.getDefault().printURLConsole(
                "保存先:" + file.getAbsolutePath());
        return file;
    }

    public static String getURLBase(HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        StringBuffer buf = new StringBuffer();
        buf.append(request.getHeader("Host"));
        if (requestURI != null) {
            buf.append(requestURI);
        }
        String url = request.getScheme() + "://" + new String(buf);
        return url;
    }

    public static String getURLWithQuery(HttpServletRequest request) {
        String baseURL = getURLBase(request);
        String queryString = request.getQueryString();
        if (queryString != null) {
            logger.debug("URL: " + baseURL);
            return baseURL + "?" + queryString;
        }
        logger.debug("URL: " + baseURL);
        return baseURL;
    }

    public static final String[] EXCEPT_EXTs = new String[] { ".png", ".jpg",
            ".gif", ".css", ".js", ".svg" }; // 情報の出力は不要なので除外

    public static void printInfo(HttpServletRequest hRequest) {
        String method = hRequest.getMethod();
        String url = getURLBase(hRequest);// URLを復元

        if (StringUtils.isEmpty(url) || StringUtils.endsWith(url, EXCEPT_EXTs)) {
            return;
        }

        WebRecorderPlugin.getDefault().printConsole("リクエスト情報");
        if (method.equals(METHOD_GET)) {
            WebRecorderPlugin.getDefault().printConsole("GET:   " + url);
            String queryString = hRequest.getQueryString();
            if (!StringUtils.isEmpty(queryString)) {
                WebRecorderPlugin.getDefault().printConsole(
                        "query: " + queryString);
                String shaHex = DigestUtils.shaHex(queryString.getBytes());
                WebRecorderPlugin.getDefault().printConsole(
                        "queryをSHA1ハッシュ: " + shaHex);

            }
        } else if (method.equals(METHOD_POST)) {
            WebRecorderPlugin.getDefault().printConsole("POST:  " + url);
            try {
                String body = getBody(hRequest);
                if (!StringUtils.isEmpty(body)) {
                    WebRecorderPlugin.getDefault().printConsole(
                            "Body:  " + body);
                    String shaHex = DigestUtils.shaHex(body.getBytes());
                    WebRecorderPlugin.getDefault().printConsole(
                            "BodyをSHA1ハッシュ: " + shaHex);
                }
            } catch (IOException e) {
                WebRecorderPlugin.getDefault().printConsole(e.getMessage());
            }
        }

    }
}
