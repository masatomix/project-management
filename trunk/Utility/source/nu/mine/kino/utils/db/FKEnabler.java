/*******************************************************************************
 * Copyright (c) 2006 Masatomi KINO.
 * All rights reserved. 
 * $Id$
 *******************************************************************************/
//çÏê¨ì˙: 2007/12/08
package nu.mine.kino.utils.db;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public interface FKEnabler {
    public void enable(String tableName, String fkName);

    public void disable(String tableName, String fkName);

    public void enableAll();

    public void disableAll();

}
