/*******************************************************************************
 * Copyright (c) 2006 Masatomi KINO.
 * All rights reserved. 
 * $Id$
 *******************************************************************************/
//作成日: 2007/06/12
package nu.mine.kino.plugin.configsample.actions;

import java.io.File;

import nu.mine.kino.plugin.configsample.Activator;
import nu.mine.kino.plugin.configsample.UpdateUIMessages;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.update.configuration.IConfiguredSite;
import org.eclipse.update.configuration.IInstallConfiguration;
import org.eclipse.update.core.SiteManager;
import org.eclipse.update.operations.OperationsManager;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class NewExtensionLocationAction extends Action {
    private Shell shell;

    public NewExtensionLocationAction(Shell shell, String text,
            ImageDescriptor desc) {
        super(text, desc);
        this.shell = shell;
    }

    public void run() {

        IStatus status = OperationsManager.getValidator()
                .validatePlatformConfigValid();
        if (status != null) {
            ErrorDialog.openError(shell, null, null, status);
            return;
        }

        DirectoryDialog dialog = new DirectoryDialog(shell,
                SWT.APPLICATION_MODAL);
        dialog
                .setMessage(UpdateUIMessages.NewExtensionLocationAction_selectExtLocation);

        String dir = dialog.open();
        while (dir != null) {
            File dirFile = getExtensionSite(new File(dir));
            if (dirFile != null) {
                if (addExtensionLocation(dirFile))
                    return;
                else {
                    // re-open the directory dialog
                    dialog.setFilterPath(dir);
                    dir = dialog.open();
                }
            } else {
                MessageDialog
                        .openInformation(
                                shell,
                                UpdateUIMessages.NewExtensionLocationAction_extInfoTitle,
                                UpdateUIMessages.NewExtensionLocationAction_extInfoMessage);
                // re-open the directory dialog
                dialog.setFilterPath(dir);
                dir = dialog.open();
            }
        }
    }

    /**
     * 指定されたディレクトリがeclipseというディレクトリだったら、親ディレクトリ取得して再帰処理します。
     * eclipseというディレクトリじゃなかったら、中にeclipseというディレクトリがあるか調べます。
     * あったら更にその中に.eclipseextensionというファイルがあるか調べます。あればそのディレクトリを返します。
     * テストコミット。
     * @param directory
     * @return the site file (including "eclipse" path) when directory is an
     *         eclipse exstension, null otherwise
     */
    static File getExtensionSite(File directory) {
        // Check the eclipse folder
        if (directory.getName().equals("eclipse")) { //$NON-NLS-1$
            // if we picked up the eclipse directory, check if its parent is a
            // site
            File site = getExtensionSite(directory.getParentFile());
            if (site != null)
                return directory;
            // otherwise, fall through
        }

        File eclipse = new File(directory, "eclipse"); //$NON-NLS-1$
        if (!eclipse.exists() || !eclipse.isDirectory())
            return null;

        // check the marker
        File marker = new File(eclipse, ".eclipseextension"); //$NON-NLS-1$
        if (!marker.exists() || marker.isDirectory())
            return null;
        return eclipse;
    }

    private boolean addExtensionLocation(File dir) {
        try {
            IInstallConfiguration config = SiteManager.getLocalSite()
                    .getCurrentConfiguration();
            IConfiguredSite csite = config.createLinkedConfiguredSite(dir);
            csite.verifyUpdatableStatus();
            config.addConfiguredSite(csite);
            boolean restartNeeded = SiteManager.getLocalSite().save();
            Activator.requestRestart(restartNeeded);
            return true;
        } catch (CoreException e) {
            String title = UpdateUIMessages.InstallWizard_TargetPage_location_error_title;
            ErrorDialog.openError(shell, title, null, e.getStatus());
            Activator.logException(e, false);
            return false;
        }
    }

}
