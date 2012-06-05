/******************************************************************************
 * Copyright (c) 2008 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//çÏê¨ì˙: 2012/06/03

package nu.mine.kino.plugin.webrecorder;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import nu.mine.kino.plugin.webrecorder.filters.PlayFilter;
import nu.mine.kino.plugin.webrecorder.servlets.MyProxyServlet;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.ProxyServlet;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class Utils {
    // public static void main(String[] args) throws Exception {
    //
    // Server server = new Server();
    // SelectChannelConnector connector = new SelectChannelConnector();
    // connector.setPort(8008);
    // server.setConnectors(new Connector[] { connector });
    //
    // ServletContextHandler context = new ServletContextHandler(
    // ServletContextHandler.SESSIONS);
    // server.setHandler(context);
    // // context.addServlet(new ServletHolder(new HelloServlet()),
    // // "/hello");
    // context.addServlet(ProxyServlet.class, "/*");
    //
    // server.start();
    // server.join();
    //
    // }

    public static void startJettyServer(Server server, int port, RecordMode mode)
            throws InterruptedException, Exception {
        // SelectChannelConnector connector = new SelectChannelConnector();
        // connector.setPort(port);
        // server.setConnectors(new Connector[] { connector });
        //
        // ContextHandler contextHandler = new ContextHandler("/");
        //
        // ServletHandler servletHandler = new ServletHandler();
        // servletHandler.addServletWithMapping(ProxyServlet.class, "/*");
        //
        // // filter
        // FilterHolder filterHolder = new FilterHolder();
        // Filter filter = null;
        // switch (mode) {
        // case RECORD:
        // filter = new RecordFilter();
        // break;
        // case PLAY:
        // filter = new PlayFilter();
        // break;
        // default:
        // break;
        // }
        // filterHolder.setFilter(filter);
        // servletHandler
        // .addFilterWithMapping(filterHolder, "/*", Handler.DEFAULT);
        //
        // contextHandler.addHandler(servletHandler);
        // server.addHandler(contextHandler);
        //
        // server.start();
        // server.join();

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(port);
        server.setConnectors(new Connector[] { connector });

        ServletContextHandler context = new ServletContextHandler(
                ServletContextHandler.SESSIONS);
        server.setHandler(context);

        // filter
        FilterHolder filterHolder = new FilterHolder();
        Filter filter = null;
        switch (mode) {
        case RECORD:
            context.addServlet(MyProxyServlet.class, "/*");
            // filter = new RecordFilter();
            // filterHolder.setFilter(filter);
            // context.addFilter(filterHolder, "/*",
            // EnumSet.of(DispatcherType.REQUEST));
            break;
        case PLAY:
            context.addServlet(ProxyServlet.class, "/*");
            filter = new PlayFilter();
            filterHolder.setFilter(filter);
            context.addFilter(filterHolder, "/*",
                    EnumSet.of(DispatcherType.REQUEST));
            break;
        default:
            break;
        }

        server.start();
        server.join();
    }
}
