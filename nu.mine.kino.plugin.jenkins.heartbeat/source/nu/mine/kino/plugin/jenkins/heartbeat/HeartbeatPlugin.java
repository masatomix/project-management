package nu.mine.kino.plugin.jenkins.heartbeat;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class HeartbeatPlugin extends AbstractUIPlugin {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(HeartbeatPlugin.class);

    public static final String IMG_EXIST = "existImange";

    public static final String IMG_ERROR = "errorImager";

    // The plug-in ID
    public static final String PLUGIN_ID = "nu.mine.kino.plugin.jenkins.heartbeat"; //$NON-NLS-1$

    // The shared instance
    private static HeartbeatPlugin plugin;

    @Override
    protected void initializeImageRegistry(ImageRegistry reg) {
        super.initializeImageRegistry(reg);
        reg.put(IMG_EXIST, getImageDescriptor("/icons/connect.png"));
        reg.put(IMG_ERROR, getImageDescriptor("/icons/cancel.png"));

    }

    /**
     * The constructor
     */
    public HeartbeatPlugin() {
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
    public static HeartbeatPlugin getDefault() {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in
     * relative path
     * 
     * @param path
     *            the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    private int num = 0;

    public int check(String username, String password) throws IOException {

        // System.out.println(username);
        // System.out.println(password);
        Document document = Jsoup.connect(
                getPreferenceStore().getString(Constants.BASE_URL)).get();
        System.out.println(document);
        num++;
        System.out.println(num);
        return num % 3;
    }

    // public Element getNexeShortMail(String username, String password)
    // throws IOException {
    // Document document = Jsoup.connect(
    // getPreferenceStore().getString(Constants.BASE_URL)).get();
    // System.out.println(document);
    // return document;
    // }
}
