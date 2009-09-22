/******************************************************************************
 * Copyright (c) 2009 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//作成日: 2009/05/06
package nu.mine.kino.plugin.beangenerator.handlers;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import nu.mine.kino.plugin.beangenerator.Activator;
import nu.mine.kino.plugin.beangenerator.Messages;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class TemplateCreateHandler extends AbstractHandler implements IHandler {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(TemplateCreateHandler.class);

    private static final String XLS1 = "JavaBeansSample.xls"; //$NON-NLS-1$

    private static final String XLS2 = "JavaBeansSampleAnno.xls"; //$NON-NLS-1$

    private static final String[] XLSs = { "JavaBeansSample.xls",
            "JavaBeansSampleAnno.xls" };

    public Object execute(ExecutionEvent event) throws ExecutionException {

        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getShell();

        if (!MessageDialog.openConfirm(shell,
                Messages.TemplateCreateAction_MESSAGE_DIALOG,
                Messages.TemplateCreateAction_MESSAGE_CONFIRM + XLS1
                        + ",  " + XLS2)) { //$NON-NLS-2$
            return null;
        }

        /* 実行中のダイアログ表示 */
        ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
        dialog.setCancelable(true);

        ISelection selection = HandlerUtil.getCurrentSelection(event);
        IStructuredSelection ss = null;
        if (selection instanceof IStructuredSelection) {
            ss = (IStructuredSelection) selection;
        }

        try {
            IRunnableWithProgress progress = null;
            Object firstElementObj = ss.getFirstElement();
            if (firstElementObj instanceof IResource) {
                final IResource firstElement = (IResource) ss.getFirstElement();
                progress = new IRunnableWithProgress() {
                    public void run(IProgressMonitor monitor)
                            throws InvocationTargetException,
                            InterruptedException {
                        IProject project = firstElement.getProject();
                        createFile(project, XLS1, monitor);
                        createFile(project, XLS2, monitor);
                    }
                };
            } else if (firstElementObj instanceof IJavaProject) {
                final IJavaProject firstElement = (IJavaProject) ss
                        .getFirstElement();
                progress = new IRunnableWithProgress() {
                    public void run(IProgressMonitor monitor)
                            throws InvocationTargetException,
                            InterruptedException {
                        IProject project = firstElement.getProject();
                        // createFile(project, XLS1, monitor);
                        // createFile(project, XLS2, monitor);
                        createFiles(project, monitor, XLSs);
                    }
                };

            }

            dialog.run(true, true, progress);
        } catch (InvocationTargetException e) {
            Activator.logException(e);
        } catch (InterruptedException e) {
            Activator.logException(e, false);
        }

        return null;
    }

    private void createFiles(IProject project, IProgressMonitor monitor,
            String... fileNames) throws InvocationTargetException {
        for (String filename : fileNames) {
            createFile(project, filename, monitor);
        }
    }

    private void createFile(IProject project, String fileName,
            IProgressMonitor monitor) throws InvocationTargetException {
        // ファイル作成先へのポインタ取得
        IFile destFile = project.getFile(new Path(fileName));
        try {
            if (destFile.exists()) {
                destFile.delete(false, null);
            }
            URL entry = Activator.getDefault().getBundle().getEntry("/"); //$NON-NLS-1$
            String pluginDirectory = FileLocator.resolve(entry).getPath();
            File sourceFile = new File(pluginDirectory, fileName);
            destFile.create(new FileInputStream(sourceFile), true, monitor);
        } catch (CoreException e) {
            throw new InvocationTargetException(e);
        } catch (FileNotFoundException e) {
            throw new InvocationTargetException(e);
        } catch (IOException e) {
            throw new InvocationTargetException(e);
        }
    }

}
