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
//çÏê¨ì˙: 2012/06/03

package nu.mine.kino.plugin.webrecorder.handlers;

import nu.mine.kino.plugin.webrecorder.WebRecorderPlugin;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class StopHandler extends AbstractHandler {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(StopHandler.class);

    /**
     * the command has been executed, so extract extract the needed information
     * from the application context.
     */
    public Object execute(ExecutionEvent event) throws ExecutionException {
        logger.debug("execute(ExecutionEvent) - start");

        try {
            WebRecorderPlugin.getDefault().stopServer();
        } catch (Exception e) {
            logger.error("execute(ExecutionEvent)", e);
            e.printStackTrace();
            WebRecorderPlugin.logException(e, false);
        }

        logger.debug("execute(ExecutionEvent) - end");
        return null;
    }
}
