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
//çÏê¨ì˙: 2012/06/03

package nu.mine.kino.plugin.webrecorder;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import nu.mine.kino.plugin.webrecorder.filters.PlayFilter;
import nu.mine.kino.plugin.webrecorder.filters.MultiReadFilter;
import nu.mine.kino.plugin.webrecorder.servlets.RecorderServlet;

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

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(port);
        server.setConnectors(new Connector[] { connector });

        ServletContextHandler context = new ServletContextHandler(
                ServletContextHandler.SESSIONS);
        server.setHandler(context);

        FilterHolder multiReadFilterHolder = new FilterHolder();
        multiReadFilterHolder.setFilter(new MultiReadFilter());
        context.addFilter(multiReadFilterHolder, "/*",
                EnumSet.of(DispatcherType.REQUEST));

        switch (mode) {
        case RECORD:
            context.addServlet(RecorderServlet.class, "/*");
            break;
        case PLAY:
            context.addServlet(ProxyServlet.class, "/*");
            FilterHolder playFilterHolder = new FilterHolder();
            playFilterHolder.setFilter(new PlayFilter());
            context.addFilter(playFilterHolder, "/*",
                    EnumSet.of(DispatcherType.REQUEST));
            break;
        default:
            break;
        }

        server.start();
        server.join();
    }
}
