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
//çÏê¨ì˙: 2014/12/09

package nu.mine.kino.project;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class SVNLogBean {

    private char type;

    private String path;

    private long revision;

    private String date;

    private String author;

    public char getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public long getRevision() {
        return revision;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public void setType(char type) {
        this.type = type;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setRevision(long revision) {
        this.revision = revision;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}