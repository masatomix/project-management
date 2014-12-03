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
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.tmatesoft.svn.core.SVNException;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class LogCheckerMain {

    @Option(name = "-U", aliases = { "--url" }, metaVar = "url", required = true, usage = "URL")
    private static String url;

    @Option(name = "-u", metaVar = "userid", usage = "userid")
    private static String userid;

    @Option(name = "-p", metaVar = "password", usage = "password")
    private static String password;

    @Option(name = "-f", metaVar = "from", required = true, usage = "from")
    private static String from;

    @Option(name = "-t", metaVar = "to", required = true, usage = "to")
    private static String to;

    // receives other command line parameters than options
    @Argument
    private List<String> arguments = new ArrayList<String>();

    public static void main(String[] args) throws SVNException, ParseException {
        LogCheckerMain main = new LogCheckerMain();
        CmdLineParser parser = new CmdLineParser(main);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.out.println("usage:");
            parser.printSingleLineUsage(System.out);
            System.out.println();
            parser.printUsage(System.out);
            return;
        }

        new LogChecker().print(url, userid, password, from, to);
    }
}
