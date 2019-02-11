<%@ page pageEncoding="utf-8" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="model.Producto" %>
<%@ page import="java.util.ArrayList" %>
<html>
    <head>
        <title>Mostrar Productos</title>
    </head>
    <body>
        <table>
            <tr>
                <th/>
                <th>Código producto</th>
                <th>Sección</th>
                <th>Nombre del producto</th>
                <th>Precio</th>
                <th>Fecha</th>
                <th>Importado</th>
                <th>País</th>
            </tr>
            <%
                ArrayList<Producto> productos = (ArrayList<Producto>) request.getAttribute("productos");
                for (Producto producto : productos) {
                    out.print("<tr>");
                    out.print("<td><a href=\"./?action=updateView&codProd=" + producto.getCodProd() + "\">Update</a></td>");
                    out.print("<td>" + producto.getCodProd() + "</td>");
                    out.print("<td>" + producto.getSeccion() + "</td>");
                    out.print("<td>" + producto.getNombreProd() + "</td>");
                    out.print("<td>" + producto.getPrecio() + "</td>");
                    out.print("<td>" + producto.getFecha().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) + "</td>");
                    out.print("<td>" + producto.getImportado() + "</td>");
                    out.print("<td>" + producto.getPais() + "</td>");
                    out.print("</tr>");
                }
            %>
        </table>
    </body>
</html>
