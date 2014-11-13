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

import org.apache.log4j.Logger;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import nu.mine.kino.plugin.projectmanagement.Activator;
import nu.mine.kino.projects.ACCreator;
import nu.mine.kino.projects.EVCreator;
import nu.mine.kino.projects.PVCreator;
import nu.mine.kino.projects.ProjectException;
import nu.mine.kino.projects.ProjectWriter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class ProjectWriterHandler extends AbstractExecutorHandler {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(ProjectWriterHandler.class);

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

                    File target_base = new File(target.getParentFile(), "base_"
                            + target.getName());

                    logger.debug("run(IProgressMonitor)"
                            + target.getAbsolutePath());
                    logger.debug("run(IProgressMonitor)"
                            + parent.getAbsolutePath());
                    try {
                        File jsonFile = ProjectWriter.write(target);
                        PVCreator.createFromJSON(jsonFile);
                        
                        File jsonFile_base = ProjectWriter.write(target_base);
                        ACCreator.createFromJSON(jsonFile, jsonFile_base);
                        EVCreator.createFromJSON(jsonFile, jsonFile_base);

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
