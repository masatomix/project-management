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

import java.util.List;

import nu.mine.kino.entity.Project;

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

            // Issueから、Taskへの変換をココで行う。
            for (Issue issue : issues) {
                System.out.println(issue.toString());
            }
        } catch (RedmineException e) {
            e.printStackTrace();
        }
        return null;
    }
}