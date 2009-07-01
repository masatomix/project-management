/******************************************************************************
 * Copyright (c) 2009 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//作成日: 2009/06/27
package nu.mine.kino.plugin.jdt.utils;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.TypeNameRequestor;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class JDTUtils {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(JDTUtils.class);

    public static boolean canUseThisClass(IJavaProject javaProject,
            String qualifiedClassName, IProgressMonitor monitor)
            throws JavaModelException {
        final boolean[] flag = new boolean[1];
        flag[0] = false;
        // 検索対象は、IJavaProjectのルートにあるモノ全部。(jarとかsrcディレクトリとか、外部のパスが通ってるjarとか)
        IJavaSearchScope scope = SearchEngine
                .createJavaSearchScope(new IJavaElement[] { javaProject }); // プロジェクト内のソースや、jar(外部のも)が検索対象。
        // SearchEngine().searchAllTypeNamesを使う場合はフックするクラスはTypeNameRequestor。
        TypeNameRequestor nameRequestor = new TypeNameRequestor() {
            public void acceptType(int modifiers, char[] packageName,
                    char[] simpleTypeName, char[][] enclosingTypeNames,
                    String path) {
                flag[0] = true;
            }
        };

        String pkg = "";
        String clazzName = "";
        if (qualifiedClassName != null && qualifiedClassName.indexOf('.') > -1) {
            int lastIndex = qualifiedClassName.lastIndexOf('.');
            pkg = qualifiedClassName.substring(0, lastIndex);
            clazzName = qualifiedClassName.substring(lastIndex + 1);
        } else {
            pkg = "";
            clazzName = qualifiedClassName;
        }
        logger.debug("canUseThisClass() - pkg: " + pkg);
        logger.debug("canUseThisClass() - class: " + clazzName);
        new SearchEngine().searchAllTypeNames(pkg.toCharArray(),
                SearchPattern.R_EXACT_MATCH, clazzName.toCharArray(),
                SearchPattern.R_EXACT_MATCH, IJavaSearchConstants.CLASS, scope,
                nameRequestor, IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH,
                monitor);
        return flag[0];
    }

    /**
     * 型情報から、toStringを作成する
     * 
     * @param type
     * @param lineDelim
     * @param project
     * @param canUseToStringBuilder
     * @return
     * @throws JavaModelException
     * @throws BadLocationException
     */
    public static String createToString(IType type, String lineDelim,
            IJavaProject project, boolean canUseToStringBuilder)
            throws JavaModelException {
        StringBuffer buf = new StringBuffer();
        IField[] fields = type.getFields();
        buf.append(lineDelim);
        buf.append("@Override");
        buf.append(lineDelim);
        buf.append("public String toString(){");
        buf.append(lineDelim);
        // フィールドがない場合super.toStringにしちゃう。
        if (fields == null || fields.length == 0) {
            buf.append("return super.toString()");
        }
        // ToStringBuilderが使える場合は、フィールドを並べる。
        else if (canUseToStringBuilder) {
            buf.append("return new ToStringBuilder(this)");
            for (IField method : fields) {
                String elementName = method.getElementName();
                buf.append(".append(\"");
                buf.append(elementName);
                buf.append("\",");
                buf.append(elementName);
                buf.append(")");
            }
            buf.append(".toString()");
        }
        // ToStringBuilderは使えないけど、フィールドがある場合は、フィールドを並べる。
        else {
            buf.append("StringBuffer buf = new StringBuffer();");
            buf.append(lineDelim);
            // buf.append(indentString);
            buf.append("buf");
            for (IField method : fields) {
                String elementName = method.getElementName();
                buf.append(".append(\" ");
                buf.append(elementName);
                buf.append(": \"+");
                buf.append(elementName);
                buf.append(")");
            }
            buf.append(";");
            buf.append(lineDelim);
            buf.append("return new String(buf)");
        }
        buf.append(";");
        buf.append(lineDelim);
        buf.append("}");
        return new String(buf);
    }

    // public static String createIndentedCode(String code, int
    // memberStartOffset,
    // IDocument document, IJavaProject project)
    // throws JavaModelException, BadLocationException {
    // String lineDelim = TextUtilities.getDefaultLineDelimiter(document); //
    // デリミタを取得。
    // String tmp = addLineDelim(code, lineDelim);
    //
    // // int memberStartOffset = getMemberStartOffset(member, document);
    // // そのオフセットの位置のインデント文字列を検索している
    // String indentString = getIndentString(project, document,
    // memberStartOffset);
    // // codeに改行コードつけて、さらに先のインデントをつける
    // String indentedCode = MyStrings.changeIndent(tmp, 0, project,
    // indentString, lineDelim);
    // return indentedCode;
    // }

    // public static String addLineDelim(String code, String lineDelim) {
    // String temp = code;
    // if (temp != null) {
    // temp = lineDelim + temp;
    // }
    // return temp;
    // }

    // public static String getIndentString(IJavaProject project,
    // IDocument document, int memberStartOffset)
    // throws BadLocationException {
    // // オフセット位置の行情報。
    // IRegion region = document.getLineInformationOfOffset(memberStartOffset);
    // // オフセットとRegionをわたして、その行を取得してる?
    // // 該当する行。}まででなくて、一行。
    // String line = document.get(region.getOffset(), region.getLength());
    // String indentString = MyStrings.getIndentString(project, line);
    // return indentString;
    // }

    // /**
    // * 引数のメソッド情報のオフセット位置(先頭)を返すメソッド。
    // * コメント文とかがある場合は、コメント文よりは後ろの位置(ホントのメソッドの先頭位置)を返す。
    // *
    // * @param method
    // * @param document
    // * @return
    // * @throws JavaModelException
    // */
    // public static int getMemberStartOffset(IMethod method, IDocument
    // document)
    // throws JavaModelException {
    // ISourceRange sourceRange = method.getSourceRange();
    // int memberStartOffset = sourceRange.getOffset();
    //
    // // 通常は上のオフセットを返すので問題ないけど、コメントとかがあると、コメントよりうえにいっちゃう。
    // // ホントにメソッドの直上にする場合は、このようにする必要があるみたいだ。
    // TokenScanner scanner = new TokenScanner(document, method
    // .getJavaProject());
    //
    // try {
    // int nextStartOffset = scanner.getNextStartOffset(memberStartOffset,
    // true);
    // return nextStartOffset;
    // // return scanner.getNextStartOffset(memberStartOffset, true);
    // // read
    // // to
    // // the
    // // first real non
    // // comment token
    // } catch (CoreException e) {
    // // ignore
    // }
    // return memberStartOffset;
    // }
    //
    // /**
    // * 引数のメソッド情報のオフセット位置(終わり)を返すメソッド。
    // *
    // * @param method
    // * @param document
    // * @return
    // * @throws JavaModelException
    // */
    // public static int getMemberEndOffset(IMethod method, IDocument document)
    // throws JavaModelException {
    // ISourceRange sourceRange = method.getSourceRange();
    // int memberEndOffset = sourceRange.getOffset() + sourceRange.getLength();
    // return memberEndOffset;
    // }
    //
    // /**
    // * ITypeから、最後のメソッドを取得するメソッド。ない場合はNULLが返るが、その場合の処理が課題。
    // *
    // * @param unit
    // * @return 最後のメソッド。
    // */
    // public static IMethod getLastMethodFromType(IType type) {
    // logger.debug("getLastMethodFromType(IType) - start");
    // try {
    // // メソッド一覧を取得。
    // IMethod[] methods = type.getMethods();
    // if (methods != null && methods.length > 0) {
    // IMethod lastMethod = methods[methods.length - 1];
    // return lastMethod;
    // }
    // } catch (JavaModelException e) {
    // logger.error("getLastMethodFromType(IType)", e);
    // JDTUtilsPlugin.logException(e);
    // }
    // logger.debug("getLastMethodFromType(IType) - end");
    // logger.warn("getLastMethodFromType(IType) - メソッドがなかった(´д`;)");
    // return null;
    // }

    /**
     * {@link ICompilationUnit}を引数にして、子要素を返すメソッド。 子要素は、パッケージや、クラスなどがある。具体的には
     * {@link IPackageDeclaration}とか、{@link IType}など。
     * 
     * @param unit
     * @return
     */
    public static IJavaElement[] getChildren(ICompilationUnit unit) {
        try {
            if (!unit.isStructureKnown()) {
                return null;
            }
            // まずは子要素を取得。取得されるのは、パッケージ宣言とか、クラス型とか
            // クラスが複数定義されている場合もあるし。
            IJavaElement[] elements = unit.getChildren();
            return elements;
        } catch (JavaModelException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static IJavaElement getJavaElement(ExecutionEvent event)
            throws ExecutionException {
        ISelection selection = HandlerUtil.getActiveMenuSelectionChecked(event);
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection sselection = (IStructuredSelection) selection;
            Object firstElement = sselection.getFirstElement();
            if (firstElement instanceof IJavaElement) {
                IJavaElement element = (IJavaElement) firstElement;
                return element;
            } // ココは、ソースコードの要素を選択して遷移したら、Unit じゃなくてIJavaElementがわたってくるゾ。
            else {
                logger.debug("getJavaElement(ExecutionEvent)"
                        + firstElement.getClass().getName());
            }
        }
        return null;
    }

    public static void openEditor(ICompilationUnit unit) {
        try {
            // open the editor, forces the creation of a working copy
            IEditorPart editor = JavaUI.openInEditor(unit);
        } catch (PartInitException e) {
            JDTUtilsPlugin.logException(e);
        } catch (JavaModelException e) {
            JDTUtilsPlugin.logException(e);
        }
    }

}
