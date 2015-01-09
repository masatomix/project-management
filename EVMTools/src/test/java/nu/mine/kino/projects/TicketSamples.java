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

import nu.mine.kino.entity.Project;

import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Issue;

public class TicketSamples {
    private static Integer queryId = null; // any

    public static void main(String[] args) {
        // RedmineManager mgr = RedmineManagerFactory.createWithApiKey(
        // redmineHost, apiAccessKey);
        RedmineManager mgr = RedmineManagerFactory.createWithUserAuth(
                "http://demo.redmine.org/", "masatomix", "hogehoge");
        try {
            tryGetIssues(mgr);
            tryGetIssues2(mgr);
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
        Project createProject = creator.createProject("kinosandboxproject",
                null);
        System.out.println(createProject);
        ProjectWriter.write(createProject, new File("redmineProject.json"));
        ProjectWriter.writeText(createProject, new File("redmineProject.tsv"));

    }
}