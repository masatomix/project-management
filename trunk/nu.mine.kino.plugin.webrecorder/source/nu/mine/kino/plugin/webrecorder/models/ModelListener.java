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
//作成日: 2012/07/07

package nu.mine.kino.plugin.webrecorder.models;

/**
 * model Tの通知を受け取るListenerインタフェース
 * 
 * @author Masatomi KINO
 * @version $Revision$
 * @see Models
 */
public interface ModelListener<T> {
    /**
     * Modelsクラスにmodelがaddされたときにコールバックされます
     * 
     * @param model
     */
    void modelAdded(T model);

    /**
     * Modelsクラスが管理しているリストがすべて削除されたときにコールバックされます
     */
    void modelAllRemoved();

    /**
     * Modelsクラスのmodelが削除されたときにコールバックされます
     * 
     * @param model
     */
    void modelRemoved(T model);
}
