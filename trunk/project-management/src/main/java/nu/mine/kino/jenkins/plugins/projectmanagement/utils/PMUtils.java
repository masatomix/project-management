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
import hudson.FilePath;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.TopLevelItem;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.model.FreeStyleProject;
import hudson.model.Hudson;
import hudson.tasks.Builder;
import hudson.tasks.Mailer;
import hudson.util.DescribableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
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
import nu.mine.kino.jenkins.plugins.projectmanagement.PMConstants;
import nu.mine.kino.jenkins.plugins.projectmanagement.EVMToolsBuilder;
import nu.mine.kino.jenkins.plugins.projectmanagement.EVMToolsBuilder.DescriptorImpl;
import nu.mine.kino.jenkins.plugins.projectmanagement.ProjectSummaryAction;
import nu.mine.kino.jenkins.plugins.projectmanagement.RedmineEVMToolsBuilder;
import nu.mine.kino.projects.ExcelProjectCreator;
import nu.mine.kino.projects.JSONProjectCreator;
import nu.mine.kino.projects.ProjectCreator;
import nu.mine.kino.projects.ProjectException;
import nu.mine.kino.projects.utils.ProjectUtils;
import nu.mine.kino.projects.utils.ReadUtils;
import nu.mine.kino.projects.utils.Utils;
import nu.mine.kino.projects.utils.ViewUtils;
import nu.mine.kino.projects.utils.WriteUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;

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
            return getBaseDate(new ExcelProjectCreator(f));
        } catch (ProjectException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getBaseDateFromJSON(File f) {
        try {
            return getBaseDate(new JSONProjectCreator(f));
        } catch (ProjectException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getBaseDateFromExcelWithPoi(File file) {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(in);
            Sheet sheet = workbook.getSheetAt(0);
            Name name = workbook.getName("雷線基準日");
            CellReference cellRef = new CellReference(name.getRefersToFormula());
            Row row = sheet.getRow(cellRef.getRow());
            Cell baseDateCell = row.getCell(cellRef.getCol());
            // System.out.println("cellが日付か:"
            // + PoiUtil.isCellDateFormatted(baseDateCell));
            Date baseDate = baseDateCell.getDateCellValue();
            return baseDate;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
        return null;
    }

    public static Date getBaseDate(ProjectCreator creator) {
        try {
            return creator.createProject().getBaseDate();
        } catch (ProjectException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getBaseDateFromBaseDateFile(File file) {
        try {
            String string = ReadUtils.readFile(file);
            return DateUtils.parseDate(string, new String[] { "yyyyMMdd" });
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * そのビルドを含むプロジェクトの基準日ファイルをさがし、基準日を取得する。
     * 
     * @param build
     * @throws IOException
     */
    public static File findBaseDateFile(AbstractProject project)
            throws IOException {
        String shimeFileName = PMConstants.DATE_DAT_FILENAME;
        AbstractBuild<?, ?> prevBuild = PMUtils.findBuild(project,
                shimeFileName);

        if (prevBuild != null) {
            File target = new File(prevBuild.getRootDir(), shimeFileName);
            return target;
        }
        return null;
    }

    /**
     * そのビルドを含むプロジェクトの基準日ファイルをさがし、基準日を取得する。
     * 
     * @param build
     * @throws IOException
     */
    public static File findBaseDateFile(AbstractBuild build) throws IOException {
        AbstractProject project = build.getProject();
        String shimeFileName = PMConstants.DATE_DAT_FILENAME;
        AbstractBuild<?, ?> prevBuild = PMUtils.findBuild(project,
                shimeFileName);

        if (prevBuild != null) {
            File target = new File(prevBuild.getRootDir(), shimeFileName);
            return target;
        }
        return null;
    }

    /**
     * そのプロジェクトで、シメに使われたビルドの中で最新のビルドを返す。
     * 
     * @param project
     * @return
     * @throws IOException
     */
    public static AbstractBuild<?, ?> findNewestBuild(AbstractProject project)
            throws IOException {
        String shimeFileName = PMConstants.DATE_DAT_FILENAME;
        AbstractBuild<?, ?> prevBuild = PMUtils.findBuild(project,
                shimeFileName);

        if (prevBuild != null) {
            return prevBuild;
        }
        return null;
    }

    /**
     * ワークスペースにある基準日ファイルを探すメソッド。
     * 
     * @param build
     * @return
     * @throws IOException
     * @Deprecated
     */
    public static FilePath findBaseDateFile1(AbstractBuild build)
            throws IOException {
        String shimeFileName = PMConstants.DATE_DAT_FILENAME;
        FilePath root = build.getModuleRoot(); // ワークスペースのルート
        FilePath shimeFile = new FilePath(root, shimeFileName); // 基準日ファイル
        return shimeFile;
    }

    /**
     * このプロジェクトが持つビルドの中で、該当するファイル名がルートディレクトリに存在する、最新のビルドを返します。
     * 
     * @param project
     * @param fileName
     * @return
     */
    public static AbstractBuild<?, ?> findBuild(AbstractProject<?, ?> project,
            String fileName) {
        AbstractBuild<?, ?> b = project.getLastSuccessfulBuild();
        while (b != null) {
            if (new File(b.getRootDir(), fileName).exists()) {
                return b;
            } else {
                b = b.getPreviousBuild();
            }
        }
        return null;
    }

    public static <ACTION extends Action> ACTION findActionByUrlEndsWith(
            AbstractBuild<?, ?> b, Class<ACTION> type, String suffix) {
        ACTION ret = null;
        for (Action a : b.getActions()) {
            if (type.isInstance(a) && a.getUrlName().endsWith(suffix)) {
                ret = type.cast(a);
            }
            // ProjectSummaryAction だけの特別処理。
            else if (a instanceof ProjectSummaryAction) {
                if (a.getUrlName().endsWith("null")) {
                    ret = type.cast(a);
                }
            }
        }
        return ret;
    }

    /**
     * 直近のURLがsuffixであるビルドのアクション。
     * 
     * @param project
     * @param suffix
     * @return 正常終了したビルドのうち、URLが baseで終わる ProjectSummaryAction
     */
    public static ProjectSummaryAction getMostRecentSummaryAction(
            AbstractProject<?, ?> project, String suffix) {
        final AbstractBuild<?, ?> tb = project.getLastSuccessfulBuild();
        AbstractBuild<?, ?> b = project.getLastBuild();
        while (b != null) {
            ProjectSummaryAction a = findActionByUrlEndsWith(b,
                    ProjectSummaryAction.class, suffix);
            if (a != null)
                return a;
            if (b == tb)
                // if even the last successful build didn't produce the test
                // result,
                // that means we just don't have any tests configured.
                return null;
            b = b.getPreviousBuild();
        }
        return null;
    }

    /**
     * 指数値 が0.98以上なら晴れ、0.92以上0.98未満なら曇り、0.92未満なら雨
     * 
     * @param d
     *            指数値
     * @return
     */
    public static String choiceWeatherIconFileName(double d) {
        if (Double.isNaN(d)) {
            return null;
        }
        if (d >= 0.98) {
            return "/16x16/health-80plus.png";
        } else if (d >= 0.92 && d < 0.98) {
            return "/16x16/health-40to59.png";
        } else {
            return "/16x16/health-20to39.png";
        }
    }

    /**
     * 渡されたプロジェクトのうち、渡されたファイルがビルドディレクトリに存在する、直近のビルドを探して、返します。
     * 
     * @param project
     * @param fileName
     * @param out
     * @param err
     * @return
     */
    public static String findSeriesFile(AbstractProject<?, ?> project,
            String fileName, PrintStream out, PrintStream err) {
        AbstractBuild<?, ?> build = PMUtils.findBuild(project, fileName);
        if (build == null) {
            out.printf("EVM時系列情報ファイル(%s)がプロジェクト上に存在しないので、ファイルを新規作成します。\n",
                    fileName);
            return null;
        } else {
            out.printf("EVM時系列情報ファイル(%s)が ビルド #%s 上に見つかりました。\n", fileName,
                    build.getNumber());
        }
        try {
            return ReadUtils.readFile(new File(build.getRootDir(), fileName));
        } catch (IOException e) {
            err.println("EVM時系列情報ファイルを探す際にエラーが発生したので、ファイルを新規作成します。");
        }
        return null;
    }

    public static void writeBaseDateFile(String baseDateStr,
            final AbstractBuild<?, ?> shimeBuild, PrintStream out) {
        WriteUtils
                .writeFile(baseDateStr.getBytes(), new File(shimeBuild
                        .getRootDir().getAbsolutePath(),
                        PMConstants.DATE_DAT_FILENAME));
        out.printf("基準日ファイル(%s)をビルド #%s に書き込みました。\n",
                PMConstants.DATE_DAT_FILENAME, shimeBuild.getNumber());
        out.printf("書き込み先: %s \n", shimeBuild.getRootDir().getAbsolutePath());
    }

    /**
     * 引数の Jenkinsのプロジェクトについて、EVM Excelのファイル名を返すメソッド
     * 
     * @param project
     * @return
     */
    public static String findProjectFileName(AbstractProject<?, ?> project) {
        if (project instanceof FreeStyleProject) {
            DescribableList<Builder, Descriptor<Builder>> buildersList = ((FreeStyleProject) project)
                    .getBuildersList();
            EVMToolsBuilder builder = buildersList.get(EVMToolsBuilder.class);
            if (builder != null) {
                return builder.getName();
            }
        }
        return null;

    }

    /**
     * オリジナルのExcelファイル名から、直近読み込んだファイル(JSONファイル)のファイル名を生成する
     * 
     * @param originalExcelFileName
     * @return
     */
    public static String getPreviousJsonFileName(String originalExcelFileName) {
        return ProjectUtils.findJSONFileName(originalExcelFileName) + "."
                + PMConstants.TMP_EXT;
    }

    public static String appendData(String prevData, int buildNumber,
            String baseDateStr) {
        StringBuffer buffer = new StringBuffer().append(baseDateStr)
                .append("\t").append(buildNumber);
        if (prevData != null) {
            buffer.append("\n").append(prevData);
        }
        return new String(buffer);
    }

    public static void writeSeriesFile(AbstractProject<?, ?> project,
            String baseDateStr, String fileName,
            final AbstractBuild<?, ?> shimeBuild, PrintStream out,
            PrintStream err) {
        // stdout.printf("[%s]\n",
        // shimeBuild.getRootDir().getAbsolutePath());
        // stdout.printf("[%s]:[%s]:[%s]\n", baseDateStr,
        // shimeBuild.getNumber(), shimeBuild.getId());
        String prevData = PMUtils.findSeriesFile(project, fileName, out, err);
        String currentData = PMUtils.appendData(prevData,
                shimeBuild.getNumber(), baseDateStr);
        File file = new File(shimeBuild.getRootDir().getAbsolutePath(),
                fileName);
        WriteUtils.writeFile(currentData.getBytes(), file);
        out.printf("EVM時系列情報ファイル(%s)に情報を追記してビルド #%s に書き込みました。\n", fileName,
                shimeBuild.getNumber());
        out.printf("書き込み先: #%s \n", shimeBuild.getRootDir().getAbsolutePath());
    }

    public static AbstractProject<?, ?> findProject(String projectName) {
        List<TopLevelItem> items = Jenkins.getInstance().getItems();
        for (TopLevelItem item : items) {
            if (item instanceof FreeStyleProject) {
                FreeStyleProject project = (FreeStyleProject) item;
                if (item.getName().equals(projectName)) {
                    return project;
                }
            }
        }
        return null;
    }

    public static List<AbstractProject<?, ?>> findProjectsWithEVMToolsBuilder() {
        List<AbstractProject<?, ?>> returnList = new ArrayList<AbstractProject<?, ?>>();
        List<TopLevelItem> items = Jenkins.getInstance().getItems();
        for (TopLevelItem item : items) {
            if (item instanceof FreeStyleProject) {
                FreeStyleProject project = (FreeStyleProject) item;
                if (!project.isDisabled()) {
                    DescribableList<Builder, Descriptor<Builder>> buildersList = project
                            .getBuildersList();
                    EVMToolsBuilder builder = buildersList
                            .get(EVMToolsBuilder.class);
                    if (builder != null) {
                        returnList.add(project);
                    }
                }
            }
        }
        return returnList;
    }
}
