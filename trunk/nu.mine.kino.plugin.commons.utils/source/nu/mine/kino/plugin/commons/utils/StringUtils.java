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
//çÏê¨ì˙: 2012/06/12

package nu.mine.kino.plugin.commons.utils;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class StringUtils {

    public static boolean isEmpty(String source) {
        return (source == null || "".equals(source));

    }

    public static boolean endWith(String source, String[] suffixs) {
        if (isEmpty(source)) {
            return false;
        }
        for (int i = 0; i < suffixs.length; i++) {
            if (source.endsWith(suffixs[i])) {
                return true;
            }
        }
        return false;
    }
}
