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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

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

        ActionMessages errors = new ActionMessages();

        HttpSession session = request.getSession();
        boolean tokenValid = false;
        synchronized (session) {
            tokenValid = isTokenValid(request);
            logger.debug(tokenValid + ":"
                    + session.getAttribute(Globals.TRANSACTION_TOKEN_KEY));
            saveToken(request);
        }
        if (!tokenValid) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("tokenチェックエラー<br />");
            buffer.append("hidden: " + request.getParameter(Globals.TOKEN_KEY)
                    + "<br />");
            buffer.append("session: "
                    + session.getAttribute(Globals.TRANSACTION_TOKEN_KEY));
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    new String(buffer), false));
            saveErrors(request, errors);
        }
        logger
                .debug("execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - end");
        return mapping.findForward("success");

    }
}
