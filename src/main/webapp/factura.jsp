<%@ page pageEncoding="utf-8" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<html>
    <head>
        <meta charset="utf-8"/>
    </head>
    <body>
        <a href="<%=request.getAttribute("javax.servlet.forward.request_uri") + "?action=seleccionarFactura"%>">Volver</a>
<%
if (request.getAttribute("detalles") != null) {
            ArrayList<HashMap<String, Object>> detalles = (ArrayList<HashMap<String, Object>>) request.getAttribute("detalles");
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
    </body>
</html>
