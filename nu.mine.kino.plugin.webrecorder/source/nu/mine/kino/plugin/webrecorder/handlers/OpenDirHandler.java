package nu.mine.kino.plugin.webrecorder.handlers;

import java.io.IOException;
import java.text.MessageFormat;

import nu.mine.kino.plugin.webrecorder.WebRecorderPlugin;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class OpenDirHandler extends AbstractHandler {
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
        String cacheBasepath = WebRecorderPlugin.getDefault()
                .getCacheBasepath();
        String target = "explorer.exe {0}";// OSÇå©Çƒë÷Ç¶ÇÈ
        target = MessageFormat.format(target, new Object[] { cacheBasepath });
        try {
            Runtime.getRuntime().exec(target);
        } catch (IOException e) {
            // TODO é©ìÆê∂ê¨Ç≥ÇÍÇΩ catch ÉuÉçÉbÉN
            e.printStackTrace();
        }
        return null;
    }

    // private boolean isSupported() {
    // String osName = System.getProperty("os.name").toLowerCase();
    // return ((osName.indexOf("windows") != -1));
    // }
}
