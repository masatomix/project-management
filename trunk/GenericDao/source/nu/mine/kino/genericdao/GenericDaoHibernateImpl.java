/*******************************************************************************
 * Copyright (c) 2006 Masatomi KINO.
 * All rights reserved. 
 * $Id: GenericDaoHibernateImpl.java,v 1.1 2007/05/27 12:40:38 cvsuser Exp $
 *******************************************************************************/
//çÏê¨ì˙: 2007/04/27
package nu.mine.kino.genericdao;

import java.io.Serializable;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Masatomi KINO
 * @version $Revision: 1.1 $
 */
public class GenericDaoHibernateImpl<T, PK extends Serializable> extends
        HibernateDaoSupport implements GenericDao<T, PK> {
    private Class<T> type;

    public GenericDaoHibernateImpl(Class<T> type) {
        this.type = type;
    }

    public PK create(T o) {
        return (PK) getHibernateTemplate().save(o);
    }

    public T read(PK id) {
        return (T) getHibernateTemplate().get(type, id);
    }

    public void update(T o) {
        getHibernateTemplate().update(o);
        getSession().flush();
        getSession().evict(o);
    }

    public void delete(T o) {
        getHibernateTemplate().delete(o);
    }


}
