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
//çÏê¨ì˙: 2016/02/04

package nu.mine.kino.jenkins.plugins.projectmanagement.columns;

import hudson.Extension;
import hudson.model.Job;
import hudson.views.ListViewColumnDescriptor;
import hudson.views.ListViewColumn;
import nu.mine.kino.entity.EVMViewBean;
import nu.mine.kino.jenkins.plugins.projectmanagement.utils.PMUtils;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class EVMColumn extends ListViewColumn {

    private int columnWidth;

    private boolean forceWidth;

    @DataBoundConstructor
    public EVMColumn(int columnWidth, boolean forceWidth) {
        super();
        this.columnWidth = columnWidth;
        this.forceWidth = forceWidth;
    }

    public EVMColumn() {
        this(80, false);
    }

    public int getColumnWidth() {
        return columnWidth;
    }

    public boolean isForceWidth() {
        return forceWidth;
    }

    public EVMViewBean getCurrentPVACEV(Job<?, ?> project) {
        EVMViewBean currentPVACEV = PMUtils.getCurrentPVACEV(project);
        return currentPVACEV;
    }

    @Extension
    public static class DescriptorImpl extends ListViewColumnDescriptor {

        @Override
        public boolean shownByDefault() {
            return false;
        }

        @Override
        public String getDisplayName() {
            return "EVMèÓïÒ";
        }

        // @Override
        // public String getHelpFile() {
        // return "/plugin/extra-columns/help-buildDescription-column.html";
        // }

    }
}
