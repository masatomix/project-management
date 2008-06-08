/*******************************************************************************
 * Copyright (c) 2008 Masatomi KINO. All rights reserved. $Id: Main.java 158
 * 2008-06-08 14:59:24Z masatomix $
 ******************************************************************************/
// 作成日: 2008/05/18
package nu.mine.kino.mail.impl;

import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.IOException;

import nu.mine.kino.mail.FilterException;
import nu.mine.kino.mail.FilterManager;

public class Main {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(Main.class);

    /**
     * @param args
     * @throws IOException
     * @throws FilterException
     */
    public static void main(String[] args) throws FilterException {
        logger.debug("main(String[]) - start");

        String mailData = createStr();
        FilterManager filterManager = new FilterManager();
        // この箇所で、フィルタを追加することも可能。
        filterManager.doFilters(mailData);
        System.out.println(mailData);

        logger.debug("main(String[]) - end");
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
