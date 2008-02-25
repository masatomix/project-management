package nu.mine.kino.stresstest;

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

import struts.annotation.Forward;
import struts.annotation.StrutsAction;

/**
 * @version 1.0
 * @author
 * 
 */
// @StrutsAction(input = "/WEB-INF/jsp/login.jsp", forwards = { @Forward(name =
// "fail", path = "/WEB-INF/jsp/login.jsp"), })
// @StrutsAction(forwards = { @Forward(name = "fail", path =
// "/WEB-INF/jsp/login.jsp") })
public class LogoutAction extends Action {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(LogoutAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        logger
                .debug("execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - start");

        ActionMessages errors = new ActionMessages();
        HttpSession session = request.getSession();
        synchronized (session) {
            boolean tokenValid = isTokenValid(request);
            String sessionToken = (String) session
                    .getAttribute(Globals.TRANSACTION_TOKEN_KEY);
            logger.debug("tokenValid? " + tokenValid + ":" + sessionToken);
            if (!tokenValid) {
                String requestToken = request.getParameter(Globals.TOKEN_KEY);
                String errorMessage = "tokenチェックエラー。requestのトークン: "
                        + requestToken + " sessionのトークン： " + sessionToken;
                throw new TokenInvalidException(errorMessage);
            }
            saveToken(request);
        }
        return mapping.findForward("success");
    }
}
