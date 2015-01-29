/******************************************************************************
 * Copyright (c) 2013 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//çÏê¨ì˙: 2013/06/07

package nu.mine.kino.projects;

import java.io.File;
import java.util.List;

import net.arnx.jsonic.JSON;
import nu.mine.kino.entity.Project;
import nu.mine.kino.projects.utils.HttpUtils;
import nu.mine.kino.projects.utils.URIUtils;

import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Issue;

public class TicketSamples {
    private static final String HOST = "http://demo.redmine.org/";

    private static Integer queryId = null; // any

    private static String projectKey = "kinosandboxproject";

    public static void main(String[] args) {
        // RedmineManager mgr = RedmineManagerFactory.createWithApiKey(
        // redmineHost, apiAccessKey);
        RedmineManager mgr = RedmineManagerFactory.createWithUserAuth(HOST,
                "masatomix", "hogehoge");
        try {
            // tryGetIssues(mgr);
            tryGetIssues3(mgr, HOST, projectKey, "apiAccessKey");
            // tryGetIssues4(mgr, HOST, projectKey, "apiAccessKey");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void tryGetIssues(RedmineManager mgr) throws Exception {
        List<Issue> issues = mgr.getIssueManager().getIssues(
                "kinosandboxproject", queryId);
        for (Issue issue : issues) {
            System.out.println(issue.toString());
        }
    }

    private static void tryGetIssues2(RedmineManager mgr) throws Exception {
        RedmineProjectCreator creator = new RedmineProjectCreator(mgr);
        Project createProject = creator.createProject(projectKey, null);
        System.out.println(createProject);
        ProjectWriter.write(createProject, new File("redmineProject.json"));
        ProjectWriter.writeText(createProject, new File("redmineProject.tsv"));

    }

    private static void tryGetIssues3(RedmineManager mgr, String redmineHost,
            String projectKey, String apiAccessKey) throws Exception {

        String url = URIUtils.resolveURIStr(redmineHost, "projects/"
                + projectKey + "/" + "issues.json?key=" + apiAccessKey);
        // String url = redmineHost + "/projects/" + projectKey + "/"
        // + "issues.json?key=" + apiAccessKey;
        System.out.println(url);

        String webPage = HttpUtils.getWebPage(url);
        System.out.println(webPage);
        Issue[] issues = JSON.decode(webPage, Issue2[].class);
        for (Issue issue : issues) {
            System.out.println(issue);
        }

        // Map<String, List<LinkedHashMap>> map = JSON.decode(webPage,
        // Map.class);
        // List<LinkedHashMap> issues = map.get("issues");
        // System.out.println(issues);
        // for (LinkedHashMap linkedHashMap : issues) {
        // System.out.println(linkedHashMap);
        // Issue issue = JSON.decode(JSON.encode(linkedHashMap), Issue.class);
        // }
    }

    private static void tryGetIssues4(RedmineManager mgr, String redmineHost,
            String projectKey, String apiAccessKey) throws Exception {

        ProjectCreator creator = new RedmineProjectCreator2(new RedmineConfig(
                redmineHost, apiAccessKey));
        Project createProject = creator.createProject(projectKey, null);

    }
}