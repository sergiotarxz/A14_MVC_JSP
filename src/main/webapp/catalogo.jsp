<%@ page pageEncoding="utf-8" %>
<%@ page import="model.Producto" %>
<%@ page import="java.util.ArrayList" %>
<html>
    <head>
        <title>Catálogo</title>
    </head>
    <body>
        <a href="<%=request.getAttribute("javax.servlet.forward.request_uri")%>">Volver</a>
<% 
    if (request.getAttribute("productos") != null) {
%>
        <table>
            <tr>
                <th/>
                <th>Código de producto</th>
                <th>Nombre de producto</th>
                <th>Sección</th>
                <th>Precio</th>
                <th>Fecha</th>
                <th>Importado</th>
                <th>País</th>
                <th>Stock</th>
            </tr>
<%
        for (Producto producto : (ArrayList<Producto>) request.getAttribute("productos")) {
            String comprar = request.getAttribute("javax.servlet.forward.request_uri") + "?action=addProductoView&codProd=" + producto.getCodProd();
%>
            <tr>
                <td>
                    <a href="<%=comprar%>">Comprar</a>
                </td>
                <td><%=producto.getCodProd()%></td>
                <td><%=producto.getNombreProd()%></td>
                <td><%=producto.getSeccion()%></td>
                <td><%=producto.getPrecio()%></td>
                <td><%=producto.getFecha()%></td>
                <td><%=producto.getImportado() ? "Sí" : "No"%></td>
                <td><%=producto.getPais()%></td>
                <td><%=producto.getStock()%></td>
            <tr>
<%
        }
%>
        </table>
<%
    }
%>
    </body>
</html>
