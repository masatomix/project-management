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
//作成日: 2012/07/07

package nu.mine.kino.plugin.webrecorder.views;

import java.util.Date;

import nu.mine.kino.plugin.webrecorder.models.ModelAdapter;
import nu.mine.kino.plugin.webrecorder.models.ModelListener;
import nu.mine.kino.plugin.webrecorder.models.Models;
import nu.mine.kino.plugin.webrecorder.models.RequestResponseModel;

import org.apache.commons.lang.time.DateFormatUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class ListView extends ViewPart {

    private ISelectionListener selectionListner = new ISelectionListener() {
        @Override
        public void selectionChanged(IWorkbenchPart part, ISelection selection) {
            System.out.println(selection);
        }
    };

    // ModelsはRequestResponseModelを管理したり、変更をListenerたちに通知したりする
    private Models<RequestResponseModel> models = new Models<RequestResponseModel>();

    private class ContentProvider extends ArrayContentProvider implements
            ModelListener<RequestResponseModel> {
        private TableViewer viewer;

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            this.viewer = (TableViewer) viewer;
            if (oldInput != null) {
                ((Models<RequestResponseModel>) oldInput)
                        .removeModelListener(this);
            }
            if (newInput != null) {
                ((Models<RequestResponseModel>) newInput)
                        .addModelListener(this);
            }
        }

        @Override
        public void modelAdded(RequestResponseModel model) {
            viewer.add(model);
        }

        @Override
        public void modelAllRemoved() {
            viewer.setInput(models);
        }

        @Override
        public void modelRemoved(RequestResponseModel model) {
            viewer.remove(model);
        }
    }

    public static final String ID = "nu.mine.kino.plugin.webrecorder.views.ListView"; //$NON-NLS-1$

    private Table table;

    private TableViewer tableViewer;

    private Action clearAction;

    public ListView() {
    }

    @Override
    public void init(IViewSite site) throws PartInitException {
        // TODO 自動生成されたメソッド・スタブ
        super.init(site);
        getSite().getPage().addSelectionListener(selectionListner);
    }

    public void addRequestResponseModel(RequestResponseModel model) {
        models.addModel(model);
    }

    /**
     * Create contents of the view part.
     * 
     * @param parent
     */
    @Override
    public void createPartControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new FillLayout(SWT.HORIZONTAL));
        {
            Composite composite = new Composite(container, SWT.NONE);
            TableColumnLayout tcl_composite = new TableColumnLayout();
            composite.setLayout(tcl_composite);
            {
                tableViewer = new TableViewer(composite, SWT.BORDER
                        | SWT.FULL_SELECTION);
                table = tableViewer.getTable();
                table.setHeaderVisible(true);
                table.setLinesVisible(true);
                {
                    TableViewerColumn tableViewerColumn = new TableViewerColumn(
                            tableViewer, SWT.NONE);
                    tableViewerColumn
                            .setLabelProvider(new ColumnLabelProvider() {
                                public Image getImage(Object element) {
                                    return null;
                                }

                                public String getText(Object element) {
                                    return DateFormatUtils.format(new Date(),
                                            "yyyy/MM/dd HH:mm:ss");
                                }
                            });
                    TableColumn dateColumun = tableViewerColumn.getColumn();
                    dateColumun.setMoveable(true);
                    tcl_composite.setColumnData(dateColumun,
                            new ColumnPixelData(120, true, true));
                    dateColumun.setText("Request Date");
                }
                {
                    TableViewerColumn tableViewerColumn = new TableViewerColumn(
                            tableViewer, SWT.NONE);

                    tableViewerColumn
                            .setLabelProvider(new ColumnLabelProvider() {
                                public Image getImage(Object element) {
                                    // TODO Auto-generated method stub
                                    return null;
                                }

                                public String getText(Object element) {
                                    RequestResponseModel model = (RequestResponseModel) element;
                                    return model.getMethod();
                                }
                            });
                    TableColumn methodColumun = tableViewerColumn.getColumn();
                    methodColumun.setMoveable(true);
                    tcl_composite.setColumnData(methodColumun,
                            new ColumnPixelData(75, true, true));
                    methodColumun.setText("method");
                }
                {
                    TableViewerColumn tableViewerColumn = new TableViewerColumn(
                            tableViewer, SWT.NONE);
                    tableViewerColumn
                            .setLabelProvider(new ColumnLabelProvider() {
                                public Image getImage(Object element) {
                                    // TODO Auto-generated method stub
                                    return null;
                                }

                                public String getText(Object element) {
                                    RequestResponseModel model = (RequestResponseModel) element;
                                    return model.getHost();
                                }
                            });
                    TableColumn hostColumun = tableViewerColumn.getColumn();
                    hostColumun.setMoveable(true);
                    tcl_composite.setColumnData(hostColumun,
                            new ColumnPixelData(140, true, true));
                    hostColumun.setText("host");
                }
                {
                    TableViewerColumn tableViewerColumn = new TableViewerColumn(
                            tableViewer, SWT.NONE);
                    tableViewerColumn
                            .setLabelProvider(new ColumnLabelProvider() {
                                public Image getImage(Object element) {
                                    // TODO Auto-generated method stub
                                    return null;
                                }

                                public String getText(Object element) {
                                    RequestResponseModel model = (RequestResponseModel) element;
                                    return model.getUrl();
                                }
                            });
                    TableColumn pathColumun = tableViewerColumn.getColumn();
                    pathColumun.setMoveable(true);
                    tcl_composite.setColumnData(pathColumun,
                            new ColumnPixelData(80, true, true));
                    pathColumun.setText("path");
                }
                {
                    TableViewerColumn tableViewerColumn = new TableViewerColumn(
                            tableViewer, SWT.NONE);
                    tableViewerColumn
                            .setLabelProvider(new ColumnLabelProvider() {
                                public Image getImage(Object element) {
                                    // TODO Auto-generated method stub
                                    return null;
                                }

                                public String getText(Object element) {
                                    RequestResponseModel model = (RequestResponseModel) element;
                                    return model.getQueryString();
                                }
                            });
                    TableColumn parameterColumun = tableViewerColumn
                            .getColumn();
                    parameterColumun.setMoveable(true);
                    tcl_composite.setColumnData(parameterColumun,
                            new ColumnPixelData(100, true, true));
                    parameterColumun.setText("parameter");
                }
                {
                    TableViewerColumn tableViewerColumn = new TableViewerColumn(
                            tableViewer, SWT.NONE);
                    tableViewerColumn
                            .setLabelProvider(new ColumnLabelProvider() {
                                public Image getImage(Object element) {
                                    // TODO Auto-generated method stub
                                    return null;
                                }

                                public String getText(Object element) {
                                    RequestResponseModel model = (RequestResponseModel) element;
                                    Integer status = model.getStatus();
                                    return status == null ? "" : String
                                            .valueOf(status);
                                }
                            });
                    TableColumn statusColumn = tableViewerColumn.getColumn();
                    statusColumn.setMoveable(true);
                    tcl_composite.setColumnData(statusColumn,
                            new ColumnPixelData(40, true, true));
                    statusColumn.setText("status");
                }
                {
                    TableViewerColumn tableViewerColumn = new TableViewerColumn(
                            tableViewer, SWT.NONE);
                    tableViewerColumn
                            .setLabelProvider(new ColumnLabelProvider() {
                                public Image getImage(Object element) {
                                    // TODO Auto-generated method stub
                                    return null;
                                }

                                public String getText(Object element) {
                                    RequestResponseModel model = (RequestResponseModel) element;
                                    return model.getResContentType();
                                }
                            });
                    TableColumn resContentTypeColumun = tableViewerColumn
                            .getColumn();
                    resContentTypeColumun.setMoveable(true);
                    tcl_composite.setColumnData(resContentTypeColumun,
                            new ColumnPixelData(150, true, true));
                    resContentTypeColumun.setText("Resoonse Content-Type");
                }
                {
                    TableViewerColumn tableViewerColumn = new TableViewerColumn(
                            tableViewer, SWT.NONE);
                    tableViewerColumn
                            .setLabelProvider(new ColumnLabelProvider() {
                                public Image getImage(Object element) {
                                    // TODO Auto-generated method stub
                                    return null;
                                }

                                public String getText(Object element) {
                                    RequestResponseModel model = (RequestResponseModel) element;
                                    return model.getResContentLength();
                                }
                            });
                    TableColumn resContentLength = tableViewerColumn
                            .getColumn();
                    resContentLength.setMoveable(true);
                    tcl_composite.setColumnData(resContentLength,
                            new ColumnPixelData(150, true, true));
                    resContentLength.setText("Response Content-Length");
                }
                {
                    TableViewerColumn tableViewerColumn = new TableViewerColumn(
                            tableViewer, SWT.NONE);
                    tableViewerColumn
                            .setLabelProvider(new ColumnLabelProvider() {
                                public Image getImage(Object element) {
                                    return null;
                                }

                                public String getText(Object element) {
                                    RequestResponseModel model = (RequestResponseModel) element;
                                    Date resDate = model.getResDate();
                                    return resDate == null ? ""
                                            : DateFormatUtils.format(resDate,
                                                    "yyyy/MM/dd HH:mm:ss");
                                }
                            });
                    TableColumn resDate = tableViewerColumn.getColumn();
                    resDate.setMoveable(true);
                    tcl_composite.setColumnData(resDate, new ColumnPixelData(
                            150, true, true));
                    resDate.setText("Response Date");
                }
                tableViewer.setContentProvider(new ContentProvider());
            }
        }
        tableViewer.setInput(models);

        createActions();
        initializeToolBar();
        initializeMenu();

        // このビューのSelection ProviderはtableViewer
        getSite().setSelectionProvider(tableViewer);
        selectionListner.selectionChanged(null, getSite().getPage()
                .getSelection());

        models.addModelListener(new ModelAdapter<RequestResponseModel>() {
            @Override
            public void modelAdded(RequestResponseModel model) {
                System.out.println(model);
            }
        });
    }

    /**
     * Create the actions.
     */
    private void createActions() {
        {
            clearAction = new Action("クリア") {
                @Override
                public void run() {
                    models.removeAllModels();
                }
            };
            clearAction
                    .setToolTipText("\u30EA\u30B9\u30C8\u3092\u30AF\u30EA\u30A2\u3057\u307E\u3059");
            clearAction.setImageDescriptor(ResourceManager
                    .getPluginImageDescriptor(
                            "nu.mine.kino.plugin.webrecorder",
                            "icons/clear.gif"));
        }
    }

    /**
     * Initialize the toolbar.
     */
    private void initializeToolBar() {
        IToolBarManager toolbarManager = getViewSite().getActionBars()
                .getToolBarManager();
        toolbarManager.add(clearAction);
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

    @Override
    public void dispose() {
        super.dispose();
        getSite().getPage().removeSelectionListener(selectionListner);
    }
}
