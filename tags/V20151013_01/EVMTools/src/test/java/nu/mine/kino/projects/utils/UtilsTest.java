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
//çÏê¨ì˙: 2014/10/15

package nu.mine.kino.projects.utils;

import java.io.FileNotFoundException;
import java.util.Date;

import nu.mine.kino.projects.ProjectException;

import org.junit.Test;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class UtilsTest {

    @Test
    public void test() {
        String pattern = "yyyyMMdd";
        Date baseDate = Utils.str2Date("20140101", pattern);

        Date startDate = Utils.str2Date("20140101", pattern);
        Date endDate = Utils.str2Date("20140110", pattern);

        checkDate(baseDate, startDate, endDate);

        baseDate = Utils.str2Date("20140110", pattern);
        checkDate(baseDate, startDate, endDate);

    }

    private static void checkDate(Date baseDate, Date startDate, Date endDate) {
        System.out.println(Utils.isBetween(baseDate, startDate, endDate, false,
                false));
        System.out.println(Utils.isBetween(baseDate, startDate, endDate, true,
                false));
        System.out.println(Utils.isBetween(baseDate, startDate, endDate, false,
                true));
        System.out.println(Utils.isBetween(baseDate, startDate, endDate, true,
                true));
    }
}
