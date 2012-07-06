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

import org.apache.log4j.Logger;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class LogFilter implements Filter {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(LogFilter.class);

    private ServletContext servletContext;

    @Override
    public void destroy() {
        // TODO 自動生成されたメソッド・スタブ
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        logger.debug("doFilter(ServletRequest, ServletResponse, FilterChain) - start");

        HttpServletRequest hRequest = (HttpServletRequest) request;
        HttpRequestUtils.printInfo(hRequest);
        chain.doFilter(request, response);

        logger.debug("doFilter(ServletRequest, ServletResponse, FilterChain) - end");
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        servletContext = arg0.getServletContext();
    }

}
