package nu.mine.kino.strutsexamples.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nu.mine.kino.strutsexamples.exceptions.NormalException;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @version 1.0
 * @author
 */
public class ExceptionAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        throw new NormalException("普通のハンドリングをする例外！");
    }
}
