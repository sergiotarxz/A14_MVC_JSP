<%@ page pageEncoding="utf-8" %>
<%@ page import="model.Venta" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>Lista de la compra</title>
    </head>
    <body>
        <h1>Lista de la compra</h1>
        <ul>
            <li>
                <a href="<%=request.getContextPath()%>">Volver</a>
            </li>
            <li>
                <a href="<%=request.getAttribute("javax.servlet.forward.request_uri") + "?action=mostrarCatalogo"%>">Catálogo</a>
            </li>
            <li>
                <a href="<%=request.getAttribute("javax.servlet.forward.request_uri") + "?action=confirmar"%>">Confirmar</a>
            </li>
        </ul>
        <h3>Lista actual</h3>
<%
    if (request.getAttribute("venta") != null) {
        Venta venta = (Venta) request.getAttribute("venta");
        if (request.getAttribute("detalles") != null) {
            List<HashMap<String, Object>> detalles = (ArrayList<HashMap<String, Object>>) request.getAttribute("detalles");
%>
        <table>
            <tr>
                <th>Id detalle</th>
                <th>Código producto</th>
                <th>Nombre</th>
                <th>Precio producto</th>
                <th>Cantidad</th>
                <th>Precio total</th>
            </tr>
<%
            Double precioTotal = new Double(0);
            for (HashMap<String, Object> hm : detalles) {
%>
            <tr>
                 <td><%=hm.get("id_detalle")%></td>
                 <td><%=hm.get("codProd")%></td> 
                 <td><%=hm.get("nombre")%></td> 
                 <td><%=hm.get("precio")%></td> 
                 <td><%=hm.get("cantidad")%></td> 
                 <td><%=hm.get("precio_total")%></td> 
                 <td>
                    <%
                        String urlBorrar = (String) request.getAttribute("javax.servlet.forward.request_uri") + "?action=seguroBorrar&id=" + hm.get("id_detalle");    
                    %>
                    <a href="<%=urlBorrar%>">Borrar</a>
                </td>
<%
                precioTotal += (Double) hm.get("precio_total");
%>
            </tr>
<%
            }
%>
           <tr>
                <td/>
                <td/>
                <td/>
                <td/>
                 <td><%=precioTotal%></td>
           </tr>
        </table>
<%
        }
%>
        <h3>Datos de la venta</h3>
        <table>        
            <tr>
                <th>Id Venta</th>
                <th>Id Usuario</th>
                <th>Fecha</th>
            </tr>
            <tr>
                  <td><%=venta.getId()%></td> 
                 <td><%=venta.getIdUsuario()%></td> 
                 <td><%=venta.getFecha()%></td> 
            </tr>
        </table>        
<%
    }
%>
    </body>
</html>
