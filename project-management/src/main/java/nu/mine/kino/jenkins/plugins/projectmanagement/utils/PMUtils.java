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
//作成日: 2014/11/05

package nu.mine.kino.jenkins.plugins.projectmanagement.utils;

import static nu.mine.kino.projects.utils.Utils.round;
import hudson.AbortException;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.Hudson;
import hudson.tasks.Mailer;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import jenkins.model.Jenkins;
import nu.mine.kino.entity.PVACEVViewBean;
import nu.mine.kino.entity.Project;
import nu.mine.kino.entity.ProjectUser;
import nu.mine.kino.jenkins.plugins.projectmanagement.EVMToolsBuilder;
import nu.mine.kino.jenkins.plugins.projectmanagement.EVMToolsBuilder.DescriptorImpl;
import nu.mine.kino.jenkins.plugins.projectmanagement.ProjectSummaryAction;
import nu.mine.kino.jenkins.plugins.projectmanagement.RedmineEVMToolsBuilder;
import nu.mine.kino.projects.ExcelProjectCreator;
import nu.mine.kino.projects.ProjectException;
import nu.mine.kino.projects.utils.Utils;
import nu.mine.kino.projects.utils.ViewUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

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

    public static void sendMail(String[] addresses, String subject,
            String message) throws UnsupportedEncodingException,
            MessagingException {

        MimeMessage mimeMessage = new MimeMessage(Mailer.descriptor()
                .createSession());

        InternetAddress[] to = new InternetAddress[addresses.length];
        for (int i = 0; i < addresses.length; i++) {
            to[i] = new InternetAddress(addresses[i], true);
        }
        mimeMessage.setRecipients(Message.RecipientType.TO, to);
        mimeMessage.setSubject(subject, "ISO-2022-JP");
        mimeMessage.setText(message, "ISO-2022-JP");
        Transport.send(mimeMessage);
    }

    /**
     * {@link RedmineEVMToolsBuilder}と{@link EVMToolsBuilder}が一つの
     * {@link ProjectSummaryAction}を共有するため、取得するメソッドを共通化。
     * 
     * @param build
     * @return
     */
    public static ProjectSummaryAction getProjectSummaryAction(
            AbstractBuild build) {
        List<ProjectSummaryAction> projectSummaryActions = build
                .getActions(ProjectSummaryAction.class);
        ProjectSummaryAction action = null;
        if (projectSummaryActions.isEmpty()) {
            action = new ProjectSummaryAction(build);
            build.addAction(action);
        } else {
            action = projectSummaryActions.get(0);
        }
        return action;
    }

    public static void checkProjectAndMail(Project project,
            String otherAddresses, AbstractBuild build, BuildListener listener,
            boolean sendAll) throws IOException {

        // 参考 org.jenkinsci.plugins.tokenmacro.impl.BuildUrlMacro
        String BUILD_URL = new StringBuilder()
                .append(Hudson.getInstance().getRootUrl())
                .append(build.getUrl()).toString();
        String PROJECT_NAME = build.getProject().getName();
        String BUILD_NUMBER = String.valueOf(build.getNumber());

        String subject = String.format("%s - Build # %s のタスク", PROJECT_NAME,
                BUILD_NUMBER);
        String footer = String.format(
                "Check console output at %s to view the results.", BUILD_URL);
        // ///////////////// 以下メール送信系の処理

        String header = null;
        List<PVACEVViewBean> list = null;
        if (!sendAll) {
            list = ViewUtils.getIsCheckPVACEVViewList(project);
            header = "以下、期限が過ぎましたが完了していない要注意タスクです。 ";
        } else {
            list = ViewUtils.getPVACEVViewBeanList(project);
            header = "以下、条件に合致したタスクです。 ";
        }

        if (list.isEmpty()) {
            listener.getLogger().println("[EVM Tools] : 要注意タスクはありませんでした。");
            return;
        }
        StringBuffer messageBuf = new StringBuffer();
        messageBuf.append(header);
        messageBuf.append("\n");
        messageBuf.append("\n");
        messageBuf.append("--------------------");
        messageBuf.append("\n");
        messageBuf.append("担当者\tタスクID\tタスク名\t期限");
        messageBuf.append("\n");
        for (PVACEVViewBean bean : list) {
            Date scheduledEndDate = bean.getScheduledEndDate();
            String endDate = "未設定";
            if (scheduledEndDate != null) {
                endDate = DateFormatUtils
                        .format(scheduledEndDate, "yyyy/MM/dd");
            }
            String personInCharge = StringUtils.isEmpty(bean
                    .getPersonInCharge()) ? "未設定" : bean.getPersonInCharge();

            String line = String.format("%s\t%s\t%s\t%s", personInCharge,
                    bean.getTaskId(), bean.getTaskName(), endDate);
            messageBuf.append(line);
            messageBuf.append("\n");
        }
        messageBuf.append("--------------------");
        messageBuf.append("\n");
        messageBuf.append("\n");
        messageBuf.append(footer);

        String message = new String(messageBuf);
        listener.getLogger().println("[EVM Tools] : --- 要注意タスク--- ");
        listener.getLogger().println(message);
        listener.getLogger().println("[EVM Tools] : --- 要注意タスク--- ");

        DescriptorImpl descriptor = (DescriptorImpl) Jenkins.getInstance()
                .getDescriptor(EVMToolsBuilder.class);

        boolean useMail = !StringUtils.isEmpty(descriptor.getAddresses());
        listener.getLogger().println("[EVM Tools] メール送信する？ :" + useMail);
        String address = StringUtils.isEmpty(otherAddresses) ? descriptor
                .getAddresses() : otherAddresses;
        listener.getLogger().println("[EVM Tools] 宛先:" + address);
        listener.getLogger().println("[EVM Tools] 期限切れ以外も通知？:" + sendAll);

        if (useMail && !StringUtils.isEmpty(address)) {
            String[] addresses = Utils.parseCommna(address);
            for (String string : addresses) {
                System.out.printf("[%s]\n", string);
            }
            try {
                if (addresses.length > 0) {
                    PMUtils.sendMail(addresses, subject, message);
                } else {
                    String errorMsg = "メール送信に失敗しました。宛先の設定がされていません";
                    listener.getLogger().println("[EVM Tools] " + errorMsg);
                    throw new AbortException(errorMsg);
                }
            } catch (MessagingException e) {
                String errorMsg = "メール送信に失敗しました。「システムの設定」で E-mail 通知 の設定や宛先などを見直してください";
                listener.getLogger().println("[EVM Tools] " + errorMsg);
                throw new AbortException(errorMsg);

            }
        }
    }

    public static Date getBaseDateFromExcel(File f) {
        try {
            return new ExcelProjectCreator(f).createProject().getBaseDate();
        } catch (ProjectException e) {
            e.printStackTrace();
        }
        return null;
    }

}
