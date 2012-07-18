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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Viewerに表示する情報(モデル)を管理するクラス。 たモデルの追加・削除などをリスナたちに通知する。
 * 
 * @author Masatomi KINO
 * @version $Revision$
 */
public class Models<T> {
    private List<T> models = new ArrayList<T>();

    private List<ModelListener<T>> listeners = new ArrayList<ModelListener<T>>();

    /**
     * model をリストに追加する。追加されたらリスナたちに通知。
     * 
     * @param model
     * @see ModelListener#modelAdded(Object)
     */
    public void addModel(T model) {
        models.add(model);
        for (ModelListener<T> listener : listeners) {
            listener.modelAdded(model);
        }
    }

    /**
     * 引数のmodelをリストから削除します。削除したら、リスナたちに通知
     * 
     * @param model
     * @see ModelListener#modelRemoved(Object)
     */
    public void removeModel(T model) {
        models.remove(model);
        for (ModelListener<T> listener : listeners) {
            listener.modelRemoved(model);
        }
    }

    /**
     * modelすべて削除します。削除したらリスナたちに通知。
     * 
     * @see ModelListener#modelAllRemoved()
     */
    public void removeAllModels() {
        models.clear();
        for (ModelListener<T> listener : listeners) {
            listener.modelAllRemoved();
        }
    }

    public void addModelListener(ModelListener<T> listener) {
        listeners.add(listener);
    }

    public void removeModelListener(ModelListener<T> listener) {
        listeners.remove(listener);
    }
}
