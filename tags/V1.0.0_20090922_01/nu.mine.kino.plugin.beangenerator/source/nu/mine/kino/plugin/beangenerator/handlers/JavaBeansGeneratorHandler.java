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

import java.lang.reflect.InvocationTargetException;

import nu.mine.kino.plugin.beangenerator.Activator;
import nu.mine.kino.plugin.beangenerator.Messages;
import nu.mine.kino.plugin.beangenerator.actions.JavaBeansCreatorWithProgress;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class JavaBeansGeneratorHandler extends AbstractHandler {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(JavaBeansGeneratorHandler.class);

    public Object execute(ExecutionEvent event) throws ExecutionException {
        try {
            String name = event.getCommand().getName();
            Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                    .getShell();

            if (!MessageDialog
                    .openConfirm(
                            shell,
                            Messages.JavaBeansGeneratorAction_MSG_DIALOG_MESSAGE,
                            name
                                    + Messages.JavaBeansGeneratorAction_MSG_DIALOG_DESC)) {
                return null;
            }

            /* 実行中のダイアログ表示 */
            ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
            dialog.setCancelable(true);

            IWorkbenchPartSite site = HandlerUtil.getActivePartChecked(event)
                    .getSite();
            ISelection selection = HandlerUtil.getCurrentSelection(event);
            IStructuredSelection ss = null;
            if (selection instanceof IStructuredSelection) {
                ss = (IStructuredSelection) selection;
            }

            try {
                logger.debug("execute"); //$NON-NLS-1$
                String id = event.getCommand().getId();
                JavaBeansCreatorWithProgress progress = new JavaBeansCreatorWithProgress(
                        ss, site, id);
                dialog.run(true, true, progress);
            } catch (InvocationTargetException e) {
                Activator.logException(e);
            } catch (InterruptedException e) {
                Activator.logException(e, false);
            }
        } catch (NotDefinedException e) {
            Activator.logException(e);
        }
        return null;
    }

}
