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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
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
     * 型情報から、toStringを作成する
     * 
     * @param type
     * @return
     */
    public static String createToString(IType type) {
        return "hoge";
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
    public static IMethod getLastMethodFromUnit(IType type) {
        try {
            // メソッド一覧を取得。
            IMethod[] methods = type.getMethods();
            IMethod lastMethod = methods[methods.length - 1];
            return lastMethod;
        } catch (JavaModelException e) {
            e.printStackTrace();
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

    public static ICompilationUnit getCompilationUnit(ExecutionEvent event)
            throws ExecutionException {
        ISelection selection = HandlerUtil.getActiveMenuSelectionChecked(event);
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection sselection = (IStructuredSelection) selection;
            Object firstElement = sselection.getFirstElement();
            if (firstElement instanceof ICompilationUnit) {
                ICompilationUnit unit = (ICompilationUnit) firstElement;
                return unit;
            } // ココは、ソースコードの要素を選択して遷移したら、Unit じゃなくてIJavaElementがわたってくるゾ。
            else {
                System.out.println(firstElement.getClass().getName());
            }
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
                System.out.println(firstElement.getClass().getName());
            }
        }
        return null;
    }
}
