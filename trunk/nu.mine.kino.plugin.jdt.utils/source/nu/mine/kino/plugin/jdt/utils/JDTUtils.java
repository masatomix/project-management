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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.dom.TokenScanner;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
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

    /**
     * 型情報から、toStringを作成する
     * 
     * @param type
     * @param
     * @param lastMethod
     * @return
     * @throws JavaModelException
     * @throws BadLocationException
     */
    public static String createToString(IType type, IMethod lastMethod,
            IDocument document, IJavaProject project)
            throws JavaModelException, BadLocationException {
        String lineDelim = TextUtilities.getDefaultLineDelimiter(document);
        StringBuffer buf = new StringBuffer();
        int memberStartOffset = getMemberStartOffset(lastMethod, document);
        // そのオフセットの位置のインデント文字列を検索している
        String indentString = getIndentString(project, document,
                memberStartOffset);
        IField[] fields = type.getFields();
        buf.append(lineDelim);
        buf.append("@Override");
        buf.append(lineDelim);
        buf.append("public String toString(){");
        buf.append(lineDelim);
        buf.append(indentString);
        // フィールドがない場合super.toStringにしちゃう。
        if (fields == null || fields.length == 0) {
            buf.append("return super.toString()");
        }
        // ある場合は、フィールドを並べる。
        else {
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
        // ある場合は、フィールドを並べる。
        // else {
        // buf.append("StringBuffer buf = new StringBuffer();");
        // buf.append(lineDelim);
        // buf.append(indentString);
        // buf.append("buf");
        // for (IField method : fields) {
        // String elementName = method.getElementName();
        // buf.append(".append(\" ");
        // buf.append(elementName);
        // buf.append(": \"+");
        // buf.append(elementName);
        // buf.append(")");
        // // System.out.println(method.getElementName());
        // }
        // buf.append(";");
        // buf.append(lineDelim);
        // buf.append(indentString);
        // buf.append("return buf");
        // buf.append(".toString()");
        // }
        buf.append(";");
        buf.append(lineDelim);
        buf.append("}");
        return new String(buf);

        // @Override
        // public String toString() {
        // return new ToStringBuilder(this).append("議事録id", minutes_id).append(
        // "プロジェクトid", project_id).append("会議名", meeting_name).append(
        // "会議目的", purpose).append("会議実施日", meeting_date).append("開始時刻",
        // start_time).append("終了時刻", end_time).append("議事録ステータス",
        // minutes_status).append("備考", minutes_comment).append("会議場所",
        // meeting_place).append("削除区分", delete_div).append("version",
        // version).append("作成日時", create_date).append("作成ユーザID",
        // create_user_id).append("更新日時", update_date).append("更新ユーザID",
        // update_user_id).append("議事録明細", minutes_detail).toString();
        // }
    }

    public static String createIndentedCode(String code, IMethod member,
            IDocument document, IJavaProject project)
            throws JavaModelException, BadLocationException {
        String lineDelim = TextUtilities.getDefaultLineDelimiter(document); // デリミタを取得。
        String tmp = addLineDelim(code, lineDelim);

        int memberStartOffset = getMemberStartOffset(member, document);
        // そのオフセットの位置のインデント文字列を検索している
        String indentString = getIndentString(project, document,
                memberStartOffset);
        // codeに改行コードつけて、さらに先のインデントをつける
        String indentedCode = MyStrings.changeIndent(tmp, 0, project,
                indentString, lineDelim);
        return indentedCode;
    }

    public static String addLineDelim(String code, String lineDelim) {
        String temp = code;
        if (temp != null) {
            temp = lineDelim + temp;
        }
        return temp;
    }

    public static String getIndentString(IJavaProject project,
            IDocument document, int memberStartOffset)
            throws BadLocationException {
        // オフセット位置の行情報。
        IRegion region = document.getLineInformationOfOffset(memberStartOffset);
        // オフセットとRegionをわたして、その行を取得してる?
        // 該当する行。}まででなくて、一行。
        String line = document.get(region.getOffset(), region.getLength());
        String indentString = MyStrings.getIndentString(project, line);
        return indentString;
    }

    /**
     * 引数のメソッド情報のオフセット位置(先頭)を返すメソッド。
     * コメント文とかがある場合は、コメント文よりは後ろの位置(ホントのメソッドの先頭位置)を返す。
     * 
     * @param method
     * @param document
     * @return
     * @throws JavaModelException
     */
    public static int getMemberStartOffset(IMethod method, IDocument document)
            throws JavaModelException {
        ISourceRange sourceRange = method.getSourceRange();
        int memberStartOffset = sourceRange.getOffset();

        // 通常は上のオフセットを返すので問題ないけど、コメントとかがあると、コメントよりうえにいっちゃう。
        // ホントにメソッドの直上にする場合は、このようにする必要があるみたいだ。
        TokenScanner scanner = new TokenScanner(document, method
                .getJavaProject());

        try {
            int nextStartOffset = scanner.getNextStartOffset(memberStartOffset,
                    true);
            return nextStartOffset;
            // return scanner.getNextStartOffset(memberStartOffset, true);
            // read
            // to
            // the
            // first real non
            // comment token
        } catch (CoreException e) {
            // ignore
        }
        return memberStartOffset;
    }

    /**
     * 引数のメソッド情報のオフセット位置(終わり)を返すメソッド。
     * 
     * @param method
     * @param document
     * @return
     * @throws JavaModelException
     */
    public static int getMemberEndOffset(IMethod method, IDocument document)
            throws JavaModelException {
        ISourceRange sourceRange = method.getSourceRange();
        int memberEndOffset = sourceRange.getOffset() + sourceRange.getLength();
        return memberEndOffset;
    }

    /**
     * ITypeから、最後のメソッドを取得するメソッド。
     * 
     * @param unit
     * @return
     */
    public static IMethod getLastMethodFromType(IType type) {
        try {
            // メソッド一覧を取得。
            IMethod[] methods = type.getMethods();
            IMethod lastMethod = methods[methods.length - 1];
            return lastMethod;
        } catch (JavaModelException e) {
            JDTUtilsPlugin.logException(e);
        }
        return null;
    }

    public static IJavaElement[] unit2IJavaElements(ICompilationUnit unit) {
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

    // public static ICompilationUnit getCompilationUnit(ExecutionEvent event)
    // throws ExecutionException {
    // ISelection selection = HandlerUtil.getActiveMenuSelectionChecked(event);
    // if (selection instanceof IStructuredSelection) {
    // IStructuredSelection sselection = (IStructuredSelection) selection;
    // Object firstElement = sselection.getFirstElement();
    // if (firstElement instanceof ICompilationUnit) {
    // ICompilationUnit unit = (ICompilationUnit) firstElement;
    // return unit;
    // } // ココは、ソースコードの要素を選択して遷移したら、Unit じゃなくてIJavaElementがわたってくるゾ。
    // else {
    // System.out.println(firstElement.getClass().getName());
    // }
    // }
    // return null;
    //
    // }

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

}
