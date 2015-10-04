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
//作成日: 2014/11/04

package nu.mine.kino.projects;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;
import nu.mine.kino.entity.Project;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class JSONProjectCreator extends InputStreamProjectCreator {

    public JSONProjectCreator(InputStream in) {
        super(in);
    }

    public JSONProjectCreator(File file) throws ProjectException {
        super(file);
    }

    /**
     * JSONフォーマットのFile(InputStream)からProjectを作成する
     * 
     * @see nu.mine.kino.projects.DefaultProjectCreator#createProject(java.io.InputStream)
     */
    @Override
    public Project createProjectFromStream() throws ProjectException {
        try {
            Project project = JSON.decode(this.getInputStream(), Project.class);
            return project;
        } catch (JSONException e) {
            throw new ProjectException(e);
        } catch (IOException e) {
            throw new ProjectException(e);
        }
    }
}
