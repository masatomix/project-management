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
//çÏê¨ì˙: 2012/06/24

package nu.mine.kino.plugin.webrecorder.views;

import java.io.IOException;
import java.io.InputStream;

import nu.mine.kino.plugin.commons.utils.HttpClientUtils;
import nu.mine.kino.plugin.commons.utils.RWUtils;
import nu.mine.kino.plugin.webrecorder.WebRecorderPlugin;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class DriverView extends ViewPart {

    public static final String ID = "nu.mine.kino.plugin.webrecorder.views.DriverView"; //$NON-NLS-1$

    private Text txtURL;

    private Text textRequestBody;

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
        container.setLayout(new GridLayout(4, false));
        new Label(container, SWT.NONE);

        final Combo comboMethod = new Combo(container, SWT.NONE);
        comboMethod.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if ("POST".equals(comboMethod.getText())) {
                    textRequestBody.setEditable(true);
                } else {
                    textRequestBody.setEditable(false);
                    // êFÇ≠ÇÁÇ¢ïœÇ¶ÇÈÇ©
                }
            }
        });
        comboMethod.setItems(new String[] { "GET", "POST" });
        comboMethod.select(0);

        txtURL = new Text(container, SWT.BORDER | SWT.MULTI);
        txtURL.setText("http://");
        txtURL.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
                1, 1));

        Button requestButton = new Button(container, SWT.NONE);
        requestButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String method = comboMethod.getText();
                String url = txtURL.getText();
                String body = textRequestBody.getText();
                try {
                    if ("POST".equals(method)) {
                        executePost(url, body);
                    } else if ("GET".equals(method)) {
                        executeGet(url);
                    }
                } catch (Exception e2) {
                    // TODO: handle exception
                }

            }

            private void executePost(String url, String body)
                    throws IOException, ClientProtocolException {
                // String contentType = hRequest.getContentType();
                HttpEntity entity = HttpClientUtils.getHttpEntity(url, body,
                        null, null);
                if (entity != null) {
                    InputStream stream = entity.getContent();
                    String result = RWUtils.stream2String(stream, "UTF-8");
                    WebRecorderPlugin.getDefault().printConsole(result);
                }
            }

            private void executeGet(String url) throws IOException,
                    ClientProtocolException {

                HttpEntity entity = HttpClientUtils.getHttpEntity(url);
                if (entity != null) {
                    InputStream stream = entity.getContent();
                    String result = RWUtils.stream2String(stream, "UTF-8");
                    WebRecorderPlugin.getDefault().printConsole(result);
                }
            }

        });
        requestButton.setText("\u9001\u4FE1");
        new Label(container, SWT.NONE);

        textRequestBody = new Text(container, SWT.BORDER | SWT.READ_ONLY
                | SWT.MULTI);
        textRequestBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                true, 2, 1));
        new Label(container, SWT.NONE);

        createActions();
        initializeToolBar();
        initializeMenu();
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
}
