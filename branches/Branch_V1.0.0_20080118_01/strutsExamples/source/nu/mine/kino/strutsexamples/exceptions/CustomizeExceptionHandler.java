/*******************************************************************************
 * Copyright (c) 2007 Masatomi KINO. All rights reserved. $Id$
 ******************************************************************************/
// 作成日: 2008/01/16
package nu.mine.kino.strutsexamples.exceptions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

public class CustomizeExceptionHandler extends ExceptionHandler {
    public ActionForward execute(Exception exception, ExceptionConfig config,
            ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws ServletException {

        NormalException normalException = (NormalException) exception;
        System.out.println("hogehoggehoge");
        // エラーコード見るとか、なんか処理。
        ActionForward forward = super.execute(exception, config, mapping, form,
                request, response);
        // ActionForward forward = null;
        // ActionMessage error = null;
        // String property = null;

        // if (config.getPath() != null) {
        // forward = new ActionForward(config.getPath());
        // } else {
        // forward = mapping.getInputForward();
        // }

        // error = new ActionMessage("errors.ApplicationException", exception
        // .getMessage());
        // property = error.getKey();

        // request.setAttribute(Globals.EXCEPTION_KEY, exception);
        // this.storeException(request, property, error, forward, config
        // .getScope());

        return mapping.findForward("success");
        //        return forward;
    }
}
