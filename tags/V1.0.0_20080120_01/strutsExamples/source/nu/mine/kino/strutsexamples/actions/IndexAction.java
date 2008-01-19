package nu.mine.kino.strutsexamples.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @version 1.0
 * @author
 */
public class IndexAction extends Action {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(IndexAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        saveToken(request);
        logger.debug(request.getSession().getAttribute(
                Globals.TRANSACTION_TOKEN_KEY));
        return mapping.findForward("success");
    }
}
