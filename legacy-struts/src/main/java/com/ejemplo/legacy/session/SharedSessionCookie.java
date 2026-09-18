package com.ejemplo.legacy.session;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie SHARED_SESSION con Path=/ para que el navegador la envíe tanto a
 * /legacy/* como a /nuevo/* y /api/* (mismo dominio gracias al gateway).
 *
 * Se escribe el header a mano porque javax.servlet.http.Cookie (Servlet 4)
 * no soporta el atributo SameSite.
 */
public final class SharedSessionCookie {

    public static final String NAME = "SHARED_SESSION";

    private SharedSessionCookie() {
    }

    public static String leer(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public static void escribir(HttpServletResponse response, String sessionId) {
        // En producción agregar "; Secure" (solo HTTPS)
        response.addHeader("Set-Cookie", NAME + "=" + sessionId + "; Path=/; HttpOnly; SameSite=Lax");
    }

    public static void borrar(HttpServletResponse response) {
        response.addHeader("Set-Cookie", NAME + "=; Path=/; Max-Age=0; HttpOnly; SameSite=Lax");
    }
}
