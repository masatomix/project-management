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
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 */
public class PVBean extends BasePVBean implements Validatable {

    @Override
    public boolean isValid(Object... conditions) {
        return Utils.isNonZeroNumeric(getPlannedValue());
    }
}