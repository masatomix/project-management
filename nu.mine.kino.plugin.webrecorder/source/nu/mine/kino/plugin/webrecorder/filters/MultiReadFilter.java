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
//çÏê¨ì˙: 2012/06/07

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

/**
 * âΩìxÇ‡BodyÇÃì«Ç›èoÇµÇ™Ç≈Ç´ÇÈÇÊÇ§Ç…Ç∑ÇÈà◊ÇæÇØÇÃFilterÅB
 * 
 * @author Masatomi KINO
 * @version $Revision$
 */
public class MultiReadFilter implements Filter {
    private ServletContext servletContext;

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new MultiReadHttpServletRequest(
                (HttpServletRequest) request), response);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        servletContext = config.getServletContext();
    }

}
