/*******************************************************************************
 * Copyright (c) 2006 Masatomi KINO.
 * All rights reserved. 
 * $Id: GenericDao.java,v 1.1 2007/05/27 12:40:38 cvsuser Exp $
 *******************************************************************************/
//çÏê¨ì˙: 2007/04/27
package nu.mine.kino.genericdao;

import java.io.Serializable;

/**
 * @author Masatomi KINO
 * @version $Revision: 1.1 $
 */
public interface GenericDao<T, PK extends Serializable> {
    PK create(T newInstance);

    T read(PK id);

    void update(T transientObject);

    void delete(T persistentObject);
}
