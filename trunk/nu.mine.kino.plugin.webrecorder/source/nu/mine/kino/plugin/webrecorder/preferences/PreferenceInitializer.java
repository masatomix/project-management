package nu.mine.kino.plugin.webrecorder.preferences;

import static nu.mine.kino.plugin.webrecorder.ProxyConstant.CACHE_BASE_PATH;
import static nu.mine.kino.plugin.webrecorder.ProxyConstant.PORT;
import static nu.mine.kino.plugin.webrecorder.ProxyConstant.POST_BODY_FLAG;
import static nu.mine.kino.plugin.webrecorder.ProxyConstant.TRIM_FLAG;
import static nu.mine.kino.plugin.webrecorder.ProxyConstant.TRIM_LENGTH;
import static nu.mine.kino.plugin.webrecorder.ProxyConstant.TRIM_START_INDEX;

import java.io.File;

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

        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();
        File rootDir = root.getLocation().toFile();
        File file = new File(rootDir, "_WebRecorderCache");
        store.setDefault(CACHE_BASE_PATH, file.getAbsolutePath());
        store.setDefault(PORT, 8008);
        store.setDefault(POST_BODY_FLAG, true);
        store.setDefault(TRIM_FLAG, false);
        store.setDefault(TRIM_START_INDEX, 0);
        store.setDefault(TRIM_LENGTH, 0);

    }

}
