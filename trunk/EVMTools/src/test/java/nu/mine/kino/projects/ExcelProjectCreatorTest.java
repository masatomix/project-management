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
//çÏê¨ì˙: 2015/09/19

package nu.mine.kino.projects;

import java.io.File;

import junit.framework.Assert;

import nu.mine.kino.entity.Project;
import nu.mine.kino.entity.TaskInformation;

import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class ExcelProjectCreatorTest {

    @Test
    public void test() throws ProjectException {
        String fileName = "testdata";

        File baseDir = new File("./");
        String input = fileName + "." + "xls";
        File file = new File(baseDir, input);

        StopWatch watch = new StopWatch();
        watch.start();
        Project project = new ExcelProjectCreator(file).createProject();
        watch.stop();
        System.out.println(watch.getTime() + " ms.");

        TaskInformation[] infos = project.getTaskInformations();
        System.out.println(infos.length);
        checkPV(infos);

    }

    private void checkPV(TaskInformation[] infos) {

        for (TaskInformation info : infos) {
            System.out.printf("%s:%s\n", info.getTaskId(), info.getPV()
                    .getPlannedValue());
        }

        TaskInformation target = null;
        target = infos[0];
        Assert.assertEquals(Double.NaN, target.getPV().getPlannedValue());
        target = infos[6];
        Assert.assertEquals(0.5 / 9.0 * 1, target.getPV().getPlannedValue());// 0.05555555555555555
        target = infos[2];
        Assert.assertEquals(1.0 / 2.0 * 1, target.getPV().getPlannedValue());// 0.5
        target = infos[3];
        Assert.assertEquals(0.0, target.getPV().getPlannedValue());// 0.0
    }
}
