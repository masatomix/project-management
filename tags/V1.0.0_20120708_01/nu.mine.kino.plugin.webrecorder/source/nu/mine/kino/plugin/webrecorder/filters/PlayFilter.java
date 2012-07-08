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
//作成日: 2012/06/03

package nu.mine.kino.plugin.webrecorder.filters;

import static nu.mine.kino.plugin.webrecorder.ProxyConstant.METHOD_GET;
import static nu.mine.kino.plugin.webrecorder.ProxyConstant.METHOD_POST;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import nu.mine.kino.plugin.webrecorder.HttpRequestUtils;
import nu.mine.kino.plugin.webrecorder.WebRecorderPlugin;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.IO;

/**
 * リクエストされたURLに従って、保存ファイルが存在するならそちらからレスポンスを返し、存在しない場合は
 * 通常通りサーバからレスポンスを返すフィルタ
 * @author Masatomi KINO
 * @version $Revision$
 */
public class PlayFilter implements Filter {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(PlayFilter.class);

    private ServletContext servletContext;

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        logger.debug("doFilter(ServletRequest, ServletResponse, FilterChain) - start");

        HttpServletRequest hRequest = (HttpServletRequest) request;
        String method = hRequest.getMethod();

        File file = null;
        if (method.equals(METHOD_POST)) {
            file = HttpRequestUtils.getCachePathFromRequestForPost(request);
        } else if (method.equals(METHOD_GET)) {
            file = HttpRequestUtils.getCachePathFromRequest(request);
        }

        String path = file.getPath();
        if (file.exists()) {
            logger.info(path + " があったのでキャッシュから返します");
            WebRecorderPlugin.getDefault().printURLConsole(
                    "{0} があったのでキャッシュから返します", path);
            returnFromCache(file.getAbsolutePath(), request, response);
        } else {
            logger.info(path + " がなかったので、サーバから取得して返します");
            WebRecorderPlugin.getDefault().printURLConsole(
                    "{0} がなかったので、サーバから取得して返します", path);

            chain.doFilter(request, response);
        }

        logger.debug("doFilter(ServletRequest, ServletResponse, FilterChain) - end");
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        servletContext = config.getServletContext();
    }

    private void returnFromCache(String filePath, ServletRequest request,
            ServletResponse response) throws FileNotFoundException, IOException {
        String mime = servletContext.getMimeType(filePath);
        if (mime != null) {
            response.setContentType(mime);
        }
        IO.copy(new FileInputStream(filePath), response.getOutputStream());
    }
}
