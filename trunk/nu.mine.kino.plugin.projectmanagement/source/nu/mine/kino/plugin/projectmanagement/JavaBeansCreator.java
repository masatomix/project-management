/******************************************************************************
 * Copyright (c) 2008 Masatomi KINO and others. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *      Masatomi KINO - initial API and implementation
 * $Id$
 ******************************************************************************/
//作成日: 2008/08/15
package nu.mine.kino.plugin.projectmanagement;

import java.util.List;

import nu.mine.kino.plugin.projectmanagement.sheetdata.IClassInformation;
import nu.mine.kino.plugin.projectmanagement.sheetdata.IFieldInformation;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class JavaBeansCreator extends BaseGenerator {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(JavaBeansCreator.class);

    public JavaBeansCreator(IJavaProject javaProject) {
        super(javaProject);
    }

    public ICompilationUnit create(IClassInformation info) throws CoreException {
        logger.debug("create(ClassInformation) - start"); //$NON-NLS-1$

        // フィールドのJavaProject情報インスタンスから、情報取得。
        IPackageFragmentRoot root = getSourceDir(getJavaProject());
        String pkg = info.getPackageName();
        // パッケージへのポインタ取得
        IPackageFragment pack = root.getPackageFragment(pkg);
        // パッケージが存在しなかったら作成する
        if (!pack.exists()) {
            pack = root.createPackageFragment(pkg, true,
                    new NullProgressMonitor());
        }

        // ソースコードの生成開始。
        String mainStatement = createMain(info);
        ICompilationUnit cu = pack.createCompilationUnit(info.getClassName()
                + ".java", mainStatement, true, new NullProgressMonitor()); //$NON-NLS-1$
        // packageついか
        // cu.createPackageDeclaration(pkg, new NullProgressMonitor());

        IType type = cu.getType(info.getClassName());

        // フィールド生成
        createField(type, info);
        // メソッドの生成
        createSetter(type, info);
        createGetter(type, info);

        // toStringの生成
        if (contains(info.getToString(), "○")) { //$NON-NLS-1$
            createToString(type, info);
        }
        // 書き出し。
        cu.save(new NullProgressMonitor(), true);

        logger.debug("create(ClassInformation) - end"); //$NON-NLS-1$
        return cu;
    }

    private String createMain(IClassInformation clazz) throws CoreException {
        // クラス情報から、Velocityでメイン部分を作成する。
        return executeVelocity("main.vm", new String[] { "class" }, //$NON-NLS-1$ //$NON-NLS-2$
                new IClassInformation[] { clazz });
    }

    /**
     * @param type
     */
    private void createField(IType type, IClassInformation info)
            throws CoreException {
        logger.debug("createField() start"); //$NON-NLS-1$
        StringBuffer buf = new StringBuffer();
        List<IFieldInformation> fieldInformations = info.getFieldInformations();
        for (IFieldInformation field : fieldInformations) {
            String result = executeVelocity("field.vm", //$NON-NLS-1$
                    new String[] { "field" }, new IFieldInformation[] { field }); //$NON-NLS-1$
            buf.append(result);
        }
        type.createField(buf.toString(), null, true, new NullProgressMonitor());
        logger.debug("createField() end"); //$NON-NLS-1$
    }

    private void createToString(IType type, IClassInformation info)
            throws CoreException {
        StringBuffer buf = new StringBuffer();

        List<IFieldInformation> fieldInformations = info.getFieldInformations();
        for (IFieldInformation field : fieldInformations) {
            String key = field.getFieldNameJ();
            String value = field.getFieldName();
            buf.append(".append(\""); //$NON-NLS-1$
            buf.append(key);
            buf.append("\","); //$NON-NLS-1$
            buf.append(value);
            buf.append(")"); //$NON-NLS-1$
        }
        buf.append(".toString();"); //$NON-NLS-1$
        // toStringの作成
        String toString = executeVelocity(
                "toString.vm", new String[] { "toString" }, new String[] { new String(buf) }); //$NON-NLS-1$ //$NON-NLS-2$
        type.createMethod(toString, null, true, new NullProgressMonitor());
    }

    private void createGetter(IType type, IClassInformation info)
            throws CoreException {
        logger.debug("createGetter() start"); //$NON-NLS-1$

        List<IFieldInformation> fieldInformations = info.getFieldInformations();
        for (IFieldInformation field : fieldInformations) {
            String prefix = null;
            // フィールドの型が,booleanとかの場合はgetでなくて、isにする。
            if (contains(field.getFieldType(), "boolean", "java.lang.Boolean")) { //$NON-NLS-1$ //$NON-NLS-2$
                prefix = "is"; //$NON-NLS-1$
            } else {
                prefix = "get"; //$NON-NLS-1$
            }
            String[] keys = new String[] { "field", "cname", "prefix" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            Object[] values = new Object[] { field,
                    Utils.createPropertyMethodName(field.getFieldName()),
                    prefix };

            // setterの作成
            // String setter = executeVelocity("setter.vm", keys, values);
            // //$NON-NLS-1$
            // type.createMethod(setter, null, true, new NullProgressMonitor());
            // getterの作成
            String getter = executeVelocity("getter.vm", keys, values); //$NON-NLS-1$
            type.createMethod(getter, null, true, new NullProgressMonitor());
        }
        logger.debug("createGetter() end"); //$NON-NLS-1$
    }

    private void createSetter(IType type, IClassInformation info)
            throws CoreException {
        logger.debug("createSetter() start"); //$NON-NLS-1$

        List<IFieldInformation> fieldInformations = info.getFieldInformations();
        for (IFieldInformation field : fieldInformations) {
            String[] keys = new String[] { "field", "cname" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            Object[] values = new Object[] { field,
                    Utils.createPropertyMethodName(field.getFieldName()) };

            // setterの作成
            String setter = executeVelocity("setter.vm", keys, values); //$NON-NLS-1$
            type.createMethod(setter, null, true, new NullProgressMonitor());
        }
        logger.debug("createSetter() end"); //$NON-NLS-1$
    }

    private boolean contains(String input, String... strs) {
        for (String str : strs) {
            // 配列に、inputと同じのがあれば、true
            if (input.equals(str)) {
                return true;
            }
        }
        return false;
    }

}
