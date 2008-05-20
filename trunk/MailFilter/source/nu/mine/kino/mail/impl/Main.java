package nu.mine.kino.mail.impl;

import java.io.BufferedInputStream;
import java.io.IOException;

import nu.mine.kino.mail.FilterException;
import nu.mine.kino.mail.FilterManager;

/*******************************************************************************
 * Copyright (c) 2008 Masatomi KINO. All rights reserved. $Id$
 ******************************************************************************/
// çÏê¨ì˙: 2008/05/18
public class Main {

    /**
     * @param args
     * @throws IOException
     * @throws FilterException
     */
    public static void main(String[] args) throws IOException {
        String mailData = createStr();
        try {
            FilterManager filterManager = new FilterManager();
            filterManager.addFilter(new AddressFilter());
            // filterManager.addFilter(new TrueFilter());
            filterManager.doFilters(mailData);
            System.out.println(mailData);
        } catch (FilterException e) {
            e.printStackTrace();
        }
    }

    private static String createStr() {
        BufferedInputStream in = new BufferedInputStream(System.in);
        try {
            int size = in.available();
            byte[] bytes = new byte[size];
            in.read(bytes);
            return new String(bytes, "ISO-2022-JP");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
