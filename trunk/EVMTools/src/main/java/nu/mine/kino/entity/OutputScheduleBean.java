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

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 */
public class OutputScheduleBean extends TextScheduleBean {
    public String getScheduledEndDateStr() {
        return getScheduledEndDate() != null ? DateFormatUtils.format(
                getScheduledEndDate(), "yyyy/MM/dd") : null;
    }

    public String getScheduledStartDateStr() {
        return getScheduledStartDate() != null ? DateFormatUtils.format(
                getScheduledStartDate(), "yyyy/MM/dd") : null;
    }
}
