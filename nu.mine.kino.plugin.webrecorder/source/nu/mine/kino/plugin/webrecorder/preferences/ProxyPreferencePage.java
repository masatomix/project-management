package nu.mine.kino.plugin.webrecorder.preferences;

import org.apache.log4j.Logger;

import java.io.File;

import nu.mine.kino.plugin.webrecorder.ProxyConstant;
import nu.mine.kino.plugin.webrecorder.WebRecorderPlugin;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class ProxyPreferencePage extends FieldEditorPreferencePage implements
        IWorkbenchPreferencePage {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger
            .getLogger(ProxyPreferencePage.class);

    public ProxyPreferencePage() {
        super(GRID);
        setPreferenceStore(WebRecorderPlugin.getDefault().getPreferenceStore());
        setDescription("Proxy Serverの設定情報を変更できます。");
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common
     * GUI blocks needed to manipulate various types of preferences. Each field
     * editor knows how to save and restore itself.
     */
    public void createFieldEditors() {
        // addField(new DirectoryFieldEditor(PreferenceConstants.P_PATH,
        // "&Directory preference:", getFieldEditorParent()));
        // addField(new BooleanFieldEditor(PreferenceConstants.P_BOOLEAN,
        // "&An example of a boolean preference", getFieldEditorParent()));
        //
        // addField(new RadioGroupFieldEditor(PreferenceConstants.P_CHOICE,
        // "An example of a multiple-choice preference", 1,
        // new String[][] { { "&Choice 1", "choice1" },
        // { "C&hoice 2", "choice2" } }, getFieldEditorParent()));
        // addField(new StringFieldEditor(PreferenceConstants.P_STRING,
        // "A &text preference:", getFieldEditorParent()));

        addField(new DirectoryFieldEditor(ProxyConstant.CACHE_BASE_PATH,
                "&Cache Base Dir:", getFieldEditorParent()));
        addField(new IntegerFieldEditor(ProxyConstant.PORT, "&Port:",
                getFieldEditorParent()));

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
        String cacheBasepath = WebRecorderPlugin.getDefault()
                .getCacheBasepath();
        logger.debug("キャッシュディレクトリ: " + cacheBasepath);
        new File(cacheBasepath).mkdirs();
    }

}