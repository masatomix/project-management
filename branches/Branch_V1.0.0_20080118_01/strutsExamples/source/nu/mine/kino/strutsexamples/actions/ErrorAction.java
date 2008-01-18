package nu.mine.kino.strutsexamples.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class ErrorAction extends Action {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(ErrorAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // http://www.javaroad.jp/opensource/js_struts17.htm
        // http://civic.xrea.jp/2006/09/04/struts-message-2/
        ActionForward forward = new ActionForward(); // return value
        ActionMessages messages = new ActionMessages();

        // まずは、propertiesからでなく、ココで指定した文字列をそのまま画面表示するパタン。
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("メッセージ０",
                false));// リソースフラグをfalseにする。 指定しない場合デフォルトはtrue

        // 次はpropertiesから文字列を取得するパタン。"msg.message1"というキー値でpropertiesを検索し、
        // 画面表示。"メッセージ１"は可変文字列で、properties内の
        // msg.message1={0} <-ココが置換される。
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                "msg.message1", "メッセージ１"));

        // 可変パラメタは配列もＯＫ。そのばあい
        // msg.message2= {0},{1} などと複数プレースホルダをかける。
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                "msg.message2", new String[] { "メッセージ２の１", "メッセージ２の２" }));

        // ActionMessages.GLOBAL_MESSAGEというのはグループのIDになっていて、独自のIDを渡すこともできる。
        // Messageを表示するJSP側で、タグの指定にグループIDを指定すれば、そのチラのIDのメッセージを表示可能。
        // 詳細はJSP側で。
        messages.add("HogeGroup", new ActionMessage("別グループのメッセージ", false));

        // saveMessageすることで、
        // request.setAttribute(Globals.ERROR_KEY, errors);
        // という処理が行われる。
        saveErrors(request, messages);
        return mapping.findForward("success");

    }
}
