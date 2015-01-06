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
 * 画面で使用するAC情報
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 */
public class PVACEVViewBean extends BasePVACEVViewBean implements Validatable {
    /**
     * PV/AC/EV/進捗率すべてが、NaNか 0 なら false そうでなければ(つまり、なんらかの情報があれば) true
     * 
     * @param bean
     * @return
     */
    @Override
    public boolean isValid(Object... conditions) {
        double pv = getPlannedValue();
        double ac = getActualCost();
        double ev = getEarnedValue();
        double progressRate = getProgressRate();
        double pv_p1 = getPlannedValue_p1();

        // スケジュール期限が基準日よりあとなのに、100%になっていないもの
        if (getScheduledEndDate() != null) {
            if (this.getBaseDate().after(getScheduledEndDate())
                    && progressRate != 1.0) {
                return true;
            }
        }

        if (Utils.isNonZeroNumeric(pv) || Utils.isNonZeroNumeric(ac)
                || Utils.isNonZeroNumeric(ev)
                // || Utils.isNonZeroNumeric(progressRate)
                || Utils.isNonZeroNumeric(pv_p1)) {

            return true;
        } else if (conditions instanceof String[] && conditions.length > 0) {
            return Utils.contains(getTaskName(), (String[]) conditions);
        }
        return false;
    }
}