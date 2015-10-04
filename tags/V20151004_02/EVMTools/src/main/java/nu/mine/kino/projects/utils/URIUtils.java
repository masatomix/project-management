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
//çÏê¨ì˙: 2013/06/25

package nu.mine.kino.projects.utils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class URIUtils {
    private static URI resolve(String host, String path) {
        URI uri = null;
        try {
            uri = new URI(host);
            uri = uri.resolve(path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
        return uri;
    }

    public static String resolveURIStr(String host, String path) {
        URI resolve = resolve(host, path);
        return resolve.toASCIIString();
    }

}
