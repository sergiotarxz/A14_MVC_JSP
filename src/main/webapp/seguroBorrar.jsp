<%@ page pageEncoding="utf-8" %>
<%@ page import="model.Detalle" %>
<%@ page import="model.Producto" %>
<html>
    <head>
        <meta charset="utf-8"/>
    </head>
    <body>
<%
    Producto producto = (Producto) request.getAttribute("producto");
    Detalle detalle = (Detalle) request.getAttribute("detalle");
    if (detalle != null && producto != null) {
%>
        <p>¿Seguro que desea cancelar la compra de <%=detalle.getCantidad()%> unidades del producto <%=producto.getNombreProd()%> con codigo <%=producto.getCodProd()%>?</p>
        <br/>
<%
        String urlContinuar = request.getAttribute("javax.servlet.forward.request_uri") + "?action=borrar&id=" + detalle.getId();
%>
        <a href="<%=urlContinuar%>">Continuar</a>
        
<%
    }
    String urlAtras = (String) request.getAttribute("javax.servlet.forward.request_uri");
%>
        <a href="<%=urlAtras%>">Volver</a>
    </body>
</html>
