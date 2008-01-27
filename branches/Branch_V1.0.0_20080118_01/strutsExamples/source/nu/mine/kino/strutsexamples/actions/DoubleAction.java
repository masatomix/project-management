package nu.mine.kino.strutsexamples.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
public class DoubleAction extends Action {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(DoubleAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        logger
                .debug("execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - start");

        ActionForward forward = new ActionForward(); // return value

        HttpSession session = request.getSession();
        boolean tokenValid = false;
        synchronized (session) {
            tokenValid = isTokenValid(request);
            logger.debug(tokenValid + ":"
                    + session.getAttribute(Globals.TRANSACTION_TOKEN_KEY));
            System.out.println(tokenValid + ":"
                    + session.getAttribute(Globals.TRANSACTION_TOKEN_KEY));
            saveToken(request);
        }
        if (tokenValid) {
            forward = mapping.findForward("success");
        } else {
            forward = mapping.findForward("failure");
        }
        logger
                .debug("execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - end");
        return forward;

    }
}
