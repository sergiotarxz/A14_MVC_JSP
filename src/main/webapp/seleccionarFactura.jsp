<%@ page pageEncoding="utf-8" %>
<%@ page import="model.Venta" %>
<%@ page import="java.util.ArrayList" %>
<html>
    <head>
        <meta charset="utf-8"/>
    </head>
    <body>
        <a href="<%=request.getContextPath()%>">Volver</a>
<%
            ArrayList<Venta> ventas = (ArrayList<Venta>) request.getAttribute("ventas");
            if (ventas != null) {
                for (Venta venta : ventas) { 
                    String url = (String) request.getAttribute("javax.servlet.forward.request_uri") + "?action=factura&id=" + venta.getId();
%>
        <br/>
        <a href="<%=url%>"><%=venta.getFecha()%></a>
<%
                }
            } else {
%>
        <p>Ventas es nulo.</p>
<%
            }
%>
    </body>
</html>
