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
//作成日: 2012/07/07

package nu.mine.kino.plugin.webrecorder.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Viewerの情報を管理するModels. またモデルの追加をリスナたちに通知もしますので、Listnerも管理している
 * 
 * @author Masatomi KINO
 * @version $Revision$
 */
public class Models {
    private List<RequestResponseModel> models = new ArrayList<RequestResponseModel>();

    private List<ModelListener> listeners = new ArrayList<ModelListener>();

    public void addModel(RequestResponseModel model) {
        models.add(model);
        for (ModelListener listener : listeners) {
            listener.modelAdded(model);
        }

    }

    public void addModelListener(ModelListener listener) {
        listeners.add(listener);
    }

    public void removeModelListener(ModelListener listener) {
        listeners.remove(listener);
    }

}
