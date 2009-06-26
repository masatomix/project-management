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
//çÏê¨ì˙: 2009/06/27
package nu.mine.kino.plugin.jdt.utils;


import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class JavaUIStatus extends Status {

    private JavaUIStatus(int severity, int code, String message,
            Throwable throwable) {
        super(severity, JDTUtilsPlugin.PLUGIN_ID, code, message, throwable);
    }

    public static IStatus createError(int code, Throwable throwable) {
        String message = throwable.getMessage();
        if (message == null) {
            message = throwable.getClass().getName();
        }
        return new JavaUIStatus(IStatus.ERROR, code, message, throwable);
    }

    public static IStatus createError(int code, String message,
            Throwable throwable) {
        return new JavaUIStatus(IStatus.ERROR, code, message, throwable);
    }

    public static IStatus createWarning(int code, String message,
            Throwable throwable) {
        return new JavaUIStatus(IStatus.WARNING, code, message, throwable);
    }

    public static IStatus createInfo(int code, String message,
            Throwable throwable) {
        return new JavaUIStatus(IStatus.INFO, code, message, throwable);
    }
}
