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
//çÏê¨ì˙: 2009/05/06
package nu.mine.kino.plugin.beangenerator.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class CopyUtilTemplateCreateHandler extends
        AbstractTemplateCreateHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        return super.execute(event, "CopyUtilitySample.xls");
    }
}
