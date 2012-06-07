package nu.mine.kino.plugin.webrecorder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jetty.server.Server;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
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

    // /////////////////////////////////////////////

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

    public File getCachePathFromRequest(ServletRequest request) {
        String requestURI = ((HttpServletRequest) request).getRequestURI();
        String host = ((HttpServletRequest) request).getHeader("Host");
        host = host.replace(':', '/');
        File hostDir = new File(getCacheBasepath(), host);

        StringBuffer buf = new StringBuffer();
        buf.append(requestURI);
        // queryがあれば付けるただしSha1ハッシュして
        String queryString = ((HttpServletRequest) request).getQueryString();
        if (queryString != null && !"".equals(queryString)) {
            String shaHex = DigestUtils.shaHex(queryString.getBytes());
            logger.debug("queryをSHA1ハッシュ: " + shaHex);
            buf.append("_");
            buf.append(shaHex);
        }
        // buf.append(".txt");

        File file = new File(hostDir, new String(buf));
        return file;
    }

    public String getBody(HttpServletRequest hRequest) throws IOException {
        BufferedReader reader = hRequest.getReader();
        StringBuffer bodyBuf = new StringBuffer();
        try {
            String str = null;
            while ((str = reader.readLine()) != null) {
                bodyBuf.append(str);
            }
        } finally {
            reader.close();
        }
        String body = new String(bodyBuf);
        return body;
    }

    public File getCachePathFromRequestForPost(ServletRequest request) {
        String requestURI = ((HttpServletRequest) request).getRequestURI();
        String host = ((HttpServletRequest) request).getHeader("Host");
        host = host.replace(':', '/');
        File hostDir = new File(getCacheBasepath(), host);

        StringBuffer buf = new StringBuffer();
        buf.append(requestURI);
        // Bodyがあれば付けるただしSha1ハッシュして
        try {
            String body = getBody((HttpServletRequest) request);
            if (getPreferenceStore().getBoolean(ProxyConstant.TRIM_FLAG)) {
                int startIndex = getPreferenceStore().getInt(
                        ProxyConstant.TRIM_START_INDEX);
                int length = getPreferenceStore().getInt(
                        ProxyConstant.TRIM_LENGTH);
                if (length <= 0) {
                    body = body.substring(startIndex);
                } else {
                    body = body.substring(startIndex, startIndex + length);
                }
            }
            if (body != null && !"".equals(body)) {
                String shaHex = DigestUtils.shaHex(body.getBytes());
                logger.debug("bodyをSHA1ハッシュ: " + shaHex);
                buf.append("_");
                buf.append(shaHex);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // buf.append(".txt");

        File file = new File(hostDir, new String(buf));
        return file;
    }
}
