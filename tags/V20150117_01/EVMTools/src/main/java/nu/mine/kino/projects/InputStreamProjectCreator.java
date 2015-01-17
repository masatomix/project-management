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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import nu.mine.kino.entity.Project;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public abstract class InputStreamProjectCreator implements ProjectCreator {

    private InputStream in;

    public InputStreamProjectCreator(InputStream in) {
        this.in = in;
    }

    public InputStreamProjectCreator(File file) throws ProjectException {
        try {
            this.in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new ProjectException(e);
        }
    }

    public abstract Project createProjectFromStream() throws ProjectException;

    // /**
    // * Excel フォーマットのFile(InputStream)からProjectを作成する
    // *
    // * @see nu.mine.kino.projects.ProjectCreator#createProject(java.io.File)
    // */
    // @Override
    private Project createProject() throws ProjectException {
        // java.io.InputStream in = null;
        try {
            // in = new java.io.FileInputStream(input);
            Project project = this.createProjectFromStream();
            return project;
            // } catch (FileNotFoundException e) {
            // throw new ProjectException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new ProjectException(e);
                }
            }
        }
    }

    public InputStream getInputStream() {
        return in;
    }

    @Override
    public Project createProject(Object... conditions) throws ProjectException {
        return this.createProject();
    }
}
