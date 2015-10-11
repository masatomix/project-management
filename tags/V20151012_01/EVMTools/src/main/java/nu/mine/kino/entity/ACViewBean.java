/******************************************************************************
 * Copyright (c) 2008-2014 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 ******************************************************************************/

package nu.mine.kino.entity;

import nu.mine.kino.projects.utils.Utils;

/**
 * ‰æ–Ê‚ÅŽg—p‚·‚éACî•ñ
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 */
public class ACViewBean extends BaseACViewBean implements Validatable {

    @Override
    public boolean isValid(Object... conditions) {
        double ac = getActualCost();
        if (Utils.isNonZeroNumeric(ac)) {
            return true;
        } else if (conditions instanceof String[] && conditions.length > 0) {
            return Utils.contains(getTaskName(), (String[]) conditions);
        }
        return false;
    }
}