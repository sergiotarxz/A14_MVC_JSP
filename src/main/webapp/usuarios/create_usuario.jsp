<%@ page import="java.util.ArrayList" %>
<%@ page pageEncoding="utf-8" %>
<html>
    <head>
        <title>Usuarios</title>
        <meta charset="utf-8"/>
        <style>
            body {
                grid-template-columns: 50%;
                display: grid;
            }

            form {
                border-style: inset;
                border-color: black;
                border-width: 5px;
                border-radius: 6%;
                background: white;
                margin-left: 3%;
                margin-right: 3%;
                margin-top: 3%;
                margin-bottom: 3%;

            }

            .add input[type="text"],input[type="date"],input[type="number"],h2,div {
                width: 80%;
                height: auto;
                margin: auto;
                margin-bottom: 1%;
                margin-top: 1%;
                display: block;
            }

            .add input[type="submit"] {
                display: block;
                margin: 0 auto;
                margin-bottom: 3%;
                margin-top: 3%;
            }

            .add {
                grid-column: 1/1;
                grid-row: 1/2;
            }

            .mostrar {
                grid-column: 2/2;
                grid-row: 1;
            }

            .error {
                background: red;
                color: white;
            }
        </style>
    </head>
    <body>
        <form class="add">
            <h2>Añadir usuario</h2>
            <input name="action" value="registrar" hidden/>
            <input name="nombre" type="text" placeholder="Nombre" autofocus/>
            <input name="apellidos" type="text" placeholder="Apellidos"/>
            <input name="usuario" type="text" placeholder="Usuario"/>
            <input name="contrasena" type="text" placeholder="contrasena"/>
            <input name="pais" type="text" placeholder="pais"/>
            <input name="tecnologia" type="text" placeholder="tecnología"/>
            <input type="submit" value="Enviar"/>
        </form>
<%
    if (request.getAttribute("errores") != null) {
%>
        <div>
<%
        for (String error : (ArrayList<String>) request.getAttribute("errores")) {
%>
            <p class="error"><%=error%></p>
<%
        }
%>
        </div>
<%
    }
%>
    </body>
</html>
