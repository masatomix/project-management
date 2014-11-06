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
package nu.mine.kino.plugin.projectmanagement;

import nu.mine.kino.plugin.projectmanagement.sheetdata.IClassInformation;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public interface Generator {
    public ICompilationUnit create(IClassInformation info) throws CoreException;

}
