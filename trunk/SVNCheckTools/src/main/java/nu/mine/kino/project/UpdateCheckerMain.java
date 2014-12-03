package nu.mine.kino.project;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.tmatesoft.svn.core.SVNException;

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
//çÏê¨ì˙: 2014/12/01

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class UpdateCheckerMain {

    @Option(name = "-p", aliases = { "--path" }, metaVar = "path", required = true, usage = "PATH")
    private static String path;

    @Option(name = "-u", metaVar = "userid", usage = "userid")
    private static String userid;

    @Option(name = "-p", metaVar = "password", usage = "password")
    private static String password;

    public static void main(String[] args) throws RepositoryUpdatedException,
            SVNException {
        UpdateCheckerMain main = new UpdateCheckerMain();
        CmdLineParser parser = new CmdLineParser(main);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.out.println("usage:");
            parser.printSingleLineUsage(System.out);
            System.out.println();
            // parser.printUsage(System.out);
            return;
        }

        new UpdateChecker().throwUpdatedException(path, userid, password);
    }
}
