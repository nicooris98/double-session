package com.ejemplo.legacy.action;

import com.ejemplo.legacy.session.RedisSessionStore;
import com.ejemplo.legacy.session.SharedSessionFilter;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MenuAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
        String sessionId = (String) request.getSession().getAttribute(SharedSessionFilter.SESSION_ID_ATTR);
        request.setAttribute("ttl", RedisSessionStore.ttl(sessionId));
        return mapping.findForward("ok");
    }
}
