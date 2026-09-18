package com.ejemplo.legacy.session;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * Único punto donde el legacy valida la sesión.
 *
 * La fuente de verdad es Redis: si la clave no existe (expiró o se hizo
 * logout desde Angular/Nest), la HttpSession local de Tomcat se invalida
 * y se redirige al login. Si existe, se renueva el TTL y se deja el usuario
 * en la HttpSession para que el resto del legacy siga funcionando igual que hoy.
 */
public class SharedSessionFilter implements Filter {

    public static final String USUARIO_ATTR = "usuario";
    public static final String SESSION_ID_ATTR = "sharedSessionId";

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if ("/login.do".equals(request.getServletPath())) {
            chain.doFilter(request, response);
            return;
        }

        String sessionId = SharedSessionCookie.leer(request);
        Map<String, Object> usuario = sessionId == null ? null : RedisSessionStore.obtenerYRenovar(sessionId);

        if (usuario == null) {
            HttpSession local = request.getSession(false);
            if (local != null) {
                local.invalidate();
            }
            SharedSessionCookie.borrar(response);
            response.sendRedirect(request.getContextPath() + "/login.do?expirada=1");
            return;
        }

        HttpSession local = request.getSession(true);
        local.setAttribute(USUARIO_ATTR, usuario);
        local.setAttribute(SESSION_ID_ATTR, sessionId);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
