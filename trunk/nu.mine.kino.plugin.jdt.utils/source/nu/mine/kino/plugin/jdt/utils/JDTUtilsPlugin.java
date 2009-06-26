package nu.mine.kino.plugin.jdt.utils;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class JDTUtilsPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "nu.mine.kino.plugin.jdt.utils";

	// The shared instance
	private static JDTUtilsPlugin plugin;
	
	/**
	 * The constructor
	 */
	public JDTUtilsPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
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
	public static JDTUtilsPlugin getDefault() {
		return plugin;
	}

}
