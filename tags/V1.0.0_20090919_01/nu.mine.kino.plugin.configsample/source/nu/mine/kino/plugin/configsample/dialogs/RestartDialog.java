/*******************************************************************************
 * Copyright (c) 2006 Masatomi KINO.
 * All rights reserved. 
 * $Id$
 *******************************************************************************/
//作成日: 2007/06/12
package nu.mine.kino.plugin.configsample.dialogs;

import nu.mine.kino.plugin.configsample.UpdateUIMessages;

import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.update.operations.OperationsManager;

/**
 * リスタートを促すダイアログ。
 * @author Masatomi KINO
 * @version $Revision$
 */
public class RestartDialog extends MessageDialog {
    private static final int CONTINUE = 2;

    private final static String[] yesNo = new String[] {
            IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL };

    private final static String[] yesNoApply = new String[] {
            IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL,
            UpdateUIMessages.ApplyChanges };

    private int buttonId = 0;

    /**
     * Creates a new dialog
     * 
     * @see MessageDialog#MessageDialog(org.eclipse.swt.widgets.Shell,
     *      java.lang.String, org.eclipse.swt.graphics.Image, java.lang.String,
     *      int, java.lang.String[], int)
     */
    public RestartDialog(Shell parent, String title, String message,
            boolean restartNeeded) {
        super(parent, title, null, // accept the default window icon
                message, QUESTION, restartNeeded ? yesNo : yesNoApply, 0); // yes
        // is
        // the
        // default
    }

    /**
     * Convenience method to open the Yes/No/Continue question dialog.
     * 
     * @param parent
     *            the parent shell of the dialog, or <code>null</code> if none
     * @param restartIsReallyNeeded
     *            when false, the changes are applied to the current config
     * @return <code>true</code> if the user presses YES <code>false</code>
     *         otherwise
     */
    public static boolean openQuestion(Shell parent,
            boolean restartIsReallyNeeded) {
        String title = UpdateUIMessages.RestartTitle;
        IProduct product = Platform.getProduct();
        String productName = product != null && product.getName() != null ? product
                .getName()
                : UpdateUIMessages.ApplicationInRestartDialog;
        String message = NLS.bind(
                restartIsReallyNeeded ? UpdateUIMessages.RestartMessage
                        : UpdateUIMessages.OptionalRestartMessage, productName);
        RestartDialog dialog = new RestartDialog(parent, title, message,
                restartIsReallyNeeded);
        int button = dialog.open();
        if (button == 2)
            OperationsManager.applyChangesNow();
        return button == 0; // Yes
    }

    /**
     * When a button is pressed, store the preference.
     * 
     * @see org.eclipse.jface.dialogs.Dialog#buttonPressed(int)
     */
    protected void buttonPressed(int id) {
        if (id == 2) {
            buttonId = CONTINUE;
        }

        super.buttonPressed(id);
    }

    /**
     * Returns the user's selection, <code>null</code> if the user hasn't
     * chosen yet.
     * 
     * @return the user's selection or <code>null</code>
     */
    public int getResult() {
        return buttonId;
    }
}
