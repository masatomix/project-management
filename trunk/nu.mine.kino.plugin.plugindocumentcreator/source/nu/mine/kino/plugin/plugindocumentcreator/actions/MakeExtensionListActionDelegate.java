/*******************************************************************************
 * Copyright (c) 2006 Masatomi KINO. All rights reserved. $Id: MakeExtensionListActionDelegate.java,v 1.1 2006/10/25 16:40:49 cvsuser Exp $
 ******************************************************************************/
// 作成日: 2006/10/22
package nu.mine.kino.plugin.plugindocumentcreator.actions;

import nu.mine.kino.plugin.plugindocumentcreator.wizards.MakeExtensionListWizard;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

public class MakeExtensionListActionDelegate implements
        IWorkbenchWindowActionDelegate {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(MakeExtensionListActionDelegate.class);

    public void dispose() {
        // TODO 自動生成されたメソッド・スタブ

    }

    public void init(IWorkbenchWindow window) {
        // TODO 自動生成されたメソッド・スタブ

    }

    public void run(IAction action) {
        logger.debug("run(IAction) - start");

        MakeExtensionListWizard wizard = new MakeExtensionListWizard();
        IWorkbench workbench = PlatformUI.getWorkbench();
        wizard.init(workbench, new StructuredSelection());

        Shell activeShell = workbench.getDisplay().getActiveShell();
        WizardDialog dialog = new WizardDialog(activeShell, wizard);
        dialog.create();
        // dialog.getShell().setText(wizard.getWindowTitle());
        int result = dialog.open();
        // notifyResult(result == WizardDialog.OK);

        logger.debug("run(IAction) - end");
    }

    public void selectionChanged(IAction action, ISelection selection) {
        // TODO 自動生成されたメソッド・スタブ

    }

}
