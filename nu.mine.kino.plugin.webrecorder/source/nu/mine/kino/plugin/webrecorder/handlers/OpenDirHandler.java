package nu.mine.kino.plugin.webrecorder.handlers;

import static nu.mine.kino.plugin.webrecorder.ProxyConstant.POST_BODY_FLAG;
import static nu.mine.kino.plugin.webrecorder.ProxyConstant.TRIM_FLAG;
import static nu.mine.kino.plugin.webrecorder.ProxyConstant.TRIM_LENGTH;
import static nu.mine.kino.plugin.webrecorder.ProxyConstant.TRIM_START_INDEX;

import java.io.IOException;
import java.text.MessageFormat;

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
public class OpenDirHandler extends AbstractHandler {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(OpenDirHandler.class);

    /**
     * The constructor.
     */
    public OpenDirHandler() {
    }

    /**
     * the command has been executed, so extract extract the needed information
     * from the application context.
     */
    public Object execute(ExecutionEvent event) throws ExecutionException {

        IPreferenceStore store = WebRecorderPlugin.getDefault()
                .getPreferenceStore();
        logger.debug("POST_BODY_FLAG: " + store.getBoolean(POST_BODY_FLAG));
        logger.debug("TRIM_FLAG     : " + store.getBoolean(TRIM_FLAG));
        logger.debug("START_INDEX   : " + store.getInt(TRIM_START_INDEX));
        logger.debug("LENGTH        : " + store.getInt(TRIM_LENGTH));

        String cacheBasepath = WebRecorderPlugin.getDefault()
                .getCacheBasepath();

        String target = null;

        if (isMac()) {
            target = "Open {0}";
        }
        if (isWindows()) {
            target = "explorer.exe {0}";// OSÇå©Çƒë÷Ç¶ÇÈ
        }

        if (target == null) {
            return null;
        }

        target = MessageFormat.format(target, new Object[] { cacheBasepath });
        try {
            Runtime.getRuntime().exec(target);
        } catch (IOException e) {
            // TODO é©ìÆê∂ê¨Ç≥ÇÍÇΩ catch ÉuÉçÉbÉN
            e.printStackTrace();
        }
        return null;
    }

    private boolean isWindows() {
        String osName = System.getProperty("os.name").toLowerCase();
        return ((osName.indexOf("windows") != -1));
    }

    private boolean isMac() {
        String osName = System.getProperty("os.name").toLowerCase();
        return ((osName.indexOf("mac") != -1));
    }
}
