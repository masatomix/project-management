/*******************************************************************************
 * Copyright (c) 2008 Masatomi KINO. All rights reserved. 
 * $Id$
 ******************************************************************************/
// 作成日: 2008/05/19
package nu.mine.kino.mail;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.log4j.Logger;

public class FilterManager {
    private List<IMailFilter> filters = new LinkedList<IMailFilter>();

    private static Logger logger = Logger.getLogger(FilterException.class);

    private ExtendedProperties props = new ExtendedProperties();

    public FilterManager() {
        init();
        initMailInfo();
    }

    private void init() {
        ExtendedProperties props = new ExtendedProperties();
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream resourceAsStream = loader
                    .getResourceAsStream("filter.properties");
            InputStream in = new BufferedInputStream(resourceAsStream);
            props.load(in);
        } catch (IOException e) {
            // ファイルがないとかですね。
            logger.warn(e);
        }
        // 初期化済み(loadが成功している)なら
        if (props.isInitialized()) {
            String[] stringArray = props.getStringArray("filterClass");
            for (String name : stringArray) {
                try {
                    IMailFilter filter = (IMailFilter) Class.forName(name)
                            .newInstance();
                    filters.add(filter);
                } catch (InstantiationException e) {
                    logger.error(e);
                } catch (IllegalAccessException e) {
                    logger.error(e);
                } catch (ClassNotFoundException e) {
                    logger.error(e);
                }
            }// 初期化でエラーになっても、とりあえず何も設定しなかったとうことで先に進む
        }
    }

    private void initMailInfo() {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream resourceAsStream = loader
                    .getResourceAsStream("mail.properties");
            InputStream in = new BufferedInputStream(resourceAsStream);
            props.load(in);
        } catch (IOException e) {
            // ファイルがない場合は、メールしないという意味。
            logger.warn(e);
            logger.info("mail.propertiesがパス上に見つからなかったため、メール送信は行わないことにします。");
        }
    }

    public void addFilter(IMailFilter filter) {
        filters.add(filter);
    }

    /**
     * セットされたフィルタを順次実行するメソッドです。
     * 
     * @param mailData
     * @return
     * @throws FilterException
     */
    public String doFilters(String mailData) throws FilterException {
        if (!filters.isEmpty()) {
            String tmp = mailData;
            for (IMailFilter filter : filters) {
                logger.debug("実行するフィルタ名: " + filter.getClass().getName());
                try {
                    tmp = filter.doFilter(tmp);
                } catch (FilterException e) {
                    logger.error(e);
                    // mailして再スロー
                    send(e);
                    throw e;
                }
            }
            return tmp;
        }
        logger.warn("実行するフィルタが設定されていませんでした");
        return mailData;
    }

    public void send(FilterException e) {
        // 初期化済み(loadが成功している)なら
        if (props.isInitialized()) {
            String smtp = props.getString("smtp"); // SMTPサーバ
            String from = props.getString("from"); // 送信元メールアドレス
            String to = props.getString("to"); // 宛先メールアドレス
            String subject = createSubject(e);// メール題名
            logger.debug("smtp: " + smtp);
            logger.debug("from: " + from);
            logger.debug("to: " + to);
            logger.debug("subject: " + subject);
            try {
                String body = createBody(e);// メール本文
                // メール送信準備
                Properties props = new Properties();
                props.put("mail.smtp.host", smtp);
                Session session = Session.getDefaultInstance(props, null);

                // メール作成
                MimeMessage mimeMessage = new MimeMessage(session);
                mimeMessage.addFrom(InternetAddress.parse(from));
                mimeMessage.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(to));
                mimeMessage.setSubject(subject, "iso-2022-jp");
                mimeMessage.setText(body, "iso-2022-jp");
                mimeMessage.setSentDate(new Date());

                // 送信
                Transport.send(mimeMessage);

            } catch (Exception ee) {
                logger.error(ee);
            }
        } else {
            logger.warn("エラーは発生していますが、メールは送りません。");
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
}
