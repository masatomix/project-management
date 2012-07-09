/******************************************************************************
 * Copyright (c) 2010 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//çÏê¨ì˙: 2012/07/07

package nu.mine.kino.plugin.webrecorder.views;

import nu.mine.kino.plugin.webrecorder.models.ModelListener;
import nu.mine.kino.plugin.webrecorder.models.Models;
import nu.mine.kino.plugin.webrecorder.models.RequestResponseModel;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class ListView extends ViewPart {

    // modelÇä«óùÇµÇΩÇËÅAïœçXÇListenerÇΩÇøÇ…í ímÇµÇΩÇËÇ∑ÇÈ
    private Models models = new Models();

    private static class ContentProvider extends ArrayContentProvider implements
            ModelListener {
        private TableViewer viewer;

        public Object[] getElements(Object inputElement) {
            return new Object[0];
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            this.viewer = (TableViewer) viewer;
            if (oldInput != null) {
                ((Models) oldInput).removeModelListener(this);
            }

            if (newInput != null) {
                ((Models) newInput).addModelListener(this);
            }
        }

        @Override
        public void modelAdded(RequestResponseModel model) {
            viewer.add(model);
        }
    }

    private class TableLabelProvider extends LabelProvider implements
            ITableLabelProvider {
        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            return element.toString();
        }
    }

    public static final String ID = "nu.mine.kino.plugin.webrecorder.views.ListView"; //$NON-NLS-1$

    private Table table;

    private TableViewer tableViewer;

    public ListView() {
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
                    TableColumn tblclmnNewColumn = tableViewerColumn
                            .getColumn();
                    tcl_composite.setColumnData(tblclmnNewColumn,
                            new ColumnPixelData(150, true, true));
                    tblclmnNewColumn.setText("New Column");
                }
                tableViewer.setContentProvider(new ContentProvider());
                tableViewer.setLabelProvider(new TableLabelProvider());
            }
        }
        tableViewer.setInput(models);

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
