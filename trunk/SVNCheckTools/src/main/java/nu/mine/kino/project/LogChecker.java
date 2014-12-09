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
//作成日: 2014/12/02

package nu.mine.kino.project;

import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
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

import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.BeanToCsv;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class LogChecker {

    private void internalPrint(String url, String userid, String password,
            String from, String to, ISVNLogEntryHandler handler)
            throws SVNException, ParseException {

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

        SVNRepository repository = DAVRepositoryFactory.create(targetUrl);
        repository.setAuthenticationManager(auth);

        Date startDate = DateUtils.parseDate(from, new String[] { "yyyyMMdd" });
        Date endDate = DateUtils.parseDate(to, new String[] { "yyyyMMdd" });

        logClient.doLog(targetUrl, null, SVNRevision.HEAD,
                SVNRevision.create(startDate), SVNRevision.create(endDate),
                true, true, 50, handler);
    }

    public void print(String url, String userid, String password, String from,
            String to, String... format) throws SVNException, ParseException {
        ISVNLogEntryHandler handler = null;
        if (format == null) {
            handler = new LogEntryHandler();
        } else {
            handler = new LogEntryHandler2(format);
        }
        internalPrint(url, userid, password, from, to, handler);
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
            String str = DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
            return str;
        }

    }

    private static class LogEntryHandler2 implements ISVNLogEntryHandler {

        private final String[] format;

        public LogEntryHandler2(String[] format) {
            this.format = format;
        }

        @Override
        public void handleLogEntry(SVNLogEntry entry) throws SVNException {
            ColumnPositionMappingStrategy<SVNLogBean> strat = new ColumnPositionMappingStrategy<SVNLogBean>();
            strat.setType(SVNLogBean.class);
            // CSVの順番で、どのフィールドにマッピングすればいいかを指定する。
            // String[] columns = new String[] { "type", "path", "revision" };//
            // フィールド名
            strat.setColumnMapping(format);

            List<SVNLogBean> list = new ArrayList<SVNLogBean>();
            long revision = entry.getRevision();
            String date = parse(entry.getDate());
            String author = entry.getAuthor();

            // System.out.print(arg0.getChangedPaths());
            // System.out.println(entry.getMessage());
            Map<String, SVNLogEntryPath> changedPaths = entry.getChangedPaths();
            Collection<SVNLogEntryPath> values = changedPaths.values();
            for (SVNLogEntryPath entryPath : values) {
                char type = entryPath.getType();
                String path = entryPath.getPath();
                SVNLogBean bean = new SVNLogBean();
                bean.setType(type);
                bean.setPath(path);
                bean.setRevision(revision);
                bean.setDate(date);
                bean.setAuthor(author);
                list.add(bean);
                BeanToCsv csv = new BeanToCsv();
                String[] records = csv.write(strat, bean);
                for (String element : records) {
                    System.out.print(element + "\t");
                }
                System.out.println();

            }
            // List<CSVSampleBean> list = getList();
            // BeanToCsv csv = new BeanToCsv();
            // StringWriter writer = new StringWriter();

            // FileWriter writer = null;
            // try {
            // writer = new FileWriter(new File("hoge.tsv"));
            // } catch (IOException e) {
            // e.printStackTrace();
            // }

            // csv.writeAll(strat, new CSVWriter(writer, '\t',
            // CSVWriter.NO_QUOTE_CHARACTER), list);
            // System.out.println(writer.toString());

            // for (SVNLogBean logbean : list) {
            // System.out.println(logbean);
            // }
        }

        private String parse(Date date) {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            String str = DateFormatUtils.format(date, pattern);
            return str;
        }

    }

    private void init() {
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        FSRepositoryFactory.setup();
    }

    public static void main(String[] args) throws SVNException, ParseException {
        // new LogChecker().print(args[0], args[1], args[2], args[3], args[4]);

        String[] columns = new String[args.length - 5];
        if (args.length > 5) {
            // 配列の index = 5 から、配列の最後までコピー。0番目のところに挿入。
            System.arraycopy(args, 5, columns, 0, args.length - 5); // (3)
            // String[] columns = new String[] { "type", "path", "revision" };//

            for (int i = 0; i < columns.length; i++) { // (4)
                System.out.println(columns[i]);
            }
            new LogChecker().print(args[0], args[1], args[2], args[3], args[4],
                    columns);
        } else {
            new LogChecker().print(args[0], args[1], args[2], args[3], args[4]);

        }
    }
}
