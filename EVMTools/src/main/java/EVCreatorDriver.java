/******************************************************************************
 * Copyright (c) 2008-2009 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/

import java.io.File;

import nu.mine.kino.projects.EVCreator;
import nu.mine.kino.projects.ProjectException;

/**
 * 
 * @author Masatomi KINO and JavaBeans Creator Plug-in
 * @version $Revision$
 */
public class EVCreatorDriver {

    public static void main(String[] args) throws ProjectException {
        File input = new File("project_management_tools.xls");
        File input_base = new File("base_project_management_tools.xls");
        System.out.println(input.getAbsolutePath());
        EVCreator.create(input, input_base);
    }
}
