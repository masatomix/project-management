/*******************************************************************************
 * Copyright (c) 2008 Masatomi KINO.
 * All rights reserved. 
 * $Id$
 *******************************************************************************/
//çÏê¨ì˙: 2009/06/25
package nu.mine.kino.plugin.jdt.utils;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.corext.util.Strings;

public class MyStrings {
    public static String getIndentString(IJavaProject project, String line) {
        String indentString = Strings.getIndentString(line, project);
        return indentString;
    }

    public static String changeIndent(String code, int codeIndentLevel,
            IJavaProject project, String newIndent, String lineDelim) {
        return Strings.changeIndent(code, codeIndentLevel, project, newIndent,
                lineDelim);
    }
}
