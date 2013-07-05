/******************************************************************************
 * Copyright (c) 2010 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//çÏê¨ì˙: 2013/07/03

package nu.mine.kino.gae;


/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class UnicodeTextObject {

    private String text;
    private String unicode;

    public String getText() {
        return text;
    }

    public String getUnicode() {
        return unicode;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUnicode(String unicode) {
        this.unicode = unicode;
    }

}
