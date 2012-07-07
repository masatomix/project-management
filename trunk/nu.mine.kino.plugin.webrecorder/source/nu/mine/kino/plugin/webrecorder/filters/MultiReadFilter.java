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

import org.apache.log4j.Logger;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 何度もリクエストのBodyの読み出しができる {@link MultiReadHttpServletRequest} へ切り替える 為のFilter。
 * 
 * @author Masatomi KINO
 * @version $Revision$
 * @see MultiReadHttpServletRequest
 */
public class MultiReadFilter implements Filter {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(MultiReadFilter.class);

    private ServletContext servletContext;

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        logger.debug("doFilter(ServletRequest, ServletResponse, FilterChain) - start");

        chain.doFilter(new MultiReadHttpServletRequest(
                (HttpServletRequest) request), response);

        logger.debug("doFilter(ServletRequest, ServletResponse, FilterChain) - end");
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        servletContext = config.getServletContext();
    }

}
