/******************************************************************************
 * Copyright (c) 2008 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//çÏê¨ì˙: 2008/08/15
package nu.mine.kino.plugin.beangenerator.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchSite;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class JavaBeansCreatorWithProgress implements IRunnableWithProgress {

    private IStructuredSelection ss;

    private List list;

    private IWorkbenchSite site;

    public JavaBeansCreatorWithProgress(IStructuredSelection ss,
            IWorkbenchSite site) {
        this.ss = ss;
        this.site = site;
    }

    public void run(IProgressMonitor monitor) throws InvocationTargetException,
            InterruptedException {
        System.out.println(monitor);
    }

}
