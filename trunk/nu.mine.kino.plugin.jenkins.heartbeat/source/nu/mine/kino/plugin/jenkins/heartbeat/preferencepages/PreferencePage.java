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
//作成日: 2014/12/18

package nu.mine.kino.plugin.jenkins.heartbeat.preferencepages;

import nu.mine.kino.plugin.jenkins.heartbeat.Constants;
import nu.mine.kino.plugin.jenkins.heartbeat.HeartbeatPlugin;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class PreferencePage extends FieldEditorPreferencePage implements
        IWorkbenchPreferencePage {

    public PreferencePage() {
        super(GRID);
        // 実際のKey,Valueを格納する、PreferenceStoreを設定。
        setPreferenceStore(HeartbeatPlugin.getDefault().getPreferenceStore());
        setDescription("Jenkinsサーバが稼働しているかを確認するための設定を行います");
    }

    @Override
    public void init(IWorkbench workbench) {

    }

    @Override
    protected void createFieldEditors() {
        {
            addField(new StringFieldEditor(Constants.BASE_URL, "&BaseURL:",
                    getFieldEditorParent()));

        }
        {
            addField(new StringFieldEditor(Constants.USER_NAME, "&UserName:",
                    getFieldEditorParent()));

        }
        {
            addField(new PasswordFieldEditor(Constants.PASSWORD, "&Password:",
                    getFieldEditorParent()));

        }
        {
            addField(new IntegerFieldEditor(Constants.PERIOD,
                    "&チェック間隔(秒)(再起動後に有効):", getFieldEditorParent()));

        }
        {
            addField(new BooleanFieldEditor(Constants.SHOW_DIALOG,
                    "ダイアログで通知する:", getFieldEditorParent()));

        }

    }

}

class PasswordFieldEditor extends StringFieldEditor {

    public PasswordFieldEditor() {
        super();
    }

    public PasswordFieldEditor(String name, String labelText, Composite parent) {
        super(name, labelText, parent);
    }

    @Override
    protected void doFillIntoGrid(Composite parent, int numColumns) {
        super.doFillIntoGrid(parent, numColumns);
        getTextControl().setEchoChar('*');
    }

}