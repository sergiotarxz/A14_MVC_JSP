<%@ page pageEncoding="utf-8" %>
<%@ page import="model.Venta" %>
<%@ page import="model.Producto" %>
<html>
    <head>
        <meta charset="utf-8"/>
    </head>
    <body>
        <form>
<%
    String codProd = request.getParameter("codProd");
    Venta venta = (Venta) request.getAttribute("venta");
    Integer idVenta = venta.getId();
    Producto producto = (Producto) request.getAttribute("producto");
%>
            <input name="action" value="addProductoLista" hidden>
            <input name="codProd" value="<%=codProd%>" hidden>
            <select name="cantidad">
<%
    for (int i = 1; i <= producto.getStock(); i++) {
%>
                <option value="<%=i%>"><%=i%></option>
<%
    }
%>
            </select>
            <input type="submit" value="Submit"/>
        </form>
    </body>
</html>
