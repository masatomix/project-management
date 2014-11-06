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

import nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IDetailInformation;
import nu.mine.kino.plugin.projectmanagement.copyutils.sheetdata.IUtilInformation;
import nu.mine.kino.plugin.projectmanagement.sheetdata.IClassInformation;

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
public class CopyUtilCreator extends BaseGenerator {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(CopyUtilCreator.class);

    public CopyUtilCreator(IJavaProject javaProject) {
        super(javaProject);
    }

    /**
     * 載せ替え情報から、JavaBeansの ICompilationUnit を生成する
     * 
     * @param info
     * @return
     * @throws CoreException
     */
    public ICompilationUnit create(IUtilInformation info) throws CoreException {
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
        String destClassName = !Utils.isEmpty(info.getDestIFName()) ? info
                .getDestIFName() : info.getDestClassName();
        String targetClassName = info.getSourceIFName() + "2" + destClassName;

        if (!Utils.isEmpty(info.getClassName())) { // クラス名が明示的に指定されている場合。
            targetClassName = info.getClassName();
        }

        ICompilationUnit cu = pack.createCompilationUnit(targetClassName
                + ".java", mainStatement, true, new NullProgressMonitor()); //$NON-NLS-1$
        // packageついか
        // cu.createPackageDeclaration(pkg, new NullProgressMonitor());

        IType type = cu.getType(targetClassName);

        createMethod(type, info);
        // 書き出し。
        cu.save(new NullProgressMonitor(), true);

        logger.debug("create(ClassInformation) - end"); //$NON-NLS-1$
        return cu;
    }

    private String createMain(IUtilInformation clazz) throws CoreException {
        // クラス情報から、Velocityでメイン部分を作成する。
        return executeVelocity("copyutil_main.vm", new String[] { "util" }, //$NON-NLS-1$ //$NON-NLS-2$
                new IUtilInformation[] { clazz });
    }

    /**
     * 載せ替え処理のソースコードを生成する。行ごと。
     * 
     * @param type
     * @param info
     * @param destClassName
     * @return
     */
    private String createCopyStatement(IType type, IDetailInformation info,
            String destClassName) {

        // dest.setFundName(source.getFundName()); こういうのを出力したい。
        StringBuffer buf = new StringBuffer();
        buf.append("((");
        buf.append(destClassName);
        buf.append(")");
        buf.append("dest).set");
        buf.append(Utils.createPropertyMethodName(info.getDestFieldName()));
        String logic = info.getLogic();
        if (Utils.isEmpty(logic)) {
            buf.append("( ");
            buf.append("source.get");
            buf.append(Utils.createPropertyMethodName(info.getSourceFieldName()));
            buf.append("() );");
        } else {
            buf.append("( ");
            buf.append(logic);
            buf.append(" );");
        }
        return new String(buf);
    }

    private void createMethod(IType type, IUtilInformation util)
            throws CoreException {
        StringBuffer buf = new StringBuffer();
        String destClassName = util.getDestClassName();

        List<IDetailInformation> fieldInformations = util.getDetails();
        for (IDetailInformation info : fieldInformations) {
            buf.append(createCopyStatement(type, info, destClassName));
            buf.append("\n");
        }
        String methodStatement = executeVelocity(
                "copyutil_method.vm", new String[] { "util", "copyStatement" }, new Object[] { util, new String(buf) }); //$NON-NLS-1$ //$NON-NLS-2$
        type.createMethod(methodStatement, null, true,
                new NullProgressMonitor());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * nu.mine.kino.plugin.beacon.generator.BaseGenerator#create(nu.mine.kino
     * .plugin.beacon.generator.sheetdata.IClassInformation)
     */
    @Override
    public ICompilationUnit create(IClassInformation info) throws CoreException {
        throw new UnsupportedOperationException();
    }

}