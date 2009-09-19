package nu.mine.kino.stresstest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
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
@StrutsAction(forwards = { @Forward(name = "fail", path = "/WEB-INF/jsp/index.jsp") })
public class LoginAction extends Action {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(LoginAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        logger
                .debug("execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - start");

        saveToken(request);
        LoginForm lform = (LoginForm) form;
        ActionMessages errors = new ActionMessages();
        String userid = lform.userid;
        String password = lform.password;

        if (userid == null || !"hoge".equals(userid)) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "errors.msg", "ユーザID"));
        }
        if (password == null || !"fuga".equals(password)) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "errors.msg", "パスワード"));
        }

        login(userid, password);

        saveErrors(request, errors);
        if (!errors.isEmpty()) {
            logger
                    .debug("execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - end");
            return mapping.findForward("fail");
        }
        logger
                .debug("execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - end");
        return mapping.findForward("success");

    }

    /**
     * @param userid
     * @param password
     */
    private void login(String userid, String password) {
        long time = Math.round((Math.random() * 2000));
        logger.debug("待ち時間 " + time);
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
