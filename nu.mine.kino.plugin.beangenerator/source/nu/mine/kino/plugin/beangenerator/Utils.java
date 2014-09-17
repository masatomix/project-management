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
//çÏê¨ì˙: 2014/09/11

package nu.mine.kino.plugin.beangenerator;

import org.apache.commons.lang.WordUtils;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class Utils {

    public static String createPropertyMethodName(String field) {
        if (field.length() == 1) {
            return WordUtils.capitalize(field);
        }
        char target = field.charAt(1);
        if (Character.isUpperCase(target)) {
            return field;
        }
        return WordUtils.capitalize(field);
    }

    public static boolean isEmpty(String target) {
        return target == null || "".equals(target);
    }
}
