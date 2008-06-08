/*******************************************************************************
 * Copyright (c) 2008 Masatomi KINO. All rights reserved. 
 * $Id$
 ******************************************************************************/
// 作成日: 2008/05/18
package nu.mine.kino.mail.impl;

import java.io.BufferedInputStream;
import java.io.IOException;

import nu.mine.kino.mail.FilterException;
import nu.mine.kino.mail.FilterManager;


public class Main {
    /**
     * @param args
     * @throws IOException
     * @throws FilterException
     */
    public static void main(String[] args) throws FilterException {
        String mailData = createStr();
        FilterManager filterManager = new FilterManager();
        // この箇所で、フィルタを追加することも可能。
        filterManager.doFilters(mailData);
        System.out.println(mailData);
    }

    private static String createStr() {
        BufferedInputStream in = new BufferedInputStream(System.in);
        try {
            int size = in.available();
            byte[] bytes = new byte[size];
            in.read(bytes);
            return new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
