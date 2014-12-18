/******************************************************************************
 * Copyright (c) 2014 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//作成日: 2014/12/17

package nu.mine.kino.plugin.jenkins.heartbeat;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    public PreferenceInitializer() {
        // TODO 自動生成されたコンストラクター・スタブ
    }

    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = HeartbeatPlugin.getDefault()
                .getPreferenceStore();
        store.setDefault(Constants.BASE_URL, "http://build:8080/jenkins/");
        store.setDefault(Constants.USER_NAME, "");
        store.setDefault(Constants.PASSWORD, "");
        store.setDefault(Constants.PERIOD, "60");
        store.setDefault(Constants.SHOW_DIALOG, false);
    }

}
