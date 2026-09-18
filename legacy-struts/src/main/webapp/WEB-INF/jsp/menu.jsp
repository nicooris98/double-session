<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.Map" %>
<%
    Map usuario = (Map) session.getAttribute("usuario");
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Menú - Sistema Legacy</title>
    <link rel="stylesheet" href="<%= ctx %>/css/legacy.css">
</head>
<body>
<div class="header">
    <span>Sistema Corporativo <span class="tag">LEGACY · Struts 1.2</span></span>
    <span><%= usuario.get("nombre") %> · <a href="<%= ctx %>/logout.do">Cerrar sesión</a></span>
</div>
<div class="box">
    <h2>Menú principal</h2>
    <div class="menu">
        <a href="<%= ctx %>/modulo.do">
            Facturación
            <small>Módulo que sigue en el sistema legacy (Java / Struts)</small>
        </a>
        <a href="/nuevo/">
            Clientes
            <small>Módulo migrado (Angular + NestJS) — usa la misma sesión</small>
        </a>
    </div>
    <p class="muted">
        Sesión compartida: <b>shared-session:<%= session.getAttribute("sharedSessionId") %></b><br>
        Roles: <%= usuario.get("roles") %> · TTL en Redis: <%= request.getAttribute("ttl") %> s
    </p>
</div>
</body>
</html>
