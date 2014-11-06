/******************************************************************************
 * Copyright (c) 2008 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//作成日: 2008/08/24
package nu.mine.kino.plugin.projectmanagement.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import nu.mine.kino.plugin.projectmanagement.Activator;
import nu.mine.kino.plugin.projectmanagement.Messages;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PlatformUI;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class TemplateCreateAction implements IObjectActionDelegate {
    private static final String XLS1 = "JavaBeansSample.xls"; //$NON-NLS-1$

    private static final String XLS2 = "JavaBeansSampleAnno.xls"; //$NON-NLS-1$

    private static final String[] XLSs = { "JavaBeansSample.xls",
            "JavaBeansSampleAnno.xls" };

    private IStructuredSelection ss;

    private IWorkbenchSite site;

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        this.site = targetPart.getSite();
    }

    public void run(IAction action) {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getShell();

        if (!MessageDialog.openConfirm(shell,
                Messages.TemplateCreateAction_MESSAGE_DIALOG,
                Messages.TemplateCreateAction_MESSAGE_CONFIRM + XLS1
                        + ",  " + XLS2)) { //$NON-NLS-2$
            return;
        }

        /* 実行中のダイアログ表示 */
        ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
        dialog.setCancelable(true);

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

    public void selectionChanged(IAction action, ISelection selection) {
        ss = (IStructuredSelection) selection;
    }

}
