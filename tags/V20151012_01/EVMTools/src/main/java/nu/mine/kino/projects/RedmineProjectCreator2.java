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

import org.apache.commons.codec.binary.Base64;

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
import nu.mine.kino.projects.utils.URIUtils;

import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.bean.Issue;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class RedmineProjectCreator2 implements ProjectCreator {

    private final RedmineConfig config;

    public RedmineProjectCreator2(RedmineConfig config) {
        this.config = config;
    }

    public Project createProject(Object... conditions) throws ProjectException {
        String projectId = (String) conditions[0];
        Integer queryId = (Integer) conditions[1];

        String url = createURL(projectId, queryId);
        System.out.println(url);

        String webPage = null;
        if (config.isApiKeyFlag()) {
            webPage = HttpUtils.getWebPage(url);
        } else {
            webPage = HttpUtils.getWebPage(url, config.getUserId(),
                    config.getPassword());
        }
        System.out.println(webPage);
        Issue[] issues = JSON.decode(webPage, Issue2[].class);

        // Map<String, Map<String, Object>> decodeMaps = JSON.decode(webPage,
        // Map.class);
        // Collection<Map<String, Object>> values = decodeMaps.values();
        // for (Map<String, Object> map : values) {
        // BigDecimal id = (BigDecimal) map.get("id");
        // for (int i = 0; i < issues.length; i++) {
        // Issue ticket = issues[i];
        // if (ticket.getId().equals(id.intValue())) {
        // ticket.setSubject((String) map.get("subject"));
        // ticket.setDescription((String) map.get("description"));
        //
        // String start_date = (String) map.get("start_date");
        // String due_date = (String) map.get("due_date");
        //
        // try {
        // Date start_date_d = StringUtils.isEmpty(start_date) ? null
        // : DateUtils.parseDate(start_date,
        // new String[] { "yyyy/MM/dd" });
        // Date due_date_d = StringUtils.isEmpty(due_date) ? null
        // : DateUtils.parseDate(due_date,
        // new String[] { "yyyy/MM/dd" });
        // ticket.setStartDate(start_date_d);
        // ticket.setDueDate(due_date_d);
        // } catch (ParseException e) {
        // e.printStackTrace();
        // }
        //
        // BigDecimal done_ratio = (BigDecimal) map.get("done_ratio");
        // BigDecimal estimated_hours = (BigDecimal) map
        // .get("estimated_hours");
        //
        // if (done_ratio != null) {
        // ticket.setDoneRatio(done_ratio.intValue());
        // }
        // if (estimated_hours != null) {
        // ticket.setEstimatedHours(estimated_hours.floatValue());
        // }
        // }
        // }
        // }

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
    }

    private String createURL(String projectId, Integer queryId) {
        StringBuffer buf = new StringBuffer();
        buf.append("projects/");
        buf.append(projectId);
        buf.append("/");
        buf.append("issues.json");
        if (config.isApiKeyFlag()) {
            buf.append("?key=");
            buf.append(config.getApiAccessKey());
            if (queryId != null) {
                buf.append("&query_id=");
                buf.append(queryId);
            }
        } else {
            if (queryId != null) {
                buf.append("?query_id=");
                buf.append(queryId);
            }
        }
        String path = new String(buf);
        String uri = URIUtils.resolveURIStr(config.getRedmineHost(), path);
        return uri;
    }
}