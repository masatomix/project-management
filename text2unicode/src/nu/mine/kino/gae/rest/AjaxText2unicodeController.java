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
//çÏê¨ì˙: 2013/07/04

package nu.mine.kino.gae.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import nu.mine.kino.gae.TextUtilsService;
import nu.mine.kino.gae.UnicodeTextObject;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class AjaxText2unicodeController {
    public UnicodeTextObject find(Map<String, String> params)
            throws UnsupportedEncodingException {
        String source = URLDecoder.decode(params.get("source"), "UTF-8");
        String uort = params.get("uort");
        System.out.println(params);
        UnicodeTextObject create =
            new TextUtilsService().create(source, uort.charAt(0));
        return create;
    }
}
