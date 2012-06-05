package nu.mine.kino.plugin.webrecorder.preferences;

import java.io.File;

import nu.mine.kino.plugin.webrecorder.ProxyConstant;
import nu.mine.kino.plugin.webrecorder.WebRecorderPlugin;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
     * initializeDefaultPreferences()
     */
    public void initializeDefaultPreferences() {
        IPreferenceStore store = WebRecorderPlugin.getDefault()
                .getPreferenceStore();
        // store.setDefault(PreferenceConstants.P_BOOLEAN, true);
        // store.setDefault(PreferenceConstants.P_CHOICE, "choice2");
        // store.setDefault(PreferenceConstants.P_STRING, "Default value");

        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();
        File rootDir = root.getLocation().toFile();
        store.setDefault(ProxyConstant.CACHE_BASE_PATH, new File(rootDir,
                "cache").getAbsolutePath());
        store.setDefault(ProxyConstant.PORT, 8008);

    }

}
