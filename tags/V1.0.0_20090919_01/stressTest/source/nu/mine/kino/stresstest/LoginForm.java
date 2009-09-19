package nu.mine.kino.stresstest;

import org.apache.struts.action.ActionForm;

import struts.annotation.AccessType;
import struts.annotation.StrutsForm;

/**
 * @author Masatomi KINO
 * @version $Revision$
 */
@StrutsForm(access = AccessType.FIELD)
public class LoginForm extends ActionForm {
    String userid;
    String password;
}
