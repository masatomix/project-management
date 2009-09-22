package nu.mine.kino.plugin.beangenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import net.java.amateras.xlsbeans.XLSBeans;
import net.java.amateras.xlsbeans.XLSBeansException;
import nu.mine.kino.plugin.beangenerator.sheetdata.ClassInformationSheet;
import nu.mine.kino.plugin.beangenerator.sheetdata.IClassInformation;
import nu.mine.kino.plugin.beangenerator.sheetdata.anno.ClassInformationSheetWithAnno;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(Activator.class);

    // The plug-in ID
    public static final String PLUGIN_ID = "nu.mine.kino.plugin.beangenerator"; //$NON-NLS-1$

    // The shared instance
    private static Activator plugin;

    /**
     * The constructor
     */
    public Activator() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }

    public List<IClassInformation> getClassInformations(IFile file)
            throws CoreException {
        logger.debug("getClassInformations(IFile) - start"); //$NON-NLS-1$

        File instance = file.getLocation().toFile();
        try {
            InputStream in = new FileInputStream(instance);
            // ここでアノテーションからJavaBeansのマッピングがされ、インスタンスまで生成される
            ClassInformationSheetWithAnno sheet = new XLSBeans().load(in,
                    ClassInformationSheetWithAnno.class);
            logger.debug("getClassInformations(IFile) - end"); //$NON-NLS-1$
            return sheet.getClassInformation();
        } catch (FileNotFoundException e) {
            logger.error("getClassInformations(IFile)", e); //$NON-NLS-1$
            IStatus status = new Status(IStatus.ERROR, Activator.getPluginId(),
                    IStatus.OK, e.getMessage(), e);
            throw new CoreException(status);
        } catch (XLSBeansException e) {
            logger.warn(e);
            logger.warn("Annotationナシでもとおるロジックを実行してみる。"); //$NON-NLS-1$
        }
        return getClassInformationsWithoutAnno(instance);
    }

    private List<IClassInformation> getClassInformationsWithoutAnno(
            File instance) throws CoreException {
        logger.debug("getClassInformationsWithoutAnno(File) - start"); //$NON-NLS-1$

        try {
            InputStream in = new FileInputStream(instance);
            // ここでアノテーションからJavaBeansのマッピングがされ、インスタンスまで生成される
            ClassInformationSheet sheet = new XLSBeans().load(in,
                    ClassInformationSheet.class);
            logger.debug("getClassInformationsWithoutAnno(File) - end"); //$NON-NLS-1$
            return sheet.getClassInformation();
        } catch (FileNotFoundException e) {
            logger.error("getClassInformationsWithoutAnno(File)", e); //$NON-NLS-1$

            IStatus status = new Status(IStatus.ERROR, Activator.getPluginId(),
                    IStatus.OK, e.getMessage(), e);
            throw new CoreException(status);
        } catch (XLSBeansException e) {
            logger.error("getClassInformationsWithoutAnno(File)", e); //$NON-NLS-1$

            IStatus status = new Status(IStatus.ERROR, Activator.getPluginId(),
                    IStatus.OK, e.getMessage(), e);
            throw new CoreException(status);
        }
    }

    // ////////////////////
    public static String getPluginId() {
        return getDefault().getBundle().getSymbolicName();
    }

    public static void logException(Throwable e) {
        logException(e, true);
    }

    public static void logException(Throwable e, boolean showErrorDialog) {
        if (e instanceof InvocationTargetException) {
            e = ((InvocationTargetException) e).getTargetException();
        }

        IStatus status = null;
        if (e instanceof CoreException) {
            status = ((CoreException) e).getStatus();
        } else {
            String message = e.getMessage();
            if (message == null)
                message = e.toString();
            status = new Status(IStatus.ERROR, getPluginId(), IStatus.OK,
                    message, e);
        }
        log(status, showErrorDialog);
    }

    public static void log(IStatus status, boolean showErrorDialog) {
        if (status.getSeverity() != IStatus.INFO) {
            if (showErrorDialog) {
                ErrorDialog.openError(getActiveWorkbenchShell(), null, null,
                        status);
            }
            Bundle bundle = Platform.getBundle(PLUGIN_ID);
            Platform.getLog(bundle).log(status);
        } else {
            MessageDialog.openInformation(getActiveWorkbenchShell(), null,
                    status.getMessage());
        }
    }

    /**
     * Returns the standard display to be used. The method first checks, if the
     * thread calling this method has an associated disaply. If so, this display
     * is returned. Otherwise the method returns the default display.
     */
    public static Display getStandardDisplay() {
        Display display;
        display = Display.getCurrent();
        if (display == null)
            display = Display.getDefault();
        return display;
    }

    public static Shell getActiveWorkbenchShell() {
        IWorkbenchWindow window = getActiveWorkbenchWindow();
        return window != null ? window.getShell() : getStandardDisplay()
                .getActiveShell();
    }

    public static IWorkbenchWindow getActiveWorkbenchWindow() {
        IWorkbench workbench = getDefault().getWorkbench();
        return workbench.getActiveWorkbenchWindow();
    }

}
