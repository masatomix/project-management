/******************************************************************************
 * Copyright (c) 2012 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//作成日: 2012/06/24

package nu.mine.kino.plugin.webrecorder.views;

import nu.mine.kino.plugin.commons.utils.StringUtils;
import nu.mine.kino.plugin.webrecorder.jobs.DriverJob;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class DriverView extends ViewPart {

    public static final String ID = "nu.mine.kino.plugin.webrecorder.views.DriverView"; //$NON-NLS-1$

    private Text txtURL;

    private Text textRequestBody;

    private Text textResult;

    private Combo comboContentType;

    private Button btnIsWithProxy;

    private Button submitButton;

    private IMemento memento;

    private Combo comboMethod;

    public DriverView() {
    }

    /**
     * Create contents of the view part.
     * 
     * @param parent
     */
    @Override
    public void createPartControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(3, false));

        comboMethod = new Combo(container, SWT.READ_ONLY);
        comboMethod.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
                false, 1, 1));
        comboMethod.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if ("POST".equals(comboMethod.getText())) {
                    textRequestBody.setEditable(true);
                } else {
                    textRequestBody.setEditable(false);
                }
            }
        });
        comboMethod.setItems(new String[] { "GET", "POST" });
        comboMethod.select(1);

        txtURL = new Text(container, SWT.BORDER);
        txtURL.setText("http://www.masatom.in/pukiwiki/?cmd=search");
        txtURL.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
                1));

        submitButton = new Button(container, SWT.NONE);
        submitButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                
                textResult.setText("");
                String method = comboMethod.getText();
                String url = txtURL.getText();
                String body = textRequestBody.getText();
                String contentType = comboContentType.getText();
                boolean isWithProxy = btnIsWithProxy.getSelection();

                Job job = new DriverJob(method, url, contentType, body,
                        isWithProxy, textResult);
                // ダイアログを出す
                job.setUser(true);
                job.schedule();

            }

        });
        submitButton.setText("\u9001\u4FE1");

        txtURL.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                String text = txtURL.getText();
                if (!StringUtils.isEmpty(text)
                        && StringUtils.startsWith(text, "http://", "https://")) {
                    submitButton.setEnabled(true);
                } else {
                    submitButton.setEnabled(false);
                }
            }
        });

        Label lblContentType = new Label(container, SWT.NONE);
        lblContentType.setText("Content Type:");

        comboContentType = new Combo(container, SWT.NONE);
        comboContentType.setItems(new String[] { "",
                "application/json; charset=UTF-8" });
        comboContentType.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
                false, 1, 1));
        comboContentType.select(0);

        btnIsWithProxy = new Button(container, SWT.CHECK);
        btnIsWithProxy.setText("Recoder\u7D4C\u7531");

        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setText("Request Body:");
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);

        SashForm sashForm = new SashForm(container, SWT.VERTICAL);
        sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 3,
                1));

        textRequestBody = new Text(sashForm, SWT.BORDER | SWT.READ_ONLY
                | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
        textRequestBody.setEditable(true);
        textRequestBody.setText("encode_hint=%A4%D7&word=TESTTEST&type=AND");

        textResult = new Text(sashForm, SWT.BORDER | SWT.H_SCROLL
                | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
        sashForm.setWeights(new int[] { 1, 1 });

        createActions();
        initializeToolBar();
        initializeMenu();

        restoreState();

    }

    /**
     * Create the actions.
     */
    private void createActions() {
        // Create the actions
    }

    /**
     * Initialize the toolbar.
     */
    private void initializeToolBar() {
        IToolBarManager toolbarManager = getViewSite().getActionBars()
                .getToolBarManager();
    }

    /**
     * Initialize the menu.
     */
    private void initializeMenu() {
        IMenuManager menuManager = getViewSite().getActionBars()
                .getMenuManager();
    }

    @Override
    public void setFocus() {
        // Set the focus
    }

    public void init(IViewSite site, IMemento memento) throws PartInitException {
        super.init(site, memento);
        this.memento = memento;

    }

    @Override
    public void saveState(IMemento memento) {
        super.saveState(memento);

        String method = comboMethod.getText();
        String url = txtURL.getText();
        String body = textRequestBody.getText();
        String contentType = comboContentType.getText();
        boolean isWithProxy = btnIsWithProxy.getSelection();

        memento.putString("method", method);
        memento.putString("url", url);
        memento.putString("body", body);
        memento.putString("contentType", contentType);
        memento.putBoolean("isWithProxy", isWithProxy);

    }

    private void restoreState() {
        if (memento == null) {
            return;
        }
        String method = memento.getString("method");
        String url = memento.getString("url");
        String body = memento.getString("body");
        String contentType = memento.getString("contentType");
        boolean isWithProxy = memento.getBoolean("isWithProxy");

        txtURL.setText(url);
        textRequestBody.setText(body);
        btnIsWithProxy.setSelection(isWithProxy);
    }

}
