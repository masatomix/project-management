package nu.mine.kino.plugin.beangenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import net.java.amateras.xlsbeans.XLSBeans;
import net.java.amateras.xlsbeans.XLSBeansException;
import nu.mine.kino.exception.ApplicationException;
import nu.mine.kino.utils.beangenerator.sheetdata.ClassInformation;
import nu.mine.kino.utils.beangenerator.sheetdata.ClassInformationSheet;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "nu.mine.kino.plugin.beangenerator";

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

    public List<ClassInformation> getClassInformations(IFile file)
            throws CoreException {
        File instance = file.getLocation().toFile();

        try {
            InputStream in = new FileInputStream(instance);
            // ここでアノテーションからJavaBeansのマッピングがされ、インスタンスまで生成される
            ClassInformationSheet sheet = new XLSBeans().load(in,
                    ClassInformationSheet.class);
            return sheet.getClassInformation();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XLSBeansException e) {
            e.printStackTrace();
        }
        return null;
    }

}
