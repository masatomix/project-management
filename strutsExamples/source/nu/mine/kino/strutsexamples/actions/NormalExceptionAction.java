package nu.mine.kino.strutsexamples.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nu.mine.kino.strutsexamples.exceptions.NormalException;

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
public class NormalExceptionAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionMessages errors = new ActionMessages();
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("エラー1",
                false));
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("エラー2",
                false));
        // errors.add(ActionMessages.GLOBAL_MESSAGE, new
        // ActionMessage("normal.exp"));
        saveErrors(request, errors);
         throw new NormalException("普通のハンドリングをする例外！");
//        return mapping.findForward("success");
    }
}
