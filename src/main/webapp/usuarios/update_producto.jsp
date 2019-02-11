<%@ page import="model.Usuario" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<html>
    <head>
        <Title>Actualizar usuario</title>
    </head>
    <body>
        <form>
            <%
            Usuario usuario = (Producto) request.getAttribute("usuario");
            out.print("<input name=\"action\" value=\"updateUsuario\" hidden/>");
            out.print("<br/>");
            out.print("<input name=\"id\" value=\"" + usuario.getId() + "\" hidden/>");
            out.print("<input name=\"nombre\" value=\"" + usuario.getNombre() + "\" type=\"text\"/>");
            out.print("<br/>");
            out.print("<input name=\"apellidos\" value=\"" + usuario.getApellidos() + "\" type=\"text\"/>");
            out.print("<br/>");
            out.print("<input name=\"usuario\" value=\"" + usuario.getUsuario() + "\" type=\"text\"/>");
            out.print("<br/>");
            out.print("<input name=\"contrasena\" value=\"" + usuario.getContrasena() + "\" type=\"text\"/>");
            out.print("<br/>");
            out.print("<input name=\"pais\" value=\"" + usuario.getPais() + "\" type=\"text\"/>");
            out.print("<br/>");
            out.print("<input type=\"text\" value=\"" + usuario.getTecnologia() + "\" name=\"tecnologia\">"); 
            out.print("<input type=\"Submit\" value=\"submit\"/>");
            %>
        </form>
    </body>
</html
