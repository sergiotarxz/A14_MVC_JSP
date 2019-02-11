<%@ page pageEncoding="utf-8" %>
<%@ page import="java.util.ArrayList" %>
<html>
    <head>
        <meta charset="utf-8"/>
        <style>
            .error {
                color: white;
                background: red;
            }
        </style>
    </head>
    <body>
<%
    if (request.getAttribute("errores") != null && request.getAttribute("urlBack") != null) {
        String urlBack = (String) request.getAttribute("urlBack");
%>
        <a href="<%=urlBack%>">Volver<a>
<%
        for (String error : (ArrayList<String>) request.getAttribute("errores")) { 
%>
        <p class="error"><%=error%></p>
<%
        }
    } else {
%>
        <p>Ha sucedido un error al mostrar esta página</p>
<%
    }
%>
    </body>
</html>
