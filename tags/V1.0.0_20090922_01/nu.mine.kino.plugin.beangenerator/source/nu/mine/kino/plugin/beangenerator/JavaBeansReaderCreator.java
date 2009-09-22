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
import java.util.Properties;

import nu.mine.kino.plugin.beangenerator.sheetdata.IClassInformation;

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

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
public class JavaBeansReaderCreator {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(JavaBeansReaderCreator.class);

    private final IJavaProject javaProject;

    public JavaBeansReaderCreator(IJavaProject javaProject) {
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
            logger.error("JavaBeansReaderCreator()", e); //$NON-NLS-1$
            Activator.logException(e);
        } catch (Exception e) {
            logger.error("JavaBeansReaderCreator()", e); //$NON-NLS-1$
            Activator.logException(e);
        }
    }

    public ICompilationUnit create(IClassInformation info) throws CoreException {
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
                + "Sheet.java", mainStatement, true, new NullProgressMonitor()); //$NON-NLS-1$
        // packageついか
        // cu.createPackageDeclaration(pkg, new NullProgressMonitor());

        // IType type = cu.getType(info.getClassName());
        // if (contains(info.getToString(), "○")) { //$NON-NLS-1$
        // createToString(type, info);
        // }
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

    private String createMain(IClassInformation clazz) throws CoreException {
        // クラス情報から、Velocityでメイン部分を作成する。
        return executeVelocity("reader.vm", new String[] { "class" }, //$NON-NLS-1$ //$NON-NLS-2$
                new IClassInformation[] { clazz });
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
                IStatus.OK, Messages.JavaBeansCreator_MSG_SRCDIR_NOT_FOUND,
                null);
        throw new CoreException(status);
    }

}
