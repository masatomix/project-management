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

package nu.mine.kino.plugin.webrecorder;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class UtilsTest {

    @Test
    public void testAppendPrefix() {

        String source = null;
        String actual = null;

        source = "/WebObjects/MZStore.woa/wpa/MRSS/newreleases/limit=25/rss.xml";
        actual = Utils.appendPrefix(source, "get_");
        assertThat(
                actual,
                is("/WebObjects/MZStore.woa/wpa/MRSS/newreleases/limit=25/get_rss.xml"));

        source = "/";
        actual = Utils.appendPrefix(source, "get_");
        assertThat(actual, is("/get_"));

        source = "/a";
        actual = Utils.appendPrefix(source, "get_");
        assertThat(actual, is("/get_a"));

        source = "a";
        actual = Utils.appendPrefix(source, "get_");
        assertThat(actual, is("get_a"));

        source = "/a";
        actual = Utils.appendPrefix(source, "get_");
        assertThat(actual, is("/get_a"));

        source = null;
        actual = Utils.appendPrefix(source, "get_");
        assertThat(actual, is("get_"));
        

        source = "";
        actual = Utils.appendPrefix(source, "get_");
        assertThat(actual, is("get_"));
    }
}
