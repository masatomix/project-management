/*******************************************************************************
 * Copyright (c) 2008 Masatomi KINO. All rights reserved. $Id$
 ******************************************************************************/
// çÏê¨ì˙: 2008/05/18
package nu.mine.kino.mail.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

public class Utils {

    public static String[] getHeader(String mailData, String headerName) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(mailData
                    .getBytes("ISO-2022-JP"));
            Session session = Session.getDefaultInstance(
                    new java.util.Properties(), null);
            MimeMessage message = new MimeMessage(session, in);
            return message.getHeader(headerName);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        BufferedInputStream in = new BufferedInputStream(System.in);
        try {
            int size = in.available();
            byte[] bytes = new byte[size];
            in.read(bytes);
            System.out.println(new String(bytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
