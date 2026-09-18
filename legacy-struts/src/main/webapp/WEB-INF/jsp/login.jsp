<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - Sistema Legacy</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/legacy.css">
</head>
<body>
<div class="header"><span>Sistema Corporativo <span class="tag">LEGACY · Struts 1.2</span></span></div>
<div class="box">
    <h2>Iniciar sesión</h2>
    <% if (request.getAttribute("error") != null) { %>
        <p class="error"><%= request.getAttribute("error") %></p>
    <% } else if (request.getParameter("logout") != null) { %>
        <p class="info">Sesión cerrada en ambos sistemas.</p>
    <% } else if (request.getParameter("expirada") != null) { %>
        <p class="error">Tu sesión no existe o expiró. Vuelve a ingresar.</p>
    <% } %>
    <form method="post" action="<%= request.getContextPath() %>/login.do">
        <label>Usuario <input type="text" name="username" autofocus></label>
        <label>Contraseña <input type="password" name="password"></label>
        <button type="submit">Ingresar</button>
    </form>
    <p class="muted">Usuarios de prueba: admin / admin &nbsp;·&nbsp; user / user</p>
</div>
</body>
</html>
