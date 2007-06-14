/*******************************************************************************
 * Copyright (c) 2006 Masatomi KINO. All rights reserved. $Id: ITestDataUtil.java,v 1.1 2007/05/27 12:40:38 cvsuser Exp $
 ******************************************************************************/
// çÏê¨ì˙: 2007/05/25
package nu.mine.kino.genericdao.utils;

import java.io.Serializable;

public interface ITestDataUtil<T, PK extends Serializable> {
    PK create(T newInstance);

    T read(Class clazz, PK id);

    void update(T transientObject);

    void delete(Class clazz, PK persistentObject);

}
