<%@ page pageEncoding="utf-8" %>
<html>
    <head> 
        <meta charset="utf-8"/>
    </head> 
    <body>
        <ul>
            <h1>Menu principal</h1>
            <li>
                <a href="<%=request.getAttribute("javax.servlet.forward.request_uri") + "?action=misDatos"%>">Mis datos</a>
            </li>
            <li>
                <a href="<%=request.getAttribute("javax.servlet.forward.request_uri") + "?action=listaUsuarios"%>">Listar usuarios</a>
            </li>
            <li>
                <a href="<%=request.getContextPath() + "/ListaCompra"%>">Lista de la compra</a>
            </li>            
            <li>
                <a href="<%=request.getContextPath() + "/productos"%>">Menu de productos (No requiere usuario)</a>
            </li>

            <li>
                <a href="<%=request.getAttribute("javax.servlet.forward.request_uri") + "?action=seleccionarFactura"%>">Ver mis facturas</a>
            </li>
            <li>
                <a href="<%=request.getContextPath() + "?action=logout"%>">Logout</a>
            </li>
        </ul>
    </body>
</html>
