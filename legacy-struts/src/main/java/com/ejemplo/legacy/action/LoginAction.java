package com.ejemplo.legacy.action;

import com.ejemplo.legacy.form.LoginForm;
import com.ejemplo.legacy.session.RedisSessionStore;
import com.ejemplo.legacy.session.SharedSessionCookie;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginAction extends Action {

    /** Usuarios de ejemplo (en el sistema real vendría de la BD / LDAP). */
    private static final Map<String, String[]> USUARIOS = new HashMap<String, String[]>();

    static {
        // username -> {password, userId, nombre, roles}
        USUARIOS.put("admin", new String[]{"admin", "1", "Ana Administradora", "ADMIN,USER"});
        USUARIOS.put("user", new String[]{"user", "2", "Uriel Usuario", "USER"});
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return mapping.findForward("form");
        }

        LoginForm loginForm = (LoginForm) form;
        String[] usuario = USUARIOS.get(loginForm.getUsername());
        if (usuario == null || !usuario[0].equals(loginForm.getPassword())) {
            request.setAttribute("error", "Usuario o contraseña incorrectos");
            return mapping.findForward("form");
        }

        // Si había una sesión previa se descarta (evita session fixation)
        String anterior = SharedSessionCookie.leer(request);
        if (anterior != null) {
            RedisSessionStore.eliminar(anterior);
        }
        HttpSession local = request.getSession(false);
        if (local != null) {
            local.invalidate();
        }

        Map<String, Object> datos = new HashMap<String, Object>();
        datos.put("userId", Integer.valueOf(usuario[1]));
        datos.put("username", loginForm.getUsername());
        datos.put("nombre", usuario[2]);
        datos.put("roles", Arrays.asList(usuario[3].split(",")));

        String sessionId = RedisSessionStore.crear(datos);
        SharedSessionCookie.escribir(response, sessionId);

        return mapping.findForward("menu");
    }
}
