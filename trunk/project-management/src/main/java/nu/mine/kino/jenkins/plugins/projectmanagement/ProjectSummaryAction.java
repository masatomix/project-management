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

    public ProjectUser[] getUsers() {
        PVACEVViewBean[] pvacevViews = getPVACEVViews();

        // System.out.println(User.get("", false, Collections.emptyMap())
        // .getId());
        // return all.toArray(new User[all.size()]);
        Map<String, ProjectUser> userMap = new HashMap<String, ProjectUser>();
        FileInputStream in = null;
        try {
            in = new FileInputStream(new File(owner.getRootDir(), name));
            Project project = new JSONProjectCreator(in).createProject();
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
        } catch (FileNotFoundException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (ProjectException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
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

    public ACViewBean[] getPreviousACViews() {
        try {
            List<ACViewBean> retList = new ArrayList<ACViewBean>();
            File target = new File(owner.getRootDir(), name);
            File base = new File(owner.getRootDir(), "base_" + name);
            List<ACBean> filterList = ACCreator.createACListFromJSON(target,
                    base);
            // List<ACBean> filterList = ProjectUtils.filterAC(list);

            Project targetProject = new JSONProjectCreator(target)
                    .createProject();

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
        } finally {
        }
        return null;
    }

    public EVViewBean[] getPreviousEVViews() {
        try {
            List<EVViewBean> retList = new ArrayList<EVViewBean>();
            File target = new File(owner.getRootDir(), name);
            File base = new File(owner.getRootDir(), "base_" + name);
            List<EVBean> filterList = EVCreator.createEVListFromJSON(target,
                    base);
            // List<EVBean> filterList = ProjectUtils.filterEV(list);

            Project targetProject = new JSONProjectCreator(target)
                    .createProject();

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

        FileInputStream in = null;
        try {
            in = new FileInputStream(new File(owner.getRootDir(), name));
            Project project = new JSONProjectCreator(in).createProject();
            return project.getBaseDate();
        } catch (FileNotFoundException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (ProjectException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public PVViewBean[] getPVViews() {

        FileInputStream in = null;
        try {
            in = new FileInputStream(new File(owner.getRootDir(), name));
            Project project = new JSONProjectCreator(in).createProject();

            DescriptorImpl descriptor = (DescriptorImpl) Jenkins.getInstance()
                    .getDescriptor(EVMToolsBuilder.class);
            String[] prefixArray = Utils.parseCommna(descriptor.getPrefixs());
            List<PVViewBean> retList = ProjectUtils.filterList(
                    ViewUtils.getPVViewBeanList(project), prefixArray);

            File file = new File(owner.getRootDir(), "redmineProject.json");
            if (file.exists()) {
                Project redmineProject = new JSONProjectCreator(file)
                        .createProject();
                List<PVViewBean> pvViewBeanList = ViewUtils
                        .getPVViewBeanList(redmineProject);
                if (!pvViewBeanList.isEmpty()) {
                    retList.addAll(pvViewBeanList);
                }
            }
            return retList.toArray(new PVViewBean[retList.size()]);
        } catch (FileNotFoundException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (ProjectException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public PVACEVViewBean[] getPVACEVViews() {
        File target = new File(owner.getRootDir(), name);
        File base = new File(owner.getRootDir(), "base_" + name);
        try {
            Project targetProject = new JSONProjectCreator(target)
                    .createProject();
            Project baseProject = new JSONProjectCreator(base).createProject();

            DescriptorImpl descriptor = (DescriptorImpl) Jenkins.getInstance()
                    .getDescriptor(EVMToolsBuilder.class);
            String[] prefixArray = Utils.parseCommna(descriptor.getPrefixs());

            List<PVACEVViewBean> list = ProjectUtils
                    .filterList(ViewUtils.getPVACEVViewBeanList(targetProject,
                            baseProject), prefixArray);
            return list.toArray(new PVACEVViewBean[list.size()]);
        } catch (ProjectException e) {
            e.printStackTrace();
        }
        return new PVACEVViewBean[0];
    }

    // public PVViewBean[] getTodayTasks() {
    // FileInputStream in = null;
    // try {
    // List<PVViewBean> retList = new ArrayList<PVViewBean>();
    // in = new FileInputStream(new File(owner.getRootDir(), name));
    // Project project = ProjectUtils.createProjectFromJSON(in);
    // List<PVBean> list = PVCreator.createCurrentList(project);
    // List<PVBean> filterList = ProjectUtils.filter(list);
    //
    // for (PVBean pvBean : filterList) {
    // PVViewBean bean = new PVViewBean();
    // PVBean2PVViewBean.convert(pvBean, bean);
    // TaskInformation taskInformation = ProjectUtils
    // .getTaskInformation(project, pvBean.getTaskId());
    // // if (taskInformation != null) {
    // Task2PVViewBean.convert(taskInformation.getTask(), bean);
    // // }
    // retList.add(bean);
    // }
    //
    // return retList.toArray(new PVViewBean[retList.size()]);
    //
    // } catch (FileNotFoundException e) {
    // // TODO 自動生成された catch ブロック
    // e.printStackTrace();
    // } catch (ProjectException e) {
    // // TODO 自動生成された catch ブロック
    // e.printStackTrace();
    // } catch (IllegalAccessException e) {
    // // TODO 自動生成された catch ブロック
    // e.printStackTrace();
    // } catch (InvocationTargetException e) {
    // // TODO 自動生成された catch ブロック
    // e.printStackTrace();
    // } finally {
    // if (in != null) {
    // try {
    // in.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    // return null;
    // }

    public void setFileName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return name;
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
                return !pathname.isDirectory()
                        && pathname.getName().startsWith(name);
            }
        });
    }
}
