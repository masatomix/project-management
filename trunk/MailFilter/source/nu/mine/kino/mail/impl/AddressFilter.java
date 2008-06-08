/*******************************************************************************
 * Copyright (c) 2008 Masatomi KINO. All rights reserved. $Id:
 * AddressFilter.java 158 2008-06-08 14:59:24Z masatomix $
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
import org.apache.log4j.Logger;

public class AddressFilter implements IMailFilter {
    private static Logger logger = Logger.getLogger(AddressFilter.class);

    private List<String> whiteList = null;

    public AddressFilter() throws IOException {
        init();
    }

    private void init() {
        logger.debug("init() - start");

        ExtendedProperties props = new ExtendedProperties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream resourceAsStream = loader
                .getResourceAsStream("whitelist.properties");
        InputStream in = new BufferedInputStream(resourceAsStream);
        try {
            props.load(in);
        } catch (IOException e) {
            // ファイルがないとかですね。
            logger.warn(e);
        }
        if (props.isInitialized()) {
            String[] stringArray = props.getStringArray("address");
            whiteList = Arrays.asList(stringArray);
        }

        logger.debug("init() - end");
    }

    public String doFilter(String mailData) throws FilterException {
        logger.debug("doFilter(String) - start");

        if (mailData == null || "".equals(mailData)) {
            throw new IllegalArgumentException("メールデータが取得できませんでした。");
        }
        String path = Utils.getHeader(mailData, "Return-Path")[0];
        if (path == null || "".equals(path)) {
            throw new FilterException("Return-Path が取得できませんでした。", mailData);
        }

        if (whiteList == null || whiteList.isEmpty()) {
            logger.warn("whitelist.propertiesからデータを取得できなかったため、メールの遮断を行いません。");
            return mailData;
        }
        boolean flag = false;
        for (String mailAddress : whiteList) {
            if (trim(path).equals(mailAddress)) {
                flag = true;
            }
        }
        if (!flag) {
            throw new FilterException("Return-Path から取得されたアドレス: " + path
                    + " がアドレスリストにありません。", mailData);
        }

        logger.debug("doFilter(String) - end");
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
