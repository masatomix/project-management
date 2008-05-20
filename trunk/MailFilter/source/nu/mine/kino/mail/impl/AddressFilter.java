/*******************************************************************************
 * Copyright (c) 2008 Masatomi KINO. All rights reserved. $Id$
 ******************************************************************************/
// 作成日: 2008/05/19
package nu.mine.kino.mail.impl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import nu.mine.kino.mail.FilterException;
import nu.mine.kino.mail.IMailFilter;
import nu.mine.kino.mail.utils.Utils;

import org.apache.commons.collections.ExtendedProperties;

public class AddressFilter implements IMailFilter {
    private List<String> whiteList = null;

    public AddressFilter() throws IOException {
        init();
    }

    private void init() throws IOException {
        ExtendedProperties props = new ExtendedProperties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream resourceAsStream = loader
                .getResourceAsStream("whitelist.properties");
        InputStream in = new BufferedInputStream(resourceAsStream);
        props.load(in);
        String[] stringArray = props.getStringArray("address");
        whiteList = Arrays.asList(stringArray);
    }

    public String doFilter(String mailData) throws FilterException {
        if (mailData == null || "".equals(mailData)) {
            throw new FilterException("メールデータが取得できませんでした。");
        }
        String path = Utils.getHeader(mailData, "Return-Path")[0];
        if (path == null || "".equals(path)) {
            throw new FilterException("Return-Path が取得できませんでした。");
        }

        boolean flag = false;
        for (String mailAddress : whiteList) {
            if (trim(path).equals(mailAddress)) {
                flag = true;
            }
        }
        if (!flag) {
            throw new FilterException("Return-Path から取得されたアドレス: " + path
                    + " がアドレスリストにありません。");
        }
        return mailData;

    }

    private static final String patterns[] = { "<", ">", " " };

    private static String trim(String input) {
        String result = input;
        if (input != null && !"".equals(input)) {
            for (int i = 0; i < patterns.length; i++) {
                result = Pattern.compile(patterns[i]).matcher(result)
                        .replaceAll("");
            }
        }
        return result;
    }

}
