<%@ page import="model.Producto" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<html>
    <head>
        <Title>Actualizar producto</title>
    </head>
    <body>
        <form action="productos">
            <%
            Producto producto = (Producto) request.getAttribute("producto");
            out.print("<input name=\"action\" value=\"updateProducto\" hidden/>");
            out.print("<br/>");
            out.print("<input name=\"codProd\" value=\"" + producto.getCodProd() + "\" hidden/>");
            out.print("<br/>");
            out.print("<input name=\"seccion\" value=\"" + producto.getSeccion() + "\" type=\"text\"/>");
            out.print("<br/>");
            out.print("<input name=\"nombreProd\" value=\"" + producto.getNombreProd() + "\" type=\"text\"/>");
            out.print("<br/>");
            out.print("<input name=\"precio\" value=\"" + producto.getPrecio() + "\" type=\"text\"/>");
            out.print("<br/>");
            out.print("<input name=\"fecha\" value=\"" + producto.getFecha().format(DateTimeFormatter.ISO_LOCAL_DATE) + "\" type=\"date\"/>");
            out.print("<br/>");
            out.print("<input type=\"radio\" value=\"true\" name=\"importado\" " + (producto.getImportado() ? "checked" : "") + "/>");
            out.print("<label>Importado</label>");
            out.print("<br/>");
            out.print("<input type=\"radio\" value=\"false\" name=\"importado\" " + (!producto.getImportado() ? "checked" : "") + "/>");
            out.print("<label>Nacional</label>");
            out.print("<br/>");
            out.print("<input name=\"pais\" value=\"" + producto.getPais() + "\" type=\"text\"/>");
            out.print("<input name=\"stock\" value=\"" + producto.getStock() + "\" type=\"text\"/>");
            out.print("<br/>");
            out.print("<input type=\"Submit\" value=\"submit\"/>");
            %>
        </form>
    </body>
</html
