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
//çÏê¨ì˙: 2014/10/15

package nu.mine.kino.projects.utils;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import net.arnx.jsonic.JSON;
import nu.mine.kino.entity.PVBean;
import nu.mine.kino.entity.Project;
import nu.mine.kino.projects.DefaultProjectCreator;
import nu.mine.kino.projects.JSONProjectCreator;
import nu.mine.kino.projects.PVCreator;
import nu.mine.kino.projects.ProjectException;
import nu.mine.kino.projects.ProjectWriter;

import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class ProjectUtilsTest {

    @Test
    public void test() throws FileNotFoundException, ProjectException {
        File baseDir = new File("./");
        String fileName = "project_management_tools";
        String input = fileName + "." + "xls";

        java.io.InputStream in = null;
        File target = new File(baseDir, input);
        in = new java.io.FileInputStream(target);

        StopWatch watch = new StopWatch();
        watch.start();
        Project projectOrg = new DefaultProjectCreator().createProject(in);
        watch.stop();
        System.out.println(watch.getTime() + " ms.");
        watch.reset();
        // fail("Not yet implemented");
        File output = ProjectWriter
                .write(projectOrg, new File(input + ".json"));
        System.out.println(output.getAbsolutePath());

        watch.start();
        Project projectJ = new JSONProjectCreator()
                .createProject(new FileInputStream(output));
        watch.stop();
        System.out.println(watch.getTime() + " ms.");

        // projectJ.getProjectStartDate().setTime(new Date().getTime());

        String expected = JSON.encode(projectOrg);
        String actual = JSON.encode(projectJ);

        System.out.println("---------------");
        System.out.println(expected);
        System.out.println(actual);
        System.out.println("---------------");
        assertEquals(expected, actual);

    }

    @Test
    public void test2() throws FileNotFoundException, ProjectException {
        File baseDir = new File("./");
        String fileName = "project_management_tools";
        String input = fileName + "." + "xls";

        java.io.InputStream in = null;
        File target = new File(baseDir, input);
        in = new java.io.FileInputStream(target);

        StopWatch watch = new StopWatch();
        watch.start();
        Project projectOrg = new DefaultProjectCreator().createProject(in);
        watch.stop();
        System.out.println(watch.getTime() + " ms.");
        watch.reset();

        List<PVBean> list = PVCreator.createCurrentList(projectOrg);
        for (PVBean pvBean : list) {
            System.out.println(pvBean);
        }
    }
}
