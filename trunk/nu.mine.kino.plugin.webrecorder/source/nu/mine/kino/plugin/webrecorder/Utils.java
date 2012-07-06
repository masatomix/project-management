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

package nu.mine.kino.plugin.webrecorder;

import static javax.servlet.DispatcherType.REQUEST;

import java.io.File;
import java.util.EnumSet;

import nu.mine.kino.plugin.commons.utils.StringUtils;
import nu.mine.kino.plugin.webrecorder.filters.AcceptEncodingRemoveFilter;
import nu.mine.kino.plugin.webrecorder.filters.LogFilter;
import nu.mine.kino.plugin.webrecorder.filters.MultiReadFilter;
import nu.mine.kino.plugin.webrecorder.filters.PlayFilter;
import nu.mine.kino.plugin.webrecorder.filters.RecordFilter;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.ProxyServlet;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class Utils {

    private static final Logger logger = Logger.getLogger(Utils.class);

    public static void startJettyServer(Server server, int port, RecordMode mode)
            throws InterruptedException, Exception {

        String cacheBasepath = WebRecorderPlugin.getDefault()
                .getCacheBasepath();
        logger.debug("キャッシュディレクトリ: " + cacheBasepath);
        new File(cacheBasepath).mkdirs();

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(port);
        server.setConnectors(new Connector[] { connector });

        ServletContextHandler context = new ServletContextHandler(
                ServletContextHandler.SESSIONS);
        server.setHandler(context);

        context.addFilter(MultiReadFilter.class, "/*", EnumSet.of(REQUEST));
        context.addFilter(LogFilter.class, "/*", EnumSet.of(REQUEST));

        switch (mode) {
        case RECORD:
            context.addServlet(ProxyServlet.class, "/*");
            context.addFilter(AcceptEncodingRemoveFilter.class, "/*",
                    EnumSet.of(REQUEST));
            context.addFilter(RecordFilter.class, "/*",
                    EnumSet.of(REQUEST));
            break;
        case PLAY:
            context.addServlet(ProxyServlet.class, "/*");
            context.addFilter(PlayFilter.class, "/*", EnumSet.of(REQUEST));
            break;
        case PROXY_ONLY:
            context.addServlet(ProxyServlet.class, "/*");
            break;
        default:
            break;
        }

        server.start();
        WebRecorderPlugin.getDefault().printConsole(mode + " が起動しました");
        server.join();
        WebRecorderPlugin.getDefault().printConsole(mode + " が停止しました");
    }

    /**
     * sourceの最後の/のあとに、prefixを追加して返す
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
