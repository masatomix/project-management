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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import nu.mine.kino.entity.Holiday;
import nu.mine.kino.entity.Project;
import nu.mine.kino.projects.JSONProjectCreator;
import nu.mine.kino.projects.ProjectException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class HolidayCalendarAction implements Action {

    private static final long serialVersionUID = 1L;

    private final AbstractBuild<?, ?> owner;

    private String name;

    public HolidayCalendarAction(AbstractBuild<?, ?> owner) {
        this.owner = owner;
    }

    @Override
    public String getIconFileName() {
        // return "user.png";
        return "/plugin/project-management/images/24x24/user_suit.png";
    }

    @Override
    public String getDisplayName() {
        return String.format("休日カレンダー");
    }

    @Override
    public String getUrlName() {
        return "holiday-calendar";
    }

    public AbstractBuild<?, ?> getOwner() {
        return owner;
    }

    public void setFileName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return name;
    }

    private Map<String, Project> map = new HashMap<String, Project>();

    public synchronized Project getProject(String name) throws ProjectException {
        StopWatch watch = new StopWatch();
        watch.start();
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        if (map.containsKey(name)) {
            Project project = map.get(name);
            return project;
        }
        File target = new File(owner.getRootDir(), name);
        if (!target.exists()) {
            return null;
        }
        Project targetProject = new JSONProjectCreator(target).createProject();
        watch.stop();
        System.out.printf("%s -> Project 時間: [%d] ms\n", name, watch.getTime());
        watch = null;
        map.put(name, targetProject);
        return targetProject;
    }

    public Holiday[] getHolidays() {

        StopWatch watch = new StopWatch();
        watch.start();
        if (StringUtils.isEmpty(name)) {
            return new Holiday[0];
        }
        try {
            Project targetProject = getProject(name);
            return targetProject.getHolidays();

        } catch (ProjectException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } finally {
            watch.stop();
            System.out.printf("getHolidays 時間: [%d] ms\n", watch.getTime());
            watch = null;
        }
        return null;
    }

}
