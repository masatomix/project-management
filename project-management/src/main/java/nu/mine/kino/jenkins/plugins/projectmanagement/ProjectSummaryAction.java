/******************************************************************************
 * Copyright (c) 2012 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//作成日: 2014/10/14

package nu.mine.kino.jenkins.plugins.projectmanagement;

import static nu.mine.kino.projects.utils.Utils.round;
import static nu.mine.kino.projects.utils.Utils.isNonZeroNumeric;
import hudson.model.Action;
import hudson.model.AbstractBuild;
import hudson.model.User;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

import jenkins.model.Jenkins;
import nu.mine.kino.entity.ACBean;
import nu.mine.kino.entity.ACBean2ACViewBean;
import nu.mine.kino.entity.ACViewBean;
import nu.mine.kino.entity.EVBean;
import nu.mine.kino.entity.EVBean2EVViewBean;
import nu.mine.kino.entity.EVMViewBean;
import nu.mine.kino.entity.EVViewBean;
import nu.mine.kino.entity.PVACEVViewBean;
import nu.mine.kino.entity.PVViewBean;
import nu.mine.kino.entity.Project;
import nu.mine.kino.entity.ProjectUser;
import nu.mine.kino.entity.Task2ACViewBean;
import nu.mine.kino.entity.Task2EVViewBean;
import nu.mine.kino.entity.TaskInformation;
import nu.mine.kino.entity.User2ProjectUser;
import nu.mine.kino.jenkins.plugins.projectmanagement.EVMToolsBuilder.DescriptorImpl;
import nu.mine.kino.jenkins.plugins.projectmanagement.utils.PMUtils;
import nu.mine.kino.projects.ACCreator;
import nu.mine.kino.projects.EVCreator;
import nu.mine.kino.projects.JSONProjectCreator;
import nu.mine.kino.projects.ProjectException;
import nu.mine.kino.projects.utils.ProjectUtils;
import nu.mine.kino.projects.utils.Utils;
import nu.mine.kino.projects.utils.ViewUtils;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class ProjectSummaryAction implements Action {

    private static final long serialVersionUID = 1L;

    private final AbstractBuild<?, ?> owner;

    private String name;

    private String basePrefix = PMConstants.BASE;

    private String redmineFileName;

    public ProjectSummaryAction(AbstractBuild<?, ?> owner) {
        this.owner = owner;
    }

    @Override
    public String getIconFileName() {
        // return "user.png";
        return "/plugin/project-management/images/24x24/user_suit.png";
    }

    @Override
    public String getDisplayName() {
        return "プロジェクトサマリー";
    }

    @Override
    public String getUrlName() {
        return "project-summary";
    }

    public AbstractBuild<?, ?> getOwner() {
        return owner;
    }

    public EVMViewBean getCurrentPVACEV() {
        try {
            double pv = 0.0d;
            double ac = 0.0d;
            double ev = 0.0d;
            double bac = 0.0d;

            Project project = null;
            if (!StringUtils.isEmpty(name)) {
                project = getProject(name);
            } else if (!StringUtils.isEmpty(redmineFileName)) {
                project = getProject(redmineFileName);
            }

            Date baseDate = project.getBaseDate();
            TaskInformation[] taskInformations = project.getTaskInformations();

            System.out.println("------");
            for (TaskInformation info : taskInformations) {
                double calculatePVs = ProjectUtils.calculatePVs(info, baseDate);
                pv += (Double.isNaN(calculatePVs) ? 0.0d : calculatePVs);
                ac += (Double.isNaN(info.getAC().getActualCost()) ? 0.0d : info
                        .getAC().getActualCost());
                ev += (Double.isNaN(info.getEV().getEarnedValue()) ? 0.0d
                        : info.getEV().getEarnedValue());

                System.out.println(info.getTaskId() + "\t" + calculatePVs
                        + "\t" + info.getAC().getActualCost() + "\t"
                        + info.getEV().getEarnedValue());

                double bacPerTask = info.getTask().getNumberOfManDays();
                bac += (Double.isNaN(bacPerTask) ? 0.0d : bacPerTask);
            }
            System.out.println("------");

            EVMViewBean bean = new EVMViewBean();

            bean.setPlannedValue(round(pv, 2));
            bean.setActualCost(round(ac, 2));
            bean.setEarnedValue(round(ev, 2));
            bean.setBac(round(bac, 2));
            bean.setBaseDate(baseDate);

            double sv = Double.NaN;
            double cv = Double.NaN;
            double spi = Double.NaN;
            double cpi = Double.NaN;
            double etc = Double.NaN;
            double eac = Double.NaN;
            double vac = Double.NaN;

            if (isNonZeroNumeric(pv) && isNonZeroNumeric(ac)
                    && isNonZeroNumeric(ev)) {
                sv = ev - pv;
                cv = ev - ac;
                spi = ev / pv;
                cpi = ev / ac;
                etc = (bac - ev) / cpi;
                eac = ac + etc;
                vac = bac - eac;
            }

            bean.setSv(round(sv, 2));
            bean.setCv(round(cv, 2));

            bean.setSpi(round(spi, 3));
            bean.setCpi(round(cpi, 3));

            bean.setEtc(round(etc, 2));
            bean.setEac(round(eac, 2));
            bean.setVac(round(vac, 2));

            return bean;
        } catch (ProjectException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        return null;
    }

    public ProjectUser[] getUsers() {
        PVACEVViewBean[] pvacevViews = getPVACEVViews();

        // System.out.println(User.get("", false, Collections.emptyMap())
        // .getId());
        // return all.toArray(new User[all.size()]);
        Map<String, ProjectUser> userMap = new HashMap<String, ProjectUser>();
        try {
            Project project = getProject(name);
            if (project == null) {
                return new ProjectUser[0];
            }

            TaskInformation[] taskInformations = project.getTaskInformations();
            for (TaskInformation taskInfo : taskInformations) {
                String personInCharge = taskInfo.getTask().getPersonInCharge();
                User user = searchById(personInCharge);

                ProjectUser projectUser = null;
                if (userMap.containsKey(personInCharge)) {
                    projectUser = userMap.get(personInCharge);
                } else {
                    projectUser = User2ProjectUser.convert(user);
                    userMap.put(personInCharge, projectUser);
                }

                PVACEVViewBean bean = ViewUtils.findPVACEVViewBeanByTaskId(
                        pvacevViews, taskInfo.getTaskId());

                if (bean != null) {
                    PMUtils.addPV(projectUser, bean.getPlannedValue());
                    PMUtils.addAC(projectUser, bean.getActualCost());
                    PMUtils.addEV(projectUser, bean.getEarnedValue());
                    PMUtils.addPV_p1(projectUser, bean.getPlannedValue_p1());
                }
            }
            return userMap.values().toArray(new ProjectUser[userMap.size()]);
            // return userMap.toArray(new ProjectUser[userMap.size()]);
        } catch (ProjectException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        return new ProjectUser[0];
    }

    private User searchById(String personInCharge) {

        // ID指定で検索しても
        User user = null;
        Collection<User> all = User.getAll();
        for (User tmpUser : all) {
            // System.out.println(tmpUser.getId());
            if (tmpUser.getId().equals(personInCharge)) {
                user = tmpUser;
                break;
            }
        }

        // なかったら作る、のメソッドが、名前もヒットさせてしまう、、、。
        // すでにFullNameに存在する文字のIDのユーザは作れない??
        if (user == null) {
            user = User.get(personInCharge, true, Collections.emptyMap()); // この検索メソッドは、FullName優先。ID優先じゃない。。
            // System.out.println("res: "+MailAddressResolver.resolveFast(user));
        }
        return user;
    }

    private Map<String, Project> map = new HashMap<String, Project>();

    public synchronized Project getProject(String name) throws ProjectException {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        if (map.containsKey(name)) {
            return map.get(name);
        }
        File target = new File(owner.getRootDir(), name);
        if (!target.exists()) {
            return null;
        }
        Project targetProject = new JSONProjectCreator(target).createProject();
        map.put(name, targetProject);
        return targetProject;
    }

    public ACViewBean[] getPreviousACViews() {
        if (StringUtils.isEmpty(name)) {
            return new ACViewBean[0];
        }
        try {

            List<ACViewBean> retList = new ArrayList<ACViewBean>();
            File target = new File(owner.getRootDir(), name);
            Project targetProject = getProject(name);
            File base = new File(owner.getRootDir(), basePrefix + "_" + name);
            List<ACBean> filterList = ACCreator.createACListFromJSON(target,
                    base);

            for (ACBean acBean : filterList) {
                ACViewBean bean = new ACViewBean();
                ACBean2ACViewBean.convert(acBean, bean);
                TaskInformation taskInformation = ProjectUtils
                        .getTaskInformation(targetProject, acBean.getTaskId());
                // if (taskInformation != null) {
                Task2ACViewBean.convert(taskInformation.getTask(), bean);
                // }
                retList.add(bean);
            }

            DescriptorImpl descriptor = (DescriptorImpl) Jenkins.getInstance()
                    .getDescriptor(EVMToolsBuilder.class);
            String[] prefixArray = Utils.parseCommna(descriptor.getPrefixs());
            retList = ProjectUtils.filterList(retList, prefixArray);
            return retList.toArray(new ACViewBean[retList.size()]);

        } catch (ProjectException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        return null;
    }

    public EVViewBean[] getPreviousEVViews() {
        if (StringUtils.isEmpty(name)) {
            return new EVViewBean[0];
        }
        try {
            List<EVViewBean> retList = new ArrayList<EVViewBean>();
            File target = new File(owner.getRootDir(), name);
            Project targetProject = getProject(name);

            File base = new File(owner.getRootDir(), basePrefix + "_" + name);
            List<EVBean> filterList = EVCreator.createEVListFromJSON(target,
                    base);
            // List<EVBean> filterList = ProjectUtils.filterEV(list);

            for (EVBean evBean : filterList) {
                EVViewBean bean = new EVViewBean();
                EVBean2EVViewBean.convert(evBean, bean);
                TaskInformation taskInformation = ProjectUtils
                        .getTaskInformation(targetProject, evBean.getTaskId());
                // if (taskInformation != null) {
                Task2EVViewBean.convert(taskInformation.getTask(), bean);
                // }
                retList.add(bean);
            }

            DescriptorImpl descriptor = (DescriptorImpl) Jenkins.getInstance()
                    .getDescriptor(EVMToolsBuilder.class);
            String[] prefixArray = Utils.parseCommna(descriptor.getPrefixs());
            retList = ProjectUtils.filterList(retList, prefixArray);
            return retList.toArray(new EVViewBean[retList.size()]);

        } catch (ProjectException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } finally {
        }
        return null;
    }

    public Date getBaseDate() {
        try {
            String inputName = (!StringUtils.isEmpty(name)) ? name
                    : redmineFileName;
            Project project = getProject(inputName);
            return project.getBaseDate();
        } catch (ProjectException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 直近のタスク(PV)画面のためのデータを返す。
     * 
     * @return
     */
    public PVViewBean[] getPVViews() {

        DescriptorImpl descriptor = (DescriptorImpl) Jenkins.getInstance()
                .getDescriptor(EVMToolsBuilder.class);
        String[] prefixArray = Utils.parseCommna(descriptor.getPrefixs());
        List<PVViewBean> retList = new ArrayList<PVViewBean>();
        try {
            if (!StringUtils.isEmpty(name)) {
                Project project = getProject(name);
                List<PVViewBean> list = ProjectUtils.filterList(
                        ViewUtils.getPVViewBeanList(project), prefixArray);
                if (!list.isEmpty()) {
                    retList.addAll(list);
                }
            }

            if (!StringUtils.isEmpty(redmineFileName)) {
                Project project = getProject(redmineFileName);
                List<PVViewBean> list = ViewUtils.getPVViewBeanList(project);
                if (!list.isEmpty()) {
                    retList.addAll(list);
                }
                // }
            }
            return retList.toArray(new PVViewBean[retList.size()]);
        } catch (ProjectException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        return new PVViewBean[0];
    }

    /**
     * 直近の状況(PV/AC/EV)のデータを返す。
     * 
     * @return
     */
    public PVACEVViewBean[] getPVACEVViews() {
        DescriptorImpl descriptor = (DescriptorImpl) Jenkins.getInstance()
                .getDescriptor(EVMToolsBuilder.class);
        String[] prefixArray = Utils.parseCommna(descriptor.getPrefixs());

        List<PVACEVViewBean> retList = new ArrayList<PVACEVViewBean>();
        try {
            if (!StringUtils.isEmpty(name)) {
                Project targetProject = getProject(name);
                Project baseProject = getProject(basePrefix + "_" + name);
                List<PVACEVViewBean> list = ProjectUtils.filterList(ViewUtils
                        .getPVACEVViewBeanList(targetProject, baseProject),
                        prefixArray);
                if (!list.isEmpty()) {
                    retList.addAll(list);
                }
            }
            if (!StringUtils.isEmpty(redmineFileName)) {
                Project targetProject = getProject(redmineFileName);
                List<PVACEVViewBean> list = ViewUtils
                        .getPVACEVViewBeanList(targetProject);
                if (!list.isEmpty()) {
                    retList.addAll(list);
                }
            }
            return retList.toArray(new PVACEVViewBean[retList.size()]);
        } catch (ProjectException e) {
            e.printStackTrace();
        }
        return new PVACEVViewBean[0];
    }

    public void setFileName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return name;
    }

    public String getRedmineFileName() {
        return redmineFileName;
    }

    public void setRedmineFileName(String redmineFileName) {
        this.redmineFileName = redmineFileName;
    }

    public void setBasePrefix(String basePrefix) {
        this.basePrefix = basePrefix;
    }

    public void doDynamic(StaplerRequest req, StaplerResponse res)
            throws IOException, ServletException {

        String restOfPath = req.getRestOfPath();
        // System.out.println(restOfPath);

        String prefix = "/documents/";
        // documentsではじまるURLはビルドディレクトリのファイルを取りに行くってことにする。
        if (restOfPath.startsWith(prefix)) {
            String filePath = restOfPath.replace(prefix, "");
            openFile(req, res, filePath);
        } else {
            res.sendRedirect2(req.getContextPath() + req.getRestOfPath());
            // req.getRequestDispatcher(req.getContextPath()+restOfPath).forward(req,
            // res);
        }
    }

    private void openFile(StaplerRequest req, StaplerResponse res,
            String filePath) throws FileNotFoundException, IOException {

        System.out.println(filePath);
        System.out.println(req.getContextPath());

        System.out.println(owner.getRootDir());
        File file = new File(owner.getRootDir(), filePath);
        FileInputStream in = null;
        ServletOutputStream out = res.getOutputStream();
        try {
            in = new FileInputStream(file);
            int i;
            while ((i = in.read()) != -1) {
                out.write(i);
            }
        } finally {
            out.close();
            in.close();
        }
    }

    public File[] getSummaryFiles() {
        System.out.println(owner.getRootDir());
        return owner.getRootDir().listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                boolean flag1 = false;
                boolean flag2 = false;
                if (!StringUtils.isEmpty(name)) {
                    flag1 = !pathname.isDirectory()
                            && pathname.getName().startsWith(name);
                }
                if (!StringUtils.isEmpty(redmineFileName)) {
                    flag2 = !pathname.isDirectory()
                            && pathname.getName().startsWith(redmineFileName);
                }
                return flag1 || flag2;

            }
        });
    }

    // 以下は画面上でファイルがあるかをチェックするためのメソッド
    private static final String[] fileNames = { "_PVj.tsv", "_base_ACj.tsv",
            "_base_EVj.tsv" };

    private boolean exists(String name) {
        String[] list = owner.getRootDir().list();
        for (String string : list) {
            System.out.println(string);
        }
        return new File(owner.getRootDir(), name).exists();
    }

    public boolean getPvfileExists() {
        return exists(name + fileNames[0]);
    }

    public boolean getRedminePvfileExists() {
        return exists(redmineFileName + fileNames[0]);
    }

    public boolean getAcfileExists() {
        return exists(name + fileNames[1]);
    }

    public boolean getRedmineAcfileExists() {
        return exists(redmineFileName + fileNames[1]);
    }

    public boolean getEvfileExists() {
        return exists(name + fileNames[2]);
    }

    public boolean getRedmineEvfileExists() {
        return exists(redmineFileName + fileNames[2]);
    }

    public int getBuildNumber() {
        return owner.getNumber();
    }

}
