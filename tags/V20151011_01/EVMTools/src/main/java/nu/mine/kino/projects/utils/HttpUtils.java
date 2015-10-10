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
//作成日: 2013/06/12

package nu.mine.kino.projects.utils;

import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class HttpUtils {

    // URLにアクセスしレスポンスの文字列を取得する。
    public static String getWebPage(String url) {
        // Restletバージョン
        // ClientResource resource = new ClientResource(url);
        // String responseHtml = resource.get().getText();
        // return responseHtml;

        // jerseyバージョン
        Client c = Client.create();
        WebResource r = c.resource(url);
        ClientResponse getResponse = r.get(ClientResponse.class);
        String responseStr = getResponse.getEntity(String.class);
        return responseStr;

    }

    // URLにアクセスしレスポンスの文字列を取得する。
    public static String getWebPage(String url, String userId, String password) {
        String authKey = "Basic "
                + Base64.encodeBase64String(
                        (userId + ':' + password).getBytes()).trim();
        Client c = Client.create();
        WebResource r = c.resource(url);
        ClientResponse getResponse = r.header("Authorization", authKey).get(
                ClientResponse.class);
        // ClientResponse getResponse = r.get(ClientResponse.class);
        String responseStr = getResponse.getEntity(String.class);
        return responseStr;

    }

    public static ClientResponse putWebPage(String url, Object obj,
            String mediaType) {
        Client c = Client.create();
        WebResource r = c.resource(url);
        ClientResponse response = r.accept(mediaType).put(ClientResponse.class,
                obj);
        return response;
    }

    public static ClientResponse putWebPage(String url, Object obj) {
        return putWebPage(url, obj, MediaType.APPLICATION_XML);
    }

}
