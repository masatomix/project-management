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

package nu.mine.kino.projects;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class RedmineConfig {

    private String redmineHost;

    private String apiAccessKey;

    private String userId;

    private String password;

    private boolean apiKeyFlag = false;

    RedmineConfig(String redmineHost) {
        if (redmineHost.endsWith("/")) {
            this.redmineHost = redmineHost;
        } else {
            this.redmineHost = redmineHost + "/";
        }
    }

    public RedmineConfig(String redmineHost, String apiAccessKey) {
        this(redmineHost);
        this.apiAccessKey = apiAccessKey;
        apiKeyFlag = true;
    }

    public RedmineConfig(String redmineHost, String userId, String password) {
        this(redmineHost);
        this.userId = userId;
        this.password = password;
        apiKeyFlag = false;
    }

    public String getRedmineHost() {
        return redmineHost;
    }

    public String getApiAccessKey() {
        return apiAccessKey;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRedmineHost(String redmineHost) {
        this.redmineHost = redmineHost;
    }

    public void setApiAccessKey(String apiAccessKey) {
        this.apiAccessKey = apiAccessKey;
    }

    public boolean isApiKeyFlag() {
        return apiKeyFlag;
    }

}
