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
//�쐬��: 2015/10/22

package nu.mine.kino.jenkins.plugins.projectmanagement.tokenmacros;

import hudson.Extension;
import hudson.model.TaskListener;
import hudson.model.AbstractBuild;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.jenkinsci.plugins.tokenmacro.DataBoundTokenMacro;
import org.jenkinsci.plugins.tokenmacro.MacroEvaluationException;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
@Extension
public class BuildDateTokenMacro extends DataBoundTokenMacro {
    @Parameter
    public String format = "yyyy/MM/dd hh:mm";

    @Override
    public boolean acceptsMacroName(String macroName) {
        return macroName.equals("BUILD_DATE");
    }

    @Override
    public String evaluate(AbstractBuild<?, ?> build, TaskListener arg1,
            String arg2) throws MacroEvaluationException, IOException,
            InterruptedException {
        Date time = build.getTime();
        return DateFormatUtils.format(time, format);
    }

}
