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
//çÏê¨ì˙: 2009/05/01
package nu.mine.kino.plugin.plugindocumentcreator.handlers;

import nu.mine.kino.plugin.plugindocumentcreator.wizards.MakeExtensionListWizard;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class MakeExtensionListHandler extends AbstractHandler {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(MakeExtensionListHandler.class);

    public Object execute(ExecutionEvent event) throws ExecutionException {
        logger.debug("execute(ExecutionEvent) - start");

        MakeExtensionListWizard wizard = new MakeExtensionListWizard();
        IWorkbench workbench = PlatformUI.getWorkbench();
        wizard.init(workbench, new StructuredSelection());

        Shell activeShell = workbench.getDisplay().getActiveShell();
        WizardDialog dialog = new WizardDialog(activeShell, wizard);
        dialog.create();
        // dialog.getShell().setText(wizard.getWindowTitle());
        int result = dialog.open();
        // notifyResult(result == WizardDialog.OK);

        logger.debug("execute(ExecutionEvent) - end");
        return null;
    }

}
