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
package nu.mine.kino.plugin.beangenerator;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import nu.mine.kino.utils.beangenerator.sheetdata.ClassInformation;
import nu.mine.kino.utils.beangenerator.sheetdata.FieldInformation;

import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class JavaBeansCreator {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(JavaBeansCreator.class);

    private final IJavaProject javaProject;

    public JavaBeansCreator(IJavaProject javaProject) {
        this.javaProject = javaProject;
        // Velocity初期化
        URL entry = Activator.getDefault().getBundle().getEntry("/"); //$NON-NLS-1$
        try {
            String pluginDirectory = FileLocator.resolve(entry).getPath();
            File file = new File(pluginDirectory, "lib"); //$NON-NLS-1$
            Properties p = new Properties();
            p.setProperty("file.resource.loader.path", file.getAbsolutePath()); //$NON-NLS-1$
            Velocity.init(p);
            // どうも上のディレクトリとかがなくってもエラーにならないっぽいので、そのままつぶしちゃおう。
        } catch (IOException e) {
            logger.error("JavaBeansCreator()", e); //$NON-NLS-1$
            Activator.logException(e);
        } catch (Exception e) {
            logger.error("JavaBeansCreator()", e); //$NON-NLS-1$
            Activator.logException(e);
        }
    }

    public ICompilationUnit create(ClassInformation info) throws CoreException {
        logger.debug("create(ClassInformation) - start"); //$NON-NLS-1$

        // フィールドのJavaProject情報インスタンスから、情報取得。
        IPackageFragmentRoot root = getSourceDir(javaProject);
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
        cu.createPackageDeclaration(pkg, new NullProgressMonitor());

        IType type = cu.getType(info.getClassName());

        // フィールド生成
        createField(type, info);
        // メソッドの生成
        createMethod(type, info);
        // 書き出し。
        cu.save(new NullProgressMonitor(), true);

        logger.debug("create(ClassInformation) - end"); //$NON-NLS-1$
        return cu;
    }

    private String executeVelocity(String vm, String[] names, Object[] objs)
            throws CoreException {
        logger.debug("executeVelocity(String, String[], Object[]) - start"); //$NON-NLS-1$

        try {
            VelocityContext context = new VelocityContext();
            for (int i = 0; i < names.length; i++) {
                context.put(names[i], objs[i]);
            }
            StringWriter out = new StringWriter();
            Template template = Velocity.getTemplate(vm, "MS932"); //$NON-NLS-1$
            template.merge(context, out);
            String result = out.toString();
            out.flush();
            logger.debug("executeVelocity(String, String[], Object[]) - end"); //$NON-NLS-1$
            return result;
        } catch (Exception e) {
            logger.error("executeVelocity(String, String[], Object[])", e); //$NON-NLS-1$
            IStatus status = new Status(IStatus.ERROR, Activator.getPluginId(),
                    IStatus.OK, e.getMessage(), e);
            throw new CoreException(status);
        }
    }

    private String createMain(ClassInformation clazz) throws CoreException {
        // クラス情報から、Velocityでメイン部分を作成する。
        return executeVelocity("main.vm", new String[] { "class" }, //$NON-NLS-1$ //$NON-NLS-2$
                new ClassInformation[] { clazz });
    }

    /**
     * @param type
     */
    private void createField(IType type, ClassInformation info)
            throws CoreException {
        logger.debug("createField() start"); //$NON-NLS-1$
        StringBuffer buf = new StringBuffer();
        List<FieldInformation> fieldInformations = info.getFieldInformations();
        for (FieldInformation field : fieldInformations) {
            String result = executeVelocity("field.vm", //$NON-NLS-1$
                    new String[] { "field" }, new FieldInformation[] { field }); //$NON-NLS-1$
            buf.append(result);
        }
        type.createField(buf.toString(), null, true, new NullProgressMonitor());
        logger.debug("createField() end"); //$NON-NLS-1$
    }

    private void createMethod(IType type, ClassInformation info)
            throws CoreException {
        logger.debug("createMethod() start"); //$NON-NLS-1$

        List<FieldInformation> fieldInformations = info.getFieldInformations();
        for (FieldInformation field : fieldInformations) {
            String prefix = null;
            // フィールドの型が,booleanとかの場合はgetでなくて、isにする。
            if (contains(field.getFieldType(), "boolean", "java.lang.Boolean")) { //$NON-NLS-1$ //$NON-NLS-2$
                prefix = "is"; //$NON-NLS-1$
            } else {
                prefix = "get"; //$NON-NLS-1$
            }
            String[] keys = new String[] { "field", "cname", "prefix" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            Object[] values = new Object[] { field,
                    WordUtils.capitalize(field.getFieldName()), prefix };
            // setterの作成
            String setter = executeVelocity("setter.vm", keys, values); //$NON-NLS-1$
            type.createMethod(setter, null, true, new NullProgressMonitor());
            // getterの作成
            String getter = executeVelocity("getter.vm", keys, values); //$NON-NLS-1$
            type.createMethod(getter, null, true, new NullProgressMonitor());
        }
        logger.debug("createMethod() end"); //$NON-NLS-1$
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

    private IPackageFragmentRoot getSourceDir(IJavaProject javaProject)
            throws CoreException {
        IPackageFragmentRoot[] roots = javaProject.getPackageFragmentRoots();
        for (IPackageFragmentRoot root : roots) {
            if (root.getKind() == IPackageFragmentRoot.K_SOURCE) {
                // 複数あるかもね。先に決まった方で返しちゃう。
                return root;
            }
        }
        // ソースディレクトリがとれないので、エラー。
        IStatus status = new Status(IStatus.ERROR, Activator.getPluginId(),
                IStatus.OK, Messages.JavaBeansCreator_MSG_SRCDIR_NOT_FOUND, null);
        throw new CoreException(status);
    }

}
