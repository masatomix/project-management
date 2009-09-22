/******************************************************************************
 * Copyright (c) 2009 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 *****************************************************************************/

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.tools.ant.BuildLogger;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

//作成日: 2009/05/06

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class Main {
    public static void main(String[] args) {
        try {
            Project project = new Project();
            project.init();

            // ビルドファイルを指定
            File buildFile = getBuildFile();
            ProjectHelper.getProjectHelper().parse(project, buildFile);

            // loggerの設定。
            BuildLogger buildLogger = getBuildLogger();
            project.addBuildListener(buildLogger);

            // Vector list = new Vector();
            // list.add(project.getDefaultTarget());
            // project.executeTargets(new Vector(list));
            project.executeTarget(project.getDefaultTarget());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BuildLogger getBuildLogger() {
        BuildLogger buildLogger = new DefaultLogger();
        buildLogger.setMessageOutputLevel(Project.MSG_INFO);
        buildLogger.setOutputPrintStream(new PrintStream(System.out));
        buildLogger.setErrorPrintStream(new PrintStream(System.err));
        buildLogger.setEmacsMode(false);
        return buildLogger;
    }

    private static File getBuildFile() throws IOException {
        return new File("build.xml");
    }

}