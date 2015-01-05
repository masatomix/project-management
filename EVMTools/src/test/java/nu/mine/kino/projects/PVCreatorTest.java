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
//�쐬��: 2014/10/15

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
public class PVCreatorTest {

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

}
