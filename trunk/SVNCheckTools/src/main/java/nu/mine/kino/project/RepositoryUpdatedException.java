package nu.mine.kino.project;
import java.io.File;
import java.util.List;

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
//作成日: 2014/12/01

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class RepositoryUpdatedException extends Exception {

    private List<File> files;

    public void setFiles(List<File> files) {
        this.files = files;
        // TODO 自動生成されたメソッド・スタブ
        
    }

}
