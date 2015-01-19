/******************************************************************************
 * Copyright (c) 2014 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//çÏê¨ì˙: 2014/11/05

package nu.mine.kino.jenkins.plugins.projectmanagement.utils;

import hudson.tasks.Mailer;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import nu.mine.kino.entity.ProjectUser;

import static nu.mine.kino.projects.utils.Utils.round;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class PMUtils {

    public static void addPV(ProjectUser projectUser, double value) {
        if (!Double.isNaN(value)) {
            double tmp = projectUser.getPlannedValue();
            tmp += value;
            projectUser.setPlannedValue(round(tmp));
        }
    }

    public static void addAC(ProjectUser projectUser, double value) {
        if (!Double.isNaN(value)) {
            double tmp = projectUser.getActualCost();
            tmp += value;
            projectUser.setActualCost(round(tmp));
        }

    }

    public static void addEV(ProjectUser projectUser, double value) {
        if (!Double.isNaN(value)) {
            double tmp = projectUser.getEarnedValue();
            tmp += value;
            projectUser.setEarnedValue(round(tmp));
        }

    }

    public static void addPV_p1(ProjectUser projectUser, double value) {
        if (!Double.isNaN(value)) {
            double tmp = projectUser.getPlannedValue_p1();
            tmp += value;
            projectUser.setPlannedValue_p1(round(tmp));
        }
    }

    public static void sendMail(String[] addresses, String message)
            throws UnsupportedEncodingException, MessagingException {
        MimeMessage mimeMessage = new MimeMessage(Mailer.descriptor()
                .createSession(), new ByteArrayInputStream(
                message.getBytes("UTF-8")));

        InternetAddress[] to = new InternetAddress[addresses.length];
        for (int i = 0; i < addresses.length; i++) {
            to[i] = new InternetAddress(addresses[i], true);
        }
        mimeMessage.setRecipients(Message.RecipientType.TO, to);
//        Transport.send(mimeMessage);
    }

}
