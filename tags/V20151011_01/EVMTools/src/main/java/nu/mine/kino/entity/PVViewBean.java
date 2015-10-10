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
 * ‰æ–Ê‚ÅŽg—p‚·‚éPVî•ñ
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 */
public class PVViewBean extends BasePVViewBean implements Validatable {

    @Override
    public boolean isValid(Object... conditions) {
        double pv = getPlannedValue();
        double pvp1 = getPlannedValue_p1();
        double pvp2 = getPlannedValue_p2();
        double pvp3 = getPlannedValue_p3();
        double pvm1 = getPlannedValue_m1();
        double pvm2 = getPlannedValue_m2();
        double pvm3 = getPlannedValue_m3();
        if (Utils.isNonZeroNumeric(pv) || Utils.isNonZeroNumeric(pvp1)
                || Utils.isNonZeroNumeric(pvp2) || Utils.isNonZeroNumeric(pvp3)
        // || Utils.isNonZeroNumeric(pvm1)
        // || Utils.isNonZeroNumeric(pvm2)
        // || Utils.isNonZeroNumeric(pvm3)
        ) {
            return true;
        } else if (conditions instanceof String[] && conditions.length > 0) {
            return Utils.contains(getTaskName(), (String[]) conditions);
        }
        return false;
    }

}