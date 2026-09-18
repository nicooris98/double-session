<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.List, java.util.Map" %>
<%
    Map usuario = (Map) session.getAttribute("usuario");
    List<String[]> facturas = (List<String[]>) request.getAttribute("facturas");
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Facturación - Sistema Legacy</title>
    <link rel="stylesheet" href="<%= ctx %>/css/legacy.css">
</head>
<body>
<div class="header">
    <span>Sistema Corporativo <span class="tag">LEGACY · Struts 1.2</span></span>
    <span><%= usuario.get("nombre") %> · <a href="<%= ctx %>/logout.do">Cerrar sesión</a></span>
</div>
<div class="box">
    <h2>Facturación (módulo legacy)</h2>
    <p>Hola <b><%= usuario.get("nombre") %></b>, esta página la renderiza Struts con un JSP.</p>
    <table>
        <tr><th>N°</th><th>Cliente</th><th>Monto</th></tr>
        <% for (String[] f : facturas) { %>
            <tr><td><%= f[0] %></td><td><%= f[1] %></td><td><%= f[2] %></td></tr>
        <% } %>
    </table>
    <a class="btn" href="<%= ctx %>/menu.do">Volver al menú</a>
</div>
</body>
</html>
