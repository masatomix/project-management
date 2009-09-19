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
package nu.mine.kino.plugin.jdt.utils.handlers;

import java.lang.reflect.InvocationTargetException;

import nu.mine.kino.plugin.jdt.utils.JDTUtils;
import nu.mine.kino.plugin.jdt.utils.JDTUtilsPlugin;
import nu.mine.kino.plugin.jdt.utils.WorkbenchRunnableAdapter;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.actions.OrganizeImportsAction;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * GUIから呼び出されて、選択したソースに対して、toStringを追加するアクションハンドラです。 選択したオブジェクトが
 * {@link ICompilationUnit}の時は含まれるクラス全てに対してtoStringメソッドを追加します。 選択したオブジェクトが
 * {@link IType}の時は、選択しているクラスに対してのみ、toStringメソッドを追加します。
 * 
 * @author Masatomi KINO
 * @version $Revision$
 */
public class AddToStringHandler extends AbstractHandler implements IHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        final IJavaElement element = JDTUtils.getJavaElement(event);
        // targets:処理対象のクラス群。
        IJavaElement[] targets = null;

        // ソース自体を選択した場合、その配下のクラス達を処理対象にする。
        if (element instanceof ICompilationUnit) {
            // eventからCompilationUnitを取得。
            final ICompilationUnit unit = (ICompilationUnit) element;
            // unitから、子要素たちをIJavaElementの配列として取得。
            // このIJavaElement達は、パッケージだったり、ITypeだったりする。
            targets = JDTUtils.getChildren(unit);
        }
        // ソース下のクラス名を選択した場合、そいつだけを処理対象にする。
        else if (element.getElementType() == IJavaElement.TYPE) {
            IType type = (IType) element;
            IJavaElement[] tmpTargets = new IJavaElement[] { type };
            targets = tmpTargets;
        }

        // 該当ファイルを開いておく。
        ICompilationUnit unit = (ICompilationUnit) element
                .getAncestor(IJavaElement.COMPILATION_UNIT);
        JDTUtils.openEditor(unit);

        try {
            // 別スレを起動して、実行して終わり。
            IWorkbenchSite site = HandlerUtil.getActiveSite(event);
            AddToStringThread op = new AddToStringThread(targets, site);
            PlatformUI.getWorkbench().getProgressService().runInUI(
                    PlatformUI.getWorkbench().getProgressService(),
                    new WorkbenchRunnableAdapter(op, op.getScheduleRule()),
                    op.getScheduleRule());
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            JDTUtilsPlugin.logException(targetException);
        } catch (InterruptedException e) {
            JDTUtilsPlugin.logException(e, false);
        }
        // ////////////////////////////////////////////////////////////////////////////
        return null;
    }

    /**
     * Eclipse ワークベンチ内のダイアログ上で、実際に処理を実行するスレッド。
     * 
     * @author Masatomi KINO
     * @version $Revision$
     */
    private class AddToStringThread implements IWorkspaceRunnable {
        // 基本的にはITypeの配列。たまにパッケージ宣言(IPackageDeclaration)が混じってる。
        private final IJavaElement[] targets;

        private final IWorkbenchSite site;

        public AddToStringThread(IJavaElement[] targets, IWorkbenchSite site) {
            this.targets = targets;
            this.site = site;
        }

        public ISchedulingRule getScheduleRule() {
            return ResourcesPlugin.getWorkspace().getRoot();
        }

        public void run(IProgressMonitor monitor) throws CoreException {
            addToString(targets, monitor);
            importActionRun();
        }

        private void importActionRun() {
            // 以下は、ICompilationUnitを取得して、import文を綺麗にするアクションを実行する。
            ICompilationUnit unit = (ICompilationUnit) targets[0]
                    .getAncestor(IJavaElement.COMPILATION_UNIT);
            OrganizeImportsAction importsAction = new OrganizeImportsAction(
                    site);
            IStructuredSelection selection = new StructuredSelection(unit);
            importsAction.run(selection);
        }

        /**
         * @param targets
         * @param monitor
         * @throws CoreException
         */
        private void addToString(IJavaElement[] targets,
                IProgressMonitor monitor) throws CoreException {
            if (targets == null || targets.length == 0) {
                return;
            }
            try {
                monitor.beginTask("toStringを追加します", 5);
                // ITextFileBufferManagerの取得。
                ITextFileBufferManager manager = FileBuffers
                        .getTextFileBufferManager();
                // IPath path = unit.getPath();
                IPath path = targets[0].getPath();
                // ファイルにconnect
                SubProgressMonitor subMonitor = new SubProgressMonitor(monitor,
                        4);// monitorの5のうち、4を受け持つ。
                subMonitor.beginTask("", targets.length);
                manager.connect(path, LocationKind.IFILE, subMonitor);
                try {
                    // document取得。
                    IDocument document = manager.getTextFileBuffer(path,
                            LocationKind.IFILE).getDocument();
                    String lineDelim = TextUtilities
                            .getDefaultLineDelimiter(document);
                    IJavaProject project = targets[0].getJavaProject();

                    // このクラスが引数のプロジェクトで利用可能かをチェックする。
                    boolean canUseToStringBuilder = JDTUtils.canUseThisClass(
                            project,
                            "org.apache.commons.lang.builder.ToStringBuilder",
                            subMonitor);

                    // 子要素は、パッケージ宣言だったり、クラスだったりする。一つのソースに複数クラスが書いてある場合もあるし。
                    for (final IJavaElement javaElement : targets) {
                        // ↓型(クラス)だったらば、ITypeにキャストしていい。
                        if (javaElement.getElementType() == IJavaElement.TYPE) { // IPackageDeclarationは除外したいので。
                            IType type = (IType) javaElement;
                            String createToString = JDTUtils.createToString(
                                    type, lineDelim, project,
                                    canUseToStringBuilder);
                            type.createMethod(createToString, null, true,
                                    subMonitor);
                        }
                        subMonitor.worked(1);
                    }
                } finally {
                    manager.disconnect(path, LocationKind.IFILE, subMonitor);
                    subMonitor.done();
                }
            } finally {
                monitor.worked(1);
                monitor.done();
            }
        }
    }

}
