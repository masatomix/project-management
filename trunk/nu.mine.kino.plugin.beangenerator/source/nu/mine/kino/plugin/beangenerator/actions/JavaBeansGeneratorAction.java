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
//作成日: 2008/08/15
package nu.mine.kino.plugin.beangenerator.actions;

import java.lang.reflect.InvocationTargetException;

import nu.mine.kino.plugin.beangenerator.Activator;
import nu.mine.kino.plugin.beangenerator.Messages;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
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
public class JavaBeansGeneratorAction implements IObjectActionDelegate {
    private static final Logger logger = Logger
            .getLogger(JavaBeansGeneratorAction.class);

    private IStructuredSelection ss;

    private IWorkbenchSite site;

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        this.site = targetPart.getSite();
    }

    public void run(IAction action) {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getShell();

        if (!MessageDialog.openConfirm(shell,
                Messages.JavaBeansGeneratorAction_MSG_DIALOG_MESSAGE, action
                        .getText()
                        + Messages.JavaBeansGeneratorAction_MSG_DIALOG_DESC)) {
            return;
        }

        /* 実行中のダイアログ表示 */
        ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
        dialog.setCancelable(true);

        try {
            logger.debug("execute"); //$NON-NLS-1$
            JavaBeansCreatorWithProgress progress = new JavaBeansCreatorWithProgress(
                    ss, site, action.getId());
            dialog.run(true, true, progress);
        } catch (InvocationTargetException e) {
            Activator.logException(e);
        } catch (InterruptedException e) {
            Activator.logException(e, false);
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
        logger.debug("selectionChanged(IAction, ISelection) - start"); //$NON-NLS-1$
        ss = (IStructuredSelection) selection;
        logger.debug("selectionChanged(IAction, ISelection) - end"); //$NON-NLS-1$
    }

}
