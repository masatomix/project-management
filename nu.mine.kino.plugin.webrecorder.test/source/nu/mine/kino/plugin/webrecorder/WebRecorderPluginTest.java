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
//作成日: 2012/06/09

package nu.mine.kino.plugin.webrecorder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.URLEncoder;

import nu.mine.kino.plugin.commons.utils.HttpClientUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class WebRecorderPluginTest {
    private final String GOOGLE_WEB = "ajax.googleapis.com/ajax/services/search";

    @Before
    public void setUp() throws Exception {
        WebRecorderPlugin.getDefault().startServer(RecordMode.RECORD);
        String cacheBasepath = WebRecorderPlugin.getDefault()
                .getCacheBasepath();
        new File(cacheBasepath).mkdirs();
    }

    @Test
    public void testGet() throws Exception {

        String searchText = "きのさいと";
        String input = URLEncoder.encode(searchText, "UTF-8");
        String queryString = "v=1.0&q=" + input;

        String url = "http://" + GOOGLE_WEB + "/web?" + queryString;

        HttpHost proxy = new HttpHost("127.0.0.1", 8008);
        String body = HttpClientUtils.doGet(url, proxy);

        String cacheBasepath = WebRecorderPlugin.getDefault()
                .getCacheBasepath();

        String hash = "953222b1901fe812147187cd005eb55e33b4b99e";
        /* queryStringのSHA1ハッシュ値です */
        File file = new File(new File(cacheBasepath, GOOGLE_WEB), "web_" + hash);

        assertThat(file.exists(), not(false));
        assertThat(file.getName(), is("web_" + hash));

        // fail("Not yet implemented");
    }

    @Test
    public void testPost() throws Exception {

        String searchText = "eclipse";
        String input = URLEncoder.encode(searchText, "UTF-8");
        String queryString = "v=1.0&q=" + input;

        String url = "http://" + GOOGLE_WEB + "/web";

        HttpHost proxy = new HttpHost("127.0.0.1", 8008);
        HttpEntity httpEntity = HttpClientUtils.getHttpEntity(url, queryString,
                null, proxy);

        String cacheBasepath = WebRecorderPlugin.getDefault()
                .getCacheBasepath();

        String hash = "953222b1901fe812147187cd005eb55e33b4b99e";// requestBodyのSHA1ハッシュ値です

        File file = new File(new File(cacheBasepath, GOOGLE_WEB), "web_" + hash);

        assertThat(file.exists(), not(false));
        assertThat(file.getName(), is("web_" + hash));

    }

    @After
    public void tearDown() throws Exception {
        // String cacheBasepath = WebRecorderPlugin.getDefault()
        // .getCacheBasepath();
        //
        // File file = new File(new File(cacheBasepath,
        // "ajax.googleapis.com/ajax/services/search"),
        // "web_953222b1901fe812147187cd005eb55e33b4b99e");
        // file.delete();
        WebRecorderPlugin.getDefault().stopServer();
    }

}
