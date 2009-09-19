/*******************************************************************************
 * Copyright (c) 2006 Masatomi KINO.
 * All rights reserved. 
 * $Id$
 *******************************************************************************/
//ì¬“ú: 2007/12/09
package nu.mine.kino.utils.db.ant;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class Table {
    private String name;

    private String fkname;

    /**
     * @return fkname
     */
    public String getFkname() {
        return fkname;
    }

    /**
     * @param fkname
     *            fkname ‚ğİ’èB
     */
    public void setFkname(String fkname) {
        this.fkname = fkname;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            name ‚ğİ’èB
     */
    public void setName(String name) {
        this.name = name;
    }

}
