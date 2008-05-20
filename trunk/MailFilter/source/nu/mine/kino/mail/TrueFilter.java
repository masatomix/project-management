/*******************************************************************************
 * Copyright (c) 2008 Masatomi KINO. All rights reserved. $Id$
 ******************************************************************************/
// ì¬“ú: 2008/05/19
package nu.mine.kino.mail;

import java.util.ArrayList;
import java.util.List;

public class TrueFilter implements IMailFilter {
    List<String> whiteList = new ArrayList<String>();

    public String doFilter(String mailData) throws FilterException {
        return mailData;
    }
}
