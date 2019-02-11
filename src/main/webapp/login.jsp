<%@ page import="java.util.ArrayList" %>
<%@ page pageEncoding="utf-8" %>
<html>
    <head>
        <meta charset="utf-8"/>
    </head>
    <body>
        <a href="?action=registrarView">Registrate</a>
        <form>
            <input name="action" value="login" hidden/>
            <input name="user" placeholder="Nombre de usuario"/>
            <input name="pass" placeholder="Contraseña"/>
            <input type="submit" value="Submit"/>
            <ul>
<%
    if (request.getAttribute("errores") != null) {
        for (String error : (ArrayList<String>) request.getAttribute("errores")) {
%>
                <li style="background-color: red; color: white;"><%=error%></li>
<%
        }
    }
%>
            <ul>
        </form>
    </body>
</html>
