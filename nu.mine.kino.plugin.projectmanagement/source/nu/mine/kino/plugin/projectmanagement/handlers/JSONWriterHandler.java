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
//作成日: 2014/11/11

package nu.mine.kino.plugin.projectmanagement.handlers;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import nu.mine.kino.plugin.projectmanagement.Activator;
import nu.mine.kino.projects.ProjectException;
import nu.mine.kino.projects.ProjectWriter;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class JSONWriterHandler extends AbstractExecutorHandler {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(JSONWriterHandler.class);

    @Override
    public IRunnableWithProgress getRunnableWithProgress(
            IWorkbenchPartSite site, final IStructuredSelection ss, String id) {

        return new IRunnableWithProgress() {
            public void run(IProgressMonitor monitor)
                    throws InvocationTargetException, InterruptedException {
                logger.debug("run(IProgressMonitor) - start");

                Iterator<IFile> iterator = ss.iterator();
                // 選択されたExcelファイル数分、繰り返し。
                while (iterator.hasNext()) {
                    IFile iFile = iterator.next();

                    File parent = iFile.getParent().getLocation().toFile();
                    File target = iFile.getLocation().toFile();

                    logger.debug("run(IProgressMonitor)"
                            + target.getAbsolutePath());
                    logger.debug("run(IProgressMonitor)"
                            + parent.getAbsolutePath());
                    try {
                        File jsonFile = ProjectWriter.write(target);
                    } catch (ProjectException e) {
                        logger.error("run(IProgressMonitor)", e);

                        Activator.logException(e, false);
                        throw new InvocationTargetException(e);
                    }

                }

                logger.debug("run(IProgressMonitor) - end");
            }
        };
    }
}
