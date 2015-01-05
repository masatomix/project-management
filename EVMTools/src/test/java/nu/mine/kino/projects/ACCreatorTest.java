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

package nu.mine.kino.projects;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import nu.mine.kino.entity.ACBean;
import nu.mine.kino.projects.utils.ProjectUtils;

import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class ACCreatorTest {

    @Test
    public void test() throws FileNotFoundException, ProjectException {

        File input = new File("project_management_tools.xls");
        File input_base = new File("base_project_management_tools.xls");
        System.out.println(input.getAbsolutePath());

        StopWatch watch = new StopWatch();
        watch.start();

        List<ACBean> createACList = ACCreator.createACList(input, input_base);
        watch.stop();
        for (ACBean acBean : createACList) {
            System.out.println(acBean);
        }
        System.out.println(watch.getTime() + " ms.");
        watch.reset();

    }

    @Test
    public void test2() throws FileNotFoundException, ProjectException {

        File input = new File("project_management_tools.xls");
        File input_base = new File("base_project_management_tools.xls");
        System.out.println(input.getAbsolutePath());

        StopWatch watch = new StopWatch();
        watch.start();

        List<ACBean> createACList = ACCreator.createACList(input, input_base);
        List<ACBean> filterAC = ProjectUtils.filterList(createACList);
        watch.stop();

        printList(createACList);
        printList(filterAC);
        System.out.println(watch.getTime() + " ms.");
        watch.reset();

    }

    @Test
    public void test3() throws FileNotFoundException, ProjectException {
        ProjectWriter.write(new File("project_management_tools.xls"));
        ProjectWriter.write(new File("base_project_management_tools.xls"));

        File input = new File("project_management_tools.xls.json");
        File input_base = new File("base_project_management_tools.xls.json");
        System.out.println(input.getAbsolutePath());

        StopWatch watch = new StopWatch();
        watch.start();

        List<ACBean> createACList = ACCreator.createACListFromJSON(input,
                input_base);
        List<ACBean> filterAC = ProjectUtils.filterList(createACList);
        watch.stop();

        printList(createACList);
        printList(filterAC);
        System.out.println(watch.getTime() + " ms.");
        watch.reset();

    }

    private void printList(List<ACBean> filterAC) {
        System.out.println("------");
        for (ACBean acBean : filterAC) {
            System.out.println(acBean);
        }
        System.out.println("------");
    }

}
