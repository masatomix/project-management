package nu.mine.kino.plugin.webrecorder;

import static nu.mine.kino.plugin.webrecorder.ProxyConstant.EXCEPT_EXTs;

import java.lang.reflect.InvocationTargetException;

import nu.mine.kino.plugin.commons.utils.StringUtils;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jetty.server.Server;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class WebRecorderPlugin extends AbstractUIPlugin {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(WebRecorderPlugin.class);

    private Server server;

    // The plug-in ID
    public static final String PLUGIN_ID = "nu.mine.kino.plugin.webrecorder"; //$NON-NLS-1$

    // The shared instance
    private static WebRecorderPlugin plugin;

    /**
     * The constructor
     */
    public WebRecorderPlugin() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
     * )
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
     * )
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
    public static WebRecorderPlugin getDefault() {
        return plugin;
    }

    // /////////////////////////////////////////

    // /////////////////////////////////////////
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
        //        Bundle bundle = Platform.getBundle("org.eclipse.update.ui"); //$NON-NLS-1$
        getDefault().getLog().log(status);
        // Platform.getLog(bundle).log(status);
        if (Display.getCurrent() == null || !showErrorDialog)
            return;
        if (status.getSeverity() != IStatus.INFO) {
            ErrorDialog
                    .openError(getActiveWorkbenchShell(), null, null, status);
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
        return getDefault().getWorkbench().getActiveWorkbenchWindow();
    }

    // //////////////////////
    // 拡張子の除外リストを作ろう
    // 拡張子のOKリストを作ろう。
    public int getPort() {
        return getPreferenceStore().getInt(ProxyConstant.PORT);
    }

    public String getCacheBasepath() {
        return getPreferenceStore().getString(ProxyConstant.CACHE_BASE_PATH);
    }

    public void startServer(final RecordMode mode) throws Exception {
        if (server != null) {
            MessageDialog.openInformation(getActiveWorkbenchShell(), "すでに起動中",
                    "すでにサーバが起動中です");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                server = new Server();
                try {
                    Utils.startJettyServer(server, getPort(), mode);
                } catch (InterruptedException e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void stopServer() throws Exception {
        if (server != null && server.isRunning()) {
            server.stop();
        }
        server = null;
    }

    public void printURLConsole(String url) {
        if (StringUtils.isEmpty(url) || StringUtils.endsWith(url, EXCEPT_EXTs)) {
            return;
        }
        printConsole(url);
    }

    public void printURLConsole(String format, String url) {
        if (StringUtils.isEmpty(url) || StringUtils.endsWith(url, EXCEPT_EXTs)) {
            return;
        }

        printConsole(NLS.bind(format, url));
    }

    public void printConsole(String message) {
        MessageConsole myConsole = findConsole(ProxyConstant.CONSOLE_ID);
        MessageConsoleStream out = myConsole.newMessageStream();
        out.println(message);
    }

    public MessageConsole findConsole(String name) {
        ConsolePlugin plugin = ConsolePlugin.getDefault();
        IConsoleManager conMan = plugin.getConsoleManager();
        IConsole[] existing = conMan.getConsoles();
        for (int i = 0; i < existing.length; i++)
            if (name.equals(existing[i].getName()))
                return (MessageConsole) existing[i];
        // no console found, so create a new one
        MessageConsole myConsole = new MessageConsole(name, null);
        conMan.addConsoles(new IConsole[] { myConsole });
        return myConsole;
    }

    public void showConsole(IWorkbenchPage page) throws PartInitException {
        IConsoleView view = (IConsoleView) page
                .showView(IConsoleConstants.ID_CONSOLE_VIEW);
        view.display(findConsole(ProxyConstant.CONSOLE_ID));
    }
}
