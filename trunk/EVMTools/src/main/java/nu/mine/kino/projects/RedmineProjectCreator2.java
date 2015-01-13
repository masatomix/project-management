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
import java.util.List;

import net.arnx.jsonic.JSON;
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
import nu.mine.kino.projects.utils.HttpUtils;

import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.Issue;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class RedmineProjectCreator2 extends RedmineProjectCreator {

    private final String redmineHost;

    private final String apiKey;

    public RedmineProjectCreator2(RedmineManager redmineManager,
            String redmineHost, String apiKey) {
        super(redmineManager);
        this.redmineHost = redmineHost;
        this.apiKey = apiKey;
    }

    public Project createProject(Object... conditions) throws ProjectException {
        String projectId = (String) conditions[0];
        Integer queryId = (Integer) conditions[1];

        String url = createURL(projectId, queryId);
        System.out.println(url);
        String webPage = HttpUtils.getWebPage(url);
        System.out.println(webPage);
        Issue[] issues = JSON.decode(webPage, Issue2[].class);
        for (Issue issue : issues) {
            System.out.println(issue);
        }
        Project project = new Project();
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
    }

    private String createURL(String projectId, Integer queryId) {
        StringBuffer buf = new StringBuffer();
        buf.append(redmineHost);
        buf.append("/projects/");
        buf.append(projectId);
        buf.append("/");
        buf.append("issues.json?key=");
        buf.append(apiKey);
        if (queryId != null) {
            buf.append("&query_id=");
            buf.append(queryId);
        }
        String url = new String(buf);
        return url;
    }
}