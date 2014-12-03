/******************************************************************************
 * Copyright (c) 2014 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//çÏê¨ì˙: 2014/12/02

package nu.mine.kino.project;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNLogClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class LogChecker {
    
    public void print(String url, String userid, String password, String from,
            String to) throws SVNException, ParseException {
        System.out.printf("[%s]\n", url);
        System.out.printf("[%s]\n", userid);
        System.out.printf("[%s]\n", password);
        System.out.printf("[%s]\n", from);
        System.out.printf("[%s]\n", to);
        init();

        ISVNAuthenticationManager auth = SVNWCUtil
                .createDefaultAuthenticationManager(userid, password);
        SVNLogClient logClient = SVNClientManager.newInstance(
                SVNWCUtil.createDefaultOptions(true), auth).getLogClient();

        SVNURL targetUrl = SVNURL.parseURIEncoded(url);
        ISVNLogEntryHandler handler = new LogEntryHandler();

        SVNRepository repository = DAVRepositoryFactory.create(targetUrl);
        repository.setAuthenticationManager(auth);

        Date startDate = DateUtils.parseDate(from, new String[] { "yyyyMMdd" });
        Date endDate = DateUtils.parseDate(to, new String[] { "yyyyMMdd" });

        logClient.doLog(targetUrl, null, SVNRevision.HEAD,
                SVNRevision.create(startDate), SVNRevision.create(endDate),
                true, true, 50, handler);
    }

    private static class LogEntryHandler implements ISVNLogEntryHandler {

        @Override
        public void handleLogEntry(SVNLogEntry entry) throws SVNException {
            StringBuffer headerBuf = new StringBuffer();
            headerBuf.append(entry.getRevision() + "\t");
            headerBuf.append(parse(entry.getDate()) + "\t");
            headerBuf.append(entry.getAuthor() + "\t");
            String header = new String(headerBuf);
            // System.out.print(arg0.getChangedPaths());
            // System.out.println(entry.getMessage());
            Map<String, SVNLogEntryPath> changedPaths = entry.getChangedPaths();
            Collection<SVNLogEntryPath> values = changedPaths.values();
            for (SVNLogEntryPath path : values) {
                StringBuffer buf = new StringBuffer();
                buf.append(header);
                buf.append(path.getType() + "\t");
                buf.append(path.getPath());
                System.out.println(buf);
            }
        }

        private String parse(Date date) {
            String str = DateFormatUtils.format(date,
                    "yyyy-MM-dd HH:mm:ss");
            return str;
        }

    }

    private void init() {
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        FSRepositoryFactory.setup();
    }

    public static void main(String[] args) throws SVNException, ParseException {
        new LogChecker().print(args[0], args[1], args[2], args[3], args[4]);
        // new LogChecker().getLog(null,null,null,null,null);
    }

}
