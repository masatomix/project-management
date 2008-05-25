package nu.mine.kino.mail.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import nu.mine.kino.mail.FilterException;
import nu.mine.kino.mail.FilterManager;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.log4j.Logger;

/*******************************************************************************
 * Copyright (c) 2008 Masatomi KINO. All rights reserved. $Id$
 ******************************************************************************/
// 作成日: 2008/05/18
public class Main {

    private static Logger logger = Logger.getLogger(Main.class);

    /**
     * @param args
     * @throws IOException
     * @throws FilterException
     */
    public static void main(String[] args) throws IOException, FilterException {
        String mailData = createStr();
        FilterManager filterManager = new FilterManager();
        // この箇所で、フィルタを追加することも可能。
        try {
            filterManager.doFilters(mailData);
        } catch (FilterException e) {
            logger.error(e);
            // mailして再スロー
            send(e);
            throw e;
        }
        System.out.println(mailData);
    }

    public static void send(FilterException e) throws IOException {

        String smtp = getMailInfo("smtp"); // SMTPサーバ
        String from = getMailInfo("from"); // 送信元メールアドレス
        String to = getMailInfo("to"); // 宛先メールアドレス
        String subject = createSubject(e);// メール題名
        String body = createBody(e);// メール本文

        logger.debug("smtp: " + smtp);
        logger.debug("from: " + from);
        logger.debug("to: " + to);
        logger.debug("subject: " + subject);

        try {
            // メール送信準備
            Properties props = new Properties();
            props.put("mail.smtp.host", smtp);
            Session session = Session.getDefaultInstance(props, null);

            // メール作成
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addFrom(InternetAddress.parse(from));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress
                    .parse(to));
            mimeMessage.setSubject(subject, "iso-2022-jp");
            mimeMessage.setText(body, "iso-2022-jp");
            mimeMessage.setSentDate(new Date());

            // 送信
            Transport.send(mimeMessage);

        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    private static String createSubject(FilterException e) {
        StringBuffer buf = new StringBuffer();
        buf.append("Error From MailFilter: ");
        buf.append(e.getMessage());
        return new String(buf);
    }

    private static String createBody(FilterException e)
            throws UnsupportedEncodingException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(baos, false, "iso-2022-jp"));// メールに出力するので、iso-2022-jpで
        String trace = baos.toString();

        StringBuffer buf = new StringBuffer();
        buf.append(trace);
        buf.append("\n-------------------------\n");
        buf.append(e.getMailData());

        return new String(buf);
    }

    private static String getMailInfo(String key) throws IOException {
        ExtendedProperties props = new ExtendedProperties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream resourceAsStream = loader
                .getResourceAsStream("mail.properties");
        InputStream in = new BufferedInputStream(resourceAsStream);
        props.load(in);
        return props.getString(key);
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
