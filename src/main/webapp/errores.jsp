<%@ page import="java.util.ArrayList" %>
<html>
    <head>
        <title>ATENCIÓN</title>
        <meta charset="utf-8"/>
        <link rel="stylesheet" href="./css/errores.css"/>
    </head>
    <body>
        <%
            for ( String error : (ArrayList<String>) request.getAttribute("errores") ) {
                out.print("<p class=\"error\">" + error + "</p>");
            }
        %>
    </body>
</html>
