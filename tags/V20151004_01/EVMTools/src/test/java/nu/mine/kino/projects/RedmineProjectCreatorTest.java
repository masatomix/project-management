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

import org.junit.Test;

import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Issue;

public class RedmineProjectCreatorTest {

    private static final String HOST = "http://demo.redmine.org/";

    private static String projectKey = "kinosandboxproject";

    private static String password = "hogehoge";

    private static String apikey = "key";

    private static Integer queryId = null; // any

    private static String userId = "masatomix";

    // @Test
    public void test1() throws ProjectException {
        RedmineManager mgr = RedmineManagerFactory.createWithUserAuth(HOST,
                userId, password);
        RedmineProjectCreator creator = new RedmineProjectCreator(mgr);
        extracted(creator, "case01");
    }

    // @Test
    public void test2() throws ProjectException {
        RedmineManager mgr = RedmineManagerFactory.createWithApiKey(HOST,
                apikey);
        RedmineProjectCreator creator = new RedmineProjectCreator(mgr);
        extracted(creator, "case02");
    }

    // @Test
    public void test03() throws ProjectException {
        RedmineConfig config = new RedmineConfig(HOST, apikey);
        // RedmineConfig config = new RedmineConfig(HOST, userId, password);
        // RedmineManager mgr = RedmineManagerFactory.createWithUserAuth(HOST,
        // userId, password);
        ProjectCreator creator = new RedmineProjectCreator2(config);
        extracted(creator, "case03");
    }

    private void extracted(ProjectCreator creator, String case1)
            throws ProjectException {
        Project createProject = creator.createProject(projectKey, queryId);
        System.out.println(createProject);
        ProjectWriter.write(createProject, new File(case1 + ".json"));
        ProjectWriter.writeText(createProject, new File(case1 + ".tsv"));
    }
}