package nu.mine.kino.project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNEvent;
import org.tmatesoft.svn.core.wc.SVNEventAction;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

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
public class UpdateChecker {
    public static void main(String[] args) throws RepositoryUpdatedException,
            SVNException {
        new UpdateChecker().throwUpdatedException(args[0], args[1], args[2]);
    }

    public void throwUpdatedException(String path, String userid,
            String password) throws RepositoryUpdatedException, SVNException {
        this.throwUpdatedException(new File(path), userid, password);
    }

    public void throwUpdatedException(File file, String userid, String password)
            throws RepositoryUpdatedException, SVNException {

        final List<File> files = new ArrayList<File>();
        SVNUpdateClient updateclient = createSVNClientManager(userid, password)
                .getUpdateClient();

        ISVNEventHandler updateEventHandler = new ISVNEventHandler() {

            @Override
            public void handleEvent(SVNEvent svnevent, double d)
                    throws SVNException {
                System.out.println("----------");
                SVNEventAction action = svnevent.getAction();
                String name = svnevent.getFile().getName();
                System.out.println(name);
                System.out.println(action);
                System.out.println("----------");

                if (action.equals(SVNEventAction.UPDATE_UPDATE)
                        || action.equals(SVNEventAction.UPDATE_ADD)
                        || action.equals(SVNEventAction.UPDATE_DELETE)) {
                    files.add(svnevent.getFile());
                }
            }

            @Override
            public void checkCancelled() throws SVNCancelException {
            }

        };
        updateclient.setEventHandler(updateEventHandler);

        long doUpdate = updateclient.doUpdate(file, SVNRevision.HEAD,
                SVNDepth.fromRecurse(true), false, false);

        print(files);

        if (!files.isEmpty()) {
            RepositoryUpdatedException updatedException = new RepositoryUpdatedException();
            updatedException.setFiles(files);
            throw updatedException;
        }

    }

    private void print(List<File> files) {
        for (File file : files) {
            System.out.println(file.getAbsolutePath());
        }
    }

    private SVNClientManager createSVNClientManager(String userName,
            String password) {
        return SVNClientManager.newInstance(
                SVNWCUtil.createDefaultOptions(true), userName, password);
    }

}
