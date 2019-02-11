<%@ page pageEncoding="utf-8" %>
<%@ page import="model.Usuario" %>
<%@ page import="java.util.ArrayList" %>
<html>
    <head>
    </head>
    <body>
        <a href="<%=request.getAttribute("javax.servlet.forward.request_uri")%>">Volver</a>
<%
    if (request.getAttribute("usuarios") != null) {
%>
        <table>
            <tr>
                <th>Nombre</th>
                <th>Apellidos</th>
                <th>Nombre de usuario</th>
                <th>Contraseña</th>
                <th>País</th>
                <th>Tecnología</th>
            </tr>
<%
            for (Usuario usuario : (ArrayList<Usuario>) request.getAttribute("usuarios")) {
%>
            <tr>
                <td><%=usuario.getNombre()%></td>
                <td><%=usuario.getApellidos()%></td>
                <td><%=usuario.getUsuario()%></td>
                <td><%=usuario.getContrasena()%></td>
                <td><%=usuario.getPais()%></td>
                <td><%=usuario.getTecnologia()%></td>
            </tr>
<%
            }
%>
        </table>
<%
    }
%>
    </body>
</html>
