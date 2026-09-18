package com.ejemplo.legacy.action;

import com.ejemplo.legacy.session.RedisSessionStore;
import com.ejemplo.legacy.session.SharedSessionCookie;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/** Cierra la sesión en AMBOS sistemas: al borrar la clave de Redis, Nest también la pierde. */
public class LogoutAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
        String sessionId = SharedSessionCookie.leer(request);
        if (sessionId != null) {
            RedisSessionStore.eliminar(sessionId);
        }
        HttpSession local = request.getSession(false);
        if (local != null) {
            local.invalidate();
        }
        SharedSessionCookie.borrar(response);
        return mapping.findForward("login");
    }
}
