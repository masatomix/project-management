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

import nu.mine.kino.gae.Text;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class AjaxTextController {
    public Text find(Map<String, String> params)
            throws UnsupportedEncodingException {
        String input = URLDecoder.decode(params.get("source"), "UTF-8");
        System.out.println(params);
        Text result = new Text();

        String type = params.get("type");
        if (type.equals("unicode")) {
            result.setUnicode(input);
        } else if (type.equals("urlEncodeUTF8")) {
            result.setURLEncodeUTF8(input);
        } else if (type.equals("URLEncodeEUCJP")) {
            result.setURLEncodeEUCJP(input);
        } else {
            result.setSource(input);
        }
        return result;
    }
}
