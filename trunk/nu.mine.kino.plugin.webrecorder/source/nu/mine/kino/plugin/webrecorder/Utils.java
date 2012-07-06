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

import nu.mine.kino.plugin.commons.utils.StringUtils;
import nu.mine.kino.plugin.webrecorder.filters.LogFilter;
import nu.mine.kino.plugin.webrecorder.filters.MultiReadFilter;
import nu.mine.kino.plugin.webrecorder.filters.PlayFilter;
import nu.mine.kino.plugin.webrecorder.filters.ResponseCaptureFilter;
import nu.mine.kino.plugin.webrecorder.servlets.RecorderServlet;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.GzipFilter;
import org.eclipse.jetty.servlets.ProxyServlet;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class Utils {

    private static final Logger logger = Logger.getLogger(Utils.class);

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
            FilterHolder recordFilterHolder = new FilterHolder();
            recordFilterHolder.setFilter(new LogFilter());
            recordFilterHolder.setFilter(new ResponseCaptureFilter());
            context.addFilter(recordFilterHolder, "/*",
                    EnumSet.of(DispatcherType.REQUEST));
            break;
        case PLAY:
            context.addServlet(ProxyServlet.class, "/*");
            FilterHolder playFilterHolder = new FilterHolder();
            playFilterHolder.setFilter(new LogFilter());
            playFilterHolder.setFilter(new PlayFilter());
            context.addFilter(playFilterHolder, "/*",
                    EnumSet.of(DispatcherType.REQUEST));
            break;
        case PROXY_ONLY:
            FilterHolder proxyOnlyFilterHolder = new FilterHolder();
            proxyOnlyFilterHolder.setFilter(new LogFilter());
            context.addFilter(proxyOnlyFilterHolder, "/*",
                    EnumSet.of(DispatcherType.REQUEST));
            context.addServlet(ProxyServlet.class, "/*");
            break;

        default:
            break;
        }

        server.start();
        WebRecorderPlugin.getDefault().printConsole(mode + " Ç™ãNìÆÇµÇ‹ÇµÇΩ");
        server.join();
        WebRecorderPlugin.getDefault().printConsole(mode + " Ç™í‚é~ÇµÇ‹ÇµÇΩ");
    }

    /**
     * sourceÇÃç≈å„ÇÃ/ÇÃÇ†Ç∆Ç…ÅAprefixÇí«â¡ÇµÇƒï‘Ç∑
     * 
     * @param source
     * @param prefix
     * @return
     */
    public static String appendPrefix(String source, String prefix) {
        if (StringUtils.isEmpty(source)) {
            return prefix;
        }
        int lastIndexOf = source.lastIndexOf("/");
        if (lastIndexOf < 0) {
            return prefix + source;
        }

        String mae = source.substring(0, lastIndexOf);
        String ushiro = source.substring(lastIndexOf + 1, source.length());
        // System.out.println(mae);
        // System.out.println(ushiro);
        StringBuffer buf = new StringBuffer();
        buf.append(mae);
        buf.append("/");
        buf.append(prefix);
        buf.append(ushiro);
        return new String(buf);

    }

}
