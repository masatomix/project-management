/*******************************************************************************
 * Copyright (c) 2006 Masatomi KINO.
 * All rights reserved. 
 * $Id$
 *******************************************************************************/
//çÏê¨ì˙: 2007/12/08
package nu.mine.kino.utils.db;

import java.util.List;
import java.util.Map;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public interface FKChecker {
    public List<String> findFKNameByTableName(String tableName);

    public List<Map<String, String>> findAll();

    public boolean exist(String tableName, String fkName);

}
