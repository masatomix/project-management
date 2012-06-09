package nu.mine.kino.plugin.webrecorder.handlers;

import static nu.mine.kino.plugin.webrecorder.ProxyConstant.POST_BODY_FLAG;
import static nu.mine.kino.plugin.webrecorder.ProxyConstant.TRIM_FLAG;
import static nu.mine.kino.plugin.webrecorder.ProxyConstant.TRIM_LENGTH;
import static nu.mine.kino.plugin.webrecorder.ProxyConstant.TRIM_START_INDEX;
import nu.mine.kino.plugin.webrecorder.RecordMode;
import nu.mine.kino.plugin.webrecorder.WebRecorderPlugin;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class StartHandler extends AbstractHandler {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(StartHandler.class);

    /**
     * The constructor.
     */
    public StartHandler() {
    }

    /**
     * the command has been executed, so extract extract the needed information
     * from the application context.
     */
    public Object execute(ExecutionEvent event) throws ExecutionException {
        logger.debug("execute(ExecutionEvent) - start");

        RecordMode mode = null;

        String id = event.getCommand().getId();
        if (id.equals("nu.mine.kino.plugin.webrecorder.commands.RecordCommand")) {
            mode = RecordMode.RECORD;
        } else {
            mode = RecordMode.PLAY;
        }

        try {
            WebRecorderPlugin.getDefault().startServer(mode);
        } catch (Exception e) {
            logger.error("execute(ExecutionEvent)", e);
            e.printStackTrace();
            WebRecorderPlugin.logException(e, false);

        }

        // try {
        // ILaunchManager manager = DebugPlugin.getDefault()
        // .getLaunchManager();
        // ILaunchConfigurationType type = manager
        // .getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
        // ILaunchConfiguration[] configurations = manager
        // .getLaunchConfigurations(type);
        // for (int i = 0; i < configurations.length; i++) {
        // ILaunchConfiguration configuration = configurations[i];
        // if (configuration.getName().equals("Start Tomcat")) {
        // configuration.delete();
        // break;
        // }
        // }
        //
        // ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(
        // null, "Start Tomcat");
        //
        // workingCopy.setAttribute(
        // IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
        // "nu.mine.kino.plugin.jetty.HogeServer");
        // // /////////////////////////////////
        //
        // // // プラグインのクラスパスを取得するか
        // IPreferenceStore store = JettyPlugin.getDefault()
        // .getPreferenceStore();
        //
        // URL entry = JettyPlugin.getDefault().getBundle().getEntry("/");
        // String pluginDirectory = FileLocator.resolve(entry).getPath();
        //
        // // プログラムのクラスパスを通す
        // IPath programPath = new Path(pluginDirectory).append("lib").append(
        // "plugins.jar");
        // IRuntimeClasspathEntry programEntry = JavaRuntime
        // .newArchiveRuntimeClasspathEntry(programPath);
        // programEntry
        // .setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
        // // プログラムのクラスパスを通す以上
        //
        // // Jettyのクラスパスを通す
        // IPath jettyPath = new Path(pluginDirectory).append("lib").append(
        // "proxy-server.jar");
        // IRuntimeClasspathEntry jettyEntry = JavaRuntime
        // .newArchiveRuntimeClasspathEntry(jettyPath);
        // jettyEntry
        // .setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
        // // Jettyのクラスパスを通す 以上
        //
        // List<String> classpath = new ArrayList<String>();
        // classpath.add(programEntry.getMemento());
        // classpath.add(jettyEntry.getMemento());
        // // classpath.add(toolsEntry.getMemento());
        // // classpath.add(bootstrapEntry.getMemento());
        // // classpath.add(systemLibsEntry.getMemento());
        //
        // workingCopy
        // .setAttribute(
        // IJavaLaunchConfigurationConstants.ATTR_CLASSPATH,
        // classpath);
        // workingCopy.setAttribute(
        // IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH,
        // false);
        //
        // // ////////////////////////////
        // ILaunchConfiguration configuration = workingCopy.doSave();
        // DebugUITools.launch(configuration, ILaunchManager.RUN_MODE);
        //
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        logger.debug("execute(ExecutionEvent) - end");
        return null;
    }
}
