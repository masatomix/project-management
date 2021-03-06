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
//作成日: 2015/03/07

package nu.mine.kino.jenkins.plugins.projectmanagement;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public enum FORMAT {
    GRAPH("graph", "グラフ"), LIST("list", "時系列詳細");

    private final String urlSuffix;

    private final String name;

    private FORMAT(String urlSuffix, String name) {
        this.urlSuffix = urlSuffix;
        this.name = name;
    }

    public String getUrlSuffix() {
        return urlSuffix;
    }

    public String getName() {
        return name;
    }
}
