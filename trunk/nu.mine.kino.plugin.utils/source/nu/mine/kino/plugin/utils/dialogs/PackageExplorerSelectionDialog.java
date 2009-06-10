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
// 作成日: 2007/05/23
package nu.mine.kino.plugin.utils.dialogs;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nu.mine.kino.plugin.utils.Activator;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
 * パッケージエクスプローラライクなビューワからリソースを選択するためのダイアログです。
 * 
 * @author Masatomi KINO
 * @version $Revision$
 */
public class PackageExplorerSelectionDialog extends SelectionDialog {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(PackageExplorerSelectionDialog.class);

    // the root element to populate the viewer with
    private TreeViewer treeViewer;

    private Tree tree;

    private IAdaptable root;

    // the visual selection widget group
    // private CheckboxTreeAndListGroup selectionGroup;

    // constants
    // private final static int SIZING_SELECTION_WIDGET_WIDTH = 400;
    //
    // private final static int SIZING_SELECTION_WIDGET_HEIGHT = 300;

    /**
     * Creates a resource selection dialog rooted at the given element.
     * 
     * @param parentShell
     *            the parent shell
     * @param root
     *            the root element to populate this dialog with
     * @param message
     *            the message to be displayed at the top of this dialog, or
     *            <code>null</code> to display a default message
     */
    public PackageExplorerSelectionDialog(Shell parentShell, IAdaptable root,
            String message) {
        super(parentShell);
        setTitle("リソースの選択");
        this.root = root;
        if (message != null) {
            setMessage(message);
        } else {
            setMessage("リソースを選択します。");
        }
        setShellStyle(getShellStyle() | SWT.RESIZE);
    }

    /**
     * Visually checks the previously-specified elements in the container (left)
     * portion of this dialog's resource selection viewer.
     */
    private void checkInitialSelections() {
        // ダイアログを呼んだヤツが、あらかじめSelectionをセットしている場合
        // の処理を記述する。
        Iterator itemsToCheck = getInitialElementSelections().iterator();

        while (itemsToCheck.hasNext()) {
            IResource currentElement = (IResource) itemsToCheck.next();

            if (currentElement.getType() == IResource.FILE) {
                // selectionGroup.initialCheckListItem(currentElement);
            } else {
                // selectionGroup.initialCheckTreeItem(currentElement);
            }
        }
    }

    /**
     * @param event
     *            the event
     */
    // public void checkStateChanged(CheckStateChangedEvent event) {
    // getOkButton().setEnabled(selectionGroup.getCheckedElementCount() > 0);
    // }
    /*
     * (non-Javadoc) Method declared in Window.
     */
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        // PlatformUI.getWorkbench().getHelpSystem().setHelp(shell,
        // IIDEHelpContextIds.RESOURCE_SELECTION_DIALOG);
    }

    public void create() {
        super.create();
        initializeDialog();
    }

    /*
     * (non-Javadoc) Method declared on Dialog.
     */
    protected Control createDialogArea(Composite parent) {
        // page group
        Composite composite = (Composite) super.createDialogArea(parent);

        // create the input element, which has the root resource
        // as its only child
        // ArrayList input = new ArrayList();
        // input.add(root);

        createMessageArea(composite);
        // selectionGroup = new CheckboxTreeAndListGroup(composite, input,
        // getResourceProvider(IResource.FOLDER | IResource.PROJECT
        // | IResource.ROOT), WorkbenchLabelProvider
        // .getDecoratingWorkbenchLabelProvider(),
        // getResourceProvider(IResource.FILE), WorkbenchLabelProvider
        // .getDecoratingWorkbenchLabelProvider(), SWT.NONE,
        // // since this page has no other significantly-sized
        // // widgets we need to hardcode the combined widget's
        // // size, otherwise it will open too small
        // SIZING_SELECTION_WIDGET_WIDTH, SIZING_SELECTION_WIDGET_HEIGHT);
        //
        // composite.addControlListener(new ControlListener() {
        // public void controlMoved(ControlEvent e) {
        // }
        //
        // public void controlResized(ControlEvent e) {
        // // Also try and reset the size of the columns as appropriate
        // TableColumn[] columns = selectionGroup.getListTable()
        // .getColumns();
        // for (int i = 0; i < columns.length; i++) {
        // columns[i].pack();
        // }
        // }
        // });

        treeViewer = new TreeViewer(composite, SWT.BORDER);
        tree = treeViewer.getTree();
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        treeViewer.setContentProvider(new TreeContentProvider());
        treeViewer.setLabelProvider(new DecoratingLabelProvider(
                new WorkbenchLabelProvider(), Activator.getDefault()
                        .getWorkbench().getDecoratorManager()
                        .getLabelDecorator()));
        treeViewer.addFilter(new ViewerFilter() {
            @Override
            public boolean select(Viewer viewer, Object parentElement,
                    Object element) {
                if (element instanceof IProject) {
                    IProject project = (IProject) element;
                    if (!project.isAccessible()) {
                        return false;
                    }
                }

                return true;
            }
        });
        treeViewer.setInput(((IWorkspaceRoot) root).getProjects());
        treeViewer.addSelectionChangedListener(listener);

        return composite;
    }

    // /**
    // * Returns a content provider for <code>IResource</code>s that returns
    // * only children of the given resource type.
    // */
    // private ITreeContentProvider getResourceProvider(final int resourceType)
    // {
    // return new WorkbenchContentProvider() {
    // public Object[] getChildren(Object o) {
    // if (o instanceof IContainer) {
    // IResource[] members = null;
    // try {
    // members = ((IContainer) o).members();
    // } catch (CoreException e) {
    // // just return an empty set of children
    // return new Object[0];
    // }
    //
    // // filter out the desired resource types
    // ArrayList results = new ArrayList();
    // for (int i = 0; i < members.length; i++) {
    // // And the test bits with the resource types to see if
    // // they are what we want
    // if ((members[i].getType() & resourceType) > 0) {
    // results.add(members[i]);
    // }
    // }
    // return results.toArray();
    // }
    // // input element case
    // if (o instanceof ArrayList) {
    // return ((ArrayList) o).toArray();
    // }
    // return new Object[0];
    // }
    // };
    // }

    /**
     * Initializes this dialog's controls.
     */
    private void initializeDialog() {
        // selectionGroup.addCheckStateListener(new ICheckStateListener() {
        // public void checkStateChanged(CheckStateChangedEvent event) {
        // getOkButton().setEnabled(
        // selectionGroup.getCheckedElementCount() > 0);
        // }
        // });

        if (getInitialElementSelections().isEmpty()) {
            getOkButton().setEnabled(false);
        } else {
            checkInitialSelections();
        }
    }

    /**
     * The <code>ResourceSelectionDialog</code> implementation of this
     * <code>Dialog</code> method builds a list of the selected resources for
     * later retrieval by the client and closes this dialog.
     */
    protected void okPressed() {
        logger.debug("okPressed() - start");

        // Iterator resultEnum = selectionGroup.getAllCheckedListItems();
        // ArrayList list = new ArrayList();
        // while (resultEnum.hasNext()) {
        // list.add(resultEnum.next());
        // }

        IStructuredSelection selection = (IStructuredSelection) treeViewer
                .getSelection();
        Object firstElement = selection.getFirstElement();
        List list = new ArrayList();
        list.add(firstElement);
        setResult(list);
        super.okPressed();

        logger.debug("okPressed() - end");
    }

    class TreeContentProvider implements IStructuredContentProvider,
            ITreeContentProvider {
        /**
         * Logger for this class
         */
        private final Logger logger = Logger
                .getLogger(TreeContentProvider.class);

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }

        public void dispose() {
        }

        public Object[] getElements(Object inputElement) {
            return getChildren(inputElement);
        }

        public Object[] getChildren(Object parentElement) {
            if (parentElement instanceof IProject[]) {
                IProject[] projects = (IProject[]) parentElement;
                return projects;
            }

            if (parentElement instanceof IContainer) {
                try {
                    final IContainer container = (IContainer) parentElement;
                    if (container.isAccessible()) {
                        return container.members();
                    }
                } catch (CoreException e) {
                    e.printStackTrace();
                }
            }
            return new Object[0];
        }

        public Object getParent(Object element) {
            return null;
        }

        public boolean hasChildren(Object element) {
            return getChildren(element).length > 0;
        }
    }

    private ISelectionChangedListener listener = new ISelectionChangedListener() {

        public void selectionChanged(SelectionChangedEvent event) {

            IStructuredSelection sselection = (IStructuredSelection) event
                    .getSelection();
            IResource firstElement = (IResource) sselection.getFirstElement();
            if (firstElement.getType() == IResource.FILE) {
                getOkButton().setEnabled(true);
            } else {
                getOkButton().setEnabled(false);
            }
        }

    };

}
