/*******************************************************************************
 * Copyright (c) 2006 Masatomi KINO. All rights reserved. $Id: TestDataUtilImpl.java,v 1.1 2007/05/27 12:40:38 cvsuser Exp $
 ******************************************************************************/
// çÏê¨ì˙: 2007/05/25
package nu.mine.kino.genericdao.utils;

import java.io.Serializable;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Masatomi KINO
 * @version $Revision: 1.1 $
 * @spring.bean id = "testDataUtil"
 * @spring.property name = "sessionFactory" ref = "sessionFactory"
 */
public class TestDataUtilImpl extends HibernateDaoSupport implements
        ITestDataUtil<Object, Serializable> {

    public Serializable create(Object newInstance) {
        return (Serializable) getHibernateTemplate().save(newInstance);
    }

    public void delete(Class clazz, Serializable id) {
        getHibernateTemplate().delete(this.read(clazz, id));
    }

    public Object read(Class clazz, Serializable id) {
        return (Object) getHibernateTemplate().get(clazz, id);
    }

    public void update(Object transientObject) {
        getHibernateTemplate().update(transientObject);
        getSession().flush();
        getSession().evict(transientObject);

    }

    // public PK create(T o) {
    // return (PK) getHibernateTemplate().save(o);
    // }
    //
    // public T read(PK id) {
    // return (T) getHibernateTemplate().get(type, id);
    // }
    //
    // public void update(T o) {
    // getHibernateTemplate().update(o);
    // getSession().flush();
    // getSession().evict(o);
    // }
    //
    // public void delete(T o) {
    // getHibernateTemplate().delete(o);
    // }
}
