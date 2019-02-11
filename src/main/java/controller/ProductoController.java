package controller;

import model.Producto;
import dao.ProductoDAO;
import util.DataSourceUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.naming.NamingException;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/productos")
public class ProductoController extends HttpServlet {

    ProductoDAO productoDAO;
    ArrayList<String> errores;
    Boolean criticalError;

    public ProductoController() {
        this.criticalError = false;
        this.errores = new ArrayList<String>();
        try {
            productoDAO = new ProductoDAO(new DataSourceUtils().getDataSource());
        } catch (NamingException ex) {
            ex.printStackTrace();
            this.criticalError = true;
        }
    }

    @Override
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) {
        if (this.criticalError) {
            this.forwardError(request, response);
        }

        String action = request.getParameter("action");
        this.errores = new ArrayList<String>();
        action = action == null ? "" : action;
        switch (action) {
            case "addProducto":
                try {
                    addProducto(request, response);
                } catch (NumberFormatException ex) {
                   ex.printStackTrace();
                   this.errores.add("El precio debe ser numérico");
                   this.forwardError(request, response);
                } catch (DateTimeParseException ex) {
                    ex.printStackTrace();
                    this.errores.add("La fecha no se encuentra en el formato esperado");
                    this.forwardError(request, response);
                }
                break;
            case "mostrarProductos":
                mostrarProductos(request, response);
                break;
            case "updateProducto":
                 try {
                    updateProducto(request, response);
                } catch (NumberFormatException ex) {
                   ex.printStackTrace();
                   this.errores.add("El precio debe ser numérico");
                   this.forwardError(request, response);
                } catch (DateTimeParseException ex) {
                    ex.printStackTrace();
                    this.errores.add("La fecha no se encuentra en el formato esperado");
                    this.forwardError(request, response);
                }
                break;
            case "updateView":
                updateView(request, response);
                break;
            case "deleteProducto":
                deleteProducto(request, response);
                break;
            default:
                try {
                    request.getRequestDispatcher("productos/vista_principal.jsp")
                        .forward(request, response);
                } catch (ServletException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
        }
    }

    private void updateView(HttpServletRequest request,
            HttpServletResponse response) {
        String codProd = request.getParameter("codProd");
        Boolean valido = true;
        if ( codProd == null ) {
            this.errores.add("El codigo de producto es nulo");
            valido = false;
        } else if ( codProd.isEmpty() ) {
            this.errores.add("El codigo de producto es invalido");
            valido = false;
        }

        if ( valido ) {
            try {
                Producto producto = null;
                try {
                    producto = productoDAO.read(codProd);
                } catch (SQLException ex) {
                    this.errores.add("Error de conexión");
                }
                if ( producto != null) {
                    request.setAttribute("producto", producto);
                    request.getRequestDispatcher("productos/update_producto.jsp")
                            .forward(request, response);
                } else {
                    this.errores.add("Fallo al buscar el producto"); 
                    forwardError(request, response);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ServletException ex) {
                ex.printStackTrace();
            }
        } else {
            forwardError(request, response);
        }
    }
    
    private void updateProducto(HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException {
        Boolean valido = true;
        String codProd = request.getParameter("codProd");

        if ( codProd == null ) {
            valido = false;
            this.errores.add("El codigo de producto es nulo");
        } else if (codProd.isEmpty()) {
            valido = false;
            this.errores.add("El codigo de producto está vacio");
            forwardError(request, response);
        }

        String seccion = request.getParameter("seccion");
        String nombreProd = request.getParameter("nombreProd");

        if ( nombreProd == null) {
            valido = false;
            this.errores.add("Los productos deben tener un nombre");
            forwardError(request, response);
        } else if (nombreProd.isEmpty()) {
            valido = false;
            this.errores.add("Nombre vacio");
            forwardError(request, response);
        }

        Double precio = Double.parseDouble(request.getParameter("precio"));

        if ( precio == null ) {
            valido = false;
            this.errores.add("Los productos deben tener un precio");
        } else if ( precio < 0 ) {
            valido = false;
            this.errores.add("No se admiten precios inferiores a 0");
        }
            
        String fecha = request.getParameter("fecha");

        if ( fecha == null ) {
            valido = false;
            this.errores.add("Se necesita una fecha");
        }

        LocalDate fechaLoc = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Boolean importado = request.getParameter("importado").equals("true");
        String pais = request.getParameter("pais");
        Integer stock = Integer.parseInt(request.getParameter("stock"));
        if (stock < 0) {
            valido = false;
            this.errores.add("No se permite stock negativo");
        }
        Producto producto = new Producto(codProd, seccion, nombreProd, precio, fechaLoc, importado, pais, stock);
        if (valido) {
            if ( !this.productoDAO.update(producto) ) {
                valido = false;
                this.errores.add("El producto no se pudo crear, es posible que el código ya exista o el servidor de base de datos esté caido");
            } else {
                mostrarProductos(request, response);
            }
        } else {
            forwardError(request, response);
        }

    }

    private void addProducto(HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException {
        Boolean valido = true;
        String codProd = request.getParameter("codProd");

        if ( codProd == null ) {
            valido = false;
            this.errores.add("El codigo de producto es nulo");
        } else if (codProd.isEmpty()) {
            valido = false;
            this.errores.add("El codigo de producto está vacio");
        }

        String seccion = request.getParameter("seccion");
        String nombreProd = request.getParameter("nombreProd");

        if ( nombreProd == null) {
            valido = false;
            this.errores.add("Los productos deben tener un nombre");
        } else if (nombreProd.isEmpty()) {
            valido = false;
            this.errores.add("Nombre vacio");
        }

        Double precio = Double.parseDouble(request.getParameter("precio"));

        if ( precio == null ) {
            valido = false;
            this.errores.add("Los productos deben tener un precio");
        } else if ( precio < 0 ) {
            valido = false;
            this.errores.add("No se admiten precios inferiores a 0");
        }
            
        String fecha = request.getParameter("fecha");

        if ( fecha == null ) {
            valido = false;
            this.errores.add("Se necesita una fecha");
        }

        LocalDate fechaLoc = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Integer stock = null;

        if (Pattern.matches("[0-9]{1,}", request.getParameter("stock"))) {
            try {
                stock = Integer.parseInt(request.getParameter("stock"));
            } catch (NumberFormatException ex) {
                this.errores.add("Has desbordado el tipo entero");
                valido = false;
            }
        } else {
            this.errores.add("El stock no es numerico");
            valido = false;
        }

        if (stock == null) {
            this.errores.add("El stock no se pudo transformar a entero");
            valido = false;
        } else if (stock < 0) {
            valido = false;
            this.errores.add("No se permite stock negativo");
        } 

        Boolean importado = request.getParameter("importado").equals("true");
        String pais = request.getParameter("pais");
        Producto producto = new Producto(codProd, seccion, nombreProd, precio, fechaLoc, importado, pais, stock);

        

        if (valido) {
            try {
                if ( !this.productoDAO.create(producto) ) {
                    this.errores.add("El producto no se pudo crear, es posible que el código ya exista o el servidor de base de datos esté caido");
                    valido = false;
                    forwardError(request, response);
                } else {
                    mostrarProductos(request, response);
                }
            } catch (NullPointerException ex) {
                errores.add("Has iniciado el servidor de mysql/mariadb?");
                forwardError(request, response);
            }
        } else {
            forwardError(request, response);
        }
    }

    private void mostrarProductos(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            request.setAttribute("productos", this.productoDAO.readAll()); 
            request.getRequestDispatcher("productos/mostrar_productos.jsp")
                .forward(request, response);
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.errores.add("No se pudo leer de la base de datos");
            this.forwardError(request, response);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ServletException ex) {
            ex.printStackTrace();
        }
    }


    private void deleteProducto(HttpServletRequest request,
            HttpServletResponse response) {
        if ( !this.productoDAO.delete(request.getParameter("codProd")) ) {
            mostrarProductos(request, response);
        } else {
            this.errores.add("Este producto no existe.");
            forwardError(request, response);
        }

    }
    
    private void forwardError(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            request.setAttribute("errores", this.errores);
            request.getRequestDispatcher("./errores.jsp")
                    .forward(request, response);
        } catch (ServletException ex) {
            /* 
             * Si ha fallado al hacer forward a errores
             * fallará cuando lo vuelva a tratar de hacer
             * aquí , por eso esto no se controla de forma
             * transparente al usuario.
            */
            ex.printStackTrace();
        } catch (IOException ex) {
            /* 
             * Si ha fallado al hacer forward a errores
             * fallará cuando lo vuelva a tratar de hacer
             * aquí, por eso esto no se controla de forma
             * transparente al usuario.
             */
            ex.printStackTrace();
        }
    }

}
