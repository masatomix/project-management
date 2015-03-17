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
import nu.mine.kino.projects.utils.ProjectUtils;

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

                    logger.debug("run(IProgressMonitor)"
                            + target.getAbsolutePath());
                    logger.debug("run(IProgressMonitor)"
                            + parent.getAbsolutePath());

                    File jsonFile = null;
                    // 今日分について。ExcelからJSON。JSONからPV
                    try {
                        jsonFile = ProjectWriter.write(target);
                        PVCreator.createFromJSON(jsonFile);
                        PVCreator.createForPivotFromJSON(jsonFile);
                    } catch (ProjectException e) {
                        logger.error("run(IProgressMonitor)", e);
                        logger.error("今日分からJSONもしくはそのあとのPVを作成するときにエラー。");
                        Activator.logException(e, false);
                        throw new InvocationTargetException(e);
                    }

                    // Base分と、それとの差分について。
                    // ExcelからJSON。JSONからAC/EV
                    String[] prefixArray = { "base_", "base1_", "base2_" };
                    for (int i = 0; i < prefixArray.length; i++) {
                        File target_base = new File(target.getParentFile(),
                                prefixArray[i] + target.getName());

                        File jsonFile_base = ProjectUtils
                                .findJSONFilePath(target_base);
                        if (target_base.exists() && !jsonFile_base.exists()) { // xlsがあるがjsonはないばあいだけ、作る。
                            try {
                                jsonFile_base = ProjectWriter
                                        .write(target_base);
                            } catch (ProjectException e) {
                                logger.error("run(IProgressMonitor)", e);
                                logger.error("BaseからJSONを作成するときにエラー。");
                                Activator.logException(e, false);
                                throw new InvocationTargetException(e);
                            }
                        }

                        if (jsonFile_base.exists()) {
                            try {
                                // jsonFile_base = ProjectWriter
                                // .write(target_base);
                                ACCreator.createFromJSON(jsonFile,
                                        jsonFile_base, prefixArray[i]);
                                EVCreator.createFromJSON(jsonFile,
                                        jsonFile_base, prefixArray[i]);
                            } catch (ProjectException e) {
                                logger.error("run(IProgressMonitor)", e);
                                logger.error("AC/EVを作成するときにエラー。");
                                Activator.logException(e, false);
                                throw new InvocationTargetException(e);
                            }
                        }
                    }

                }

                logger.debug("run(IProgressMonitor) - end");
            }
        };
    }
}
