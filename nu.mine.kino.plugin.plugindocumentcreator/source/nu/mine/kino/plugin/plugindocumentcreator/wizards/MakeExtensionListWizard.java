/*******************************************************************************
 * Copyright (c) 2007 Masatomi KINO. All rights reserved. 
 * $Id$
 ******************************************************************************/
// 作成日: 2006/10/22s
package nu.mine.kino.plugin.plugindocumentcreator.wizards;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;

import nu.mine.kino.plugin.plugindocumentcreator.Activator;
import nu.mine.kino.plugin.plugindocumentcreator.ExtensionPointWriter;
import nu.mine.kino.plugin.plugindocumentcreator.IExtensionPointWriter;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class MakeExtensionListWizard extends Wizard implements INewWizard {

    private MakeListSelectionWizardPage page;

    public MakeExtensionListWizard() {
        page = new MakeListSelectionWizardPage();
        setNeedsProgressMonitor(true);
    }

    public boolean performFinish() {
        final String fileName = page.getPath();
        final String extensionPointName = page.getExtensionPointId();

        try {
            getContainer().run(true, false, new IRunnableWithProgress() {
                public void run(IProgressMonitor monitor)
                        throws InvocationTargetException, InterruptedException {
                    doFinish(fileName, extensionPointName, monitor);
                }
            });
        } catch (InterruptedException e) {
            Activator.logException(e, false);
            return false;
        } catch (InvocationTargetException e) {
            Activator.logException(e, true);
            return false;
        }
        return true;
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        // TODO 自動生成されたメソッド・スタブ

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    public void addPages() {
        super.addPages();
        addPage(page);
    }

    private void doFinish(String fileName, String extensionPointName,
            IProgressMonitor monitor) throws InterruptedException,
            InvocationTargetException {
        // create a sample file
        monitor.beginTask("Creating " + fileName, 10);
        try {
            // ここは拡張ポイントからCSVやWiki、XMLなどのレイアウタを指定しよう。
            IExtensionPointWriter writer = new ExtensionPointWriter(new File(
                    fileName));
            writer.write(extensionPointName);
            monitor.worked(10);
        } catch (FileNotFoundException e) {
            throw new InvocationTargetException(e);
        }
    }
}
