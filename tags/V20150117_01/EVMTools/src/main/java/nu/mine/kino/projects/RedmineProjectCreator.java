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
//作成日: 2014/12/26

package nu.mine.kino.projects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nu.mine.kino.entity.ACTotalBean;
import nu.mine.kino.entity.EVTotalBean;
import nu.mine.kino.entity.Issue2ACTotalBean;
import nu.mine.kino.entity.Issue2EVTotalBean;
import nu.mine.kino.entity.Issue2PVTotalBean;
import nu.mine.kino.entity.Issue2Task;
import nu.mine.kino.entity.PVTotalBean;
import nu.mine.kino.entity.Project;
import nu.mine.kino.entity.Task;
import nu.mine.kino.entity.TaskInformation;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.Issue;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class RedmineProjectCreator implements ProjectCreator {
    private RedmineManager redmineManager;

    public RedmineProjectCreator(RedmineManager redmineManager) {
        this.redmineManager = redmineManager;
    }

    public Project createProject(Object... conditions) throws ProjectException {
        String projectId = (String) conditions[0];
        Integer queryId = (Integer) conditions[1];
        try {
            List<Issue> issues;
            issues = redmineManager.getIssueManager().getIssues(projectId,
                    queryId);

            Project project = new Project();
            project.setBaseDate(new Date());
            // Issueから、Taskへの変換をココで行う。

            List<TaskInformation> taskinfos = new ArrayList<TaskInformation>();
            for (Issue issue : issues) {
                System.out.println("creator: " + issue.toString());
                Task task = Issue2Task.convert(issue);
                String taskId = task.getTaskId();
                TaskInformation info = new TaskInformation();
                info.setTaskId(taskId);

                PVTotalBean pv = Issue2PVTotalBean.convert(issue);
                ACTotalBean ac = Issue2ACTotalBean.convert(issue);
                EVTotalBean ev = Issue2EVTotalBean.convert(issue);
                info.setTask(task);
                info.setPV(pv);
                info.setAC(ac);
                info.setEV(ev);

                taskinfos.add(info);
            }
            project.setTaskInformations(taskinfos
                    .toArray(new TaskInformation[taskinfos.size()]));

            return project;
        } catch (RedmineException e) {
            throw new ProjectException(e);
        }
    }
}