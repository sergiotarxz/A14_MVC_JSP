<%@ page pageEncoding="utf-8" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="model.Usuario" %>
<%@ page import="java.util.ArrayList" %>
<html>
    <head>
        <title>Mostrar Usuarios</title>
    </head>
    <body>
<%
ArrayList<Usuario> usuarios = null;
if (request.getAttribute("usuarios") != null) {
%>
        <a href="${request.getRequestURI()}?action=addView">Añadir usuario</a>
<%
}
try {
    usuarios = (ArrayList<Usuario>) request.getAttribute("usuarios");
    if (usuarios.size() == 0 ) {
%>
        <p>No hay usuarios</p>
<%
    } else {
%>
        <table>
            <tr>
                <th/>
                <th/>
                <th>Nombre</th>
                <th>Apellidos</th>
                <th>Usuario</th>
                <th>Contraseña</th>
                <th>País</th>
                <th>Tecnología</th>
            </tr>
<%
        for (Usuario usuario : usuarios) {
            %>
            <tr>
                <td>
                    <a href="${request.getRequestURI()}?action=updateView&id=<%=usuario.getId()%>">Update</a>
                </td>
                <td>
                    <a href="${request.getRequestURI()}?action=deleteUsuario&id=<%=usuario.getId()%>">Delete</a>
                </td>
                <td><%=usuario.getNombre()%></td>
                <td><%=usuario.getApellidos()%></td>
                <td><%=usuario.getUsuario()%></td>
                <td><%=usuario.getContrasena()%></td>
                <td><%=usuario.getPais()%></td>
                <td><%=usuario.getTecnologia()%></td>
            <tr/>
<%
        }
%>
        </table>
<%
    }
} catch (NullPointerException ex) {
    %>
        <p>Error del servidor</p>
    <%
}

%>
    </body>
</html>
