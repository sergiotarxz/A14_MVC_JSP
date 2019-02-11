package controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;;
import javax.naming.NamingException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import dao.ProductoDAO;
import dao.VentaDAO;
import dao.DetalleDAO;
import dao.UsuarioDAO;

import model.Detalle;
import model.Venta;
import model.Producto;
import model.Usuario;

import util.DataSourceUtils;

@WebServlet("/ListaCompra")
public class ListaCompra extends HttpServlet {
    VentaDAO ventaDAO;
    DetalleDAO detalleDAO;
    UsuarioDAO usuarioDAO;
    ProductoDAO productoDAO;
    ArrayList<String> errores;
    ArrayList<String> note;
    public ListaCompra() {
        note = new ArrayList<String>();
        try {
            detalleDAO = new DetalleDAO(new DataSourceUtils().getDataSource());
            ventaDAO = new VentaDAO(new DataSourceUtils().getDataSource());
            usuarioDAO = new UsuarioDAO(new DataSourceUtils().getDataSource());
            productoDAO = new ProductoDAO(new DataSourceUtils().getDataSource());
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) {
        this.errores = new ArrayList<String>();
        if (request.getSession().getAttribute("usuario") == null) {
            try {
                request.getRequestDispatcher("logueateAntes.jsp")
                        .forward(request, response);
            } catch (ServletException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                Integer idVenta = null;
                Venta venta = null;
                Usuario usuario = null;
                Integer idUsuario = null;

                usuario = usuarioDAO.readByUsername((String) request.getSession().getAttribute("usuario"));
                idUsuario = usuario.getId();

                if ( request.getSession().getAttribute("listaCompra") != null ) {
                    idVenta = (Integer) request.getSession().getAttribute("listaCompra");
                } else {
                    venta = ventaDAO.readGetByIdNotConf(idUsuario);
                    if ( venta != null )
                        idVenta = venta.getId();
                }

                if (idVenta == null ) {
                    idVenta = ventaDAO.createGetKey(idUsuario, false);
                }

                if ( venta == null ) {
                    venta = ventaDAO.read(idVenta);
                }
                request.setAttribute("venta", venta); 
                request.getSession().setAttribute("listaCompra", idVenta);

                String action = request.getParameter("action");
                action = action == null ? "" : action;
                switch (action) {
                    case "mostrarCatalogo":
                        mostrarCatalogo(request, response);
                        break;
                    case "addProductoView":
                        addProductoView(request, response);
                        break;
                    case "addProductoLista":
                        addProductoLista(request, response);
                        break;
                    case "seguroBorrar":
                        seguroBorrar(request, response);
                        break;
                    case "borrar":
                        borrar(request, response);
                        break;
                    case "confirmar":
                        confirmar(request, response);
                        break;
                    default:
                        defaultAction(request, response);
                        break;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                try {
                    response.sendRedirect(request.getContextPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void confirmar(HttpServletRequest request,
            HttpServletResponse response) {
        Integer idVenta = (Integer) request.getSession().getAttribute("listaCompra");
        ventaDAO.confirmar(idVenta);
        request.getSession().setAttribute("listaCompra", null);
        try {
            response.sendRedirect(request.getRequestURI());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void borrar(HttpServletRequest request,
            HttpServletResponse response) {
        Boolean valido = true;
        Integer id = null;
        try {
            if (request.getParameter("id") == null ) {
                valido = false;
            } else {
                id = Integer.parseInt(request.getParameter("id"));
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            valido = false;
        }

        if (valido) {
            detalleDAO.delete(id);
        }
        try {
            response.sendRedirect(request.getRequestURI());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void seguroBorrar(HttpServletRequest request,
            HttpServletResponse response) {
        Integer id = null;
        Boolean valido = true;
        if (request.getParameter("id") != null) {
            try {
                id = Integer.parseInt(request.getParameter("id"));
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                valido = false;
            }
        } else {
            valido = false;
        }

        if (valido) {
            Detalle detalle = null;
            try {
                detalle = detalleDAO.read(id);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            request.setAttribute("detalle", detalle);
            try {
                if (detalle!=null)
                    request.setAttribute("producto", productoDAO.read(detalle.getCodProd()));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        try {
            request.getRequestDispatcher("seguroBorrar.jsp").forward(request, response);
        } catch (ServletException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void addProductoLista(HttpServletRequest request,
            HttpServletResponse response) {
        Boolean valido = true;
        Producto producto = null;
        Integer cantidad = null;
        Venta venta = (Venta) request.getAttribute("venta");

        if (request.getParameter("codProd") == null) {
            this.errores.add("No se recibio un código de producto válido");
            valido = false;
        } else {
            try {
                producto = productoDAO.read(request.getParameter("codProd"));
            } catch (SQLException ex) {
                ex.printStackTrace();
                valido = false;
            } 
        }

        if (producto == null) {
            this.errores.add("No se recibio un código de producto válido");
            valido = false;
        } 
        
        try {
            cantidad = Integer.parseInt(request.getParameter("cantidad"));
        } catch (NumberFormatException ex) {
            this.errores.add("No se recibió una cantidad numérica");
            valido = false;
        }

        if (cantidad != null) {
            if (cantidad <= 0) {
                this.errores.add("No se puede comprar tan poco");
                valido = false;
            } else if (cantidad > producto.getStock()) {
                this.errores.add("No se puede comprar más de lo que hay");
                valido = false;
            }
        }
        
        if (valido) {
            detalleDAO.create(new Detalle(venta.getId(), 
                    producto.getCodProd(), cantidad));
            try {
                response.sendRedirect(request.getRequestURI());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            request.setAttribute("errores", this.errores);
            request.setAttribute("urlBack", request.getRequestURI());
            try {
                request.getRequestDispatcher("noSeCompra.jsp")
                        .forward(request, response);
            } catch (ServletException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void addProductoView(HttpServletRequest request,
            HttpServletResponse response) {
        Boolean valido = true;
        Venta venta = (Venta) request.getAttribute("venta");
        String codProd = null;
        Producto producto = null;
        if (request.getParameter("codProd") == null ) {
            this.errores.add("No se puede comprar, no se recibió código de producto");
            valido = false;
        } else {
            codProd = request.getParameter("codProd");
            try {
                producto = productoDAO.read(codProd);
                if (producto == null) {
                    this.errores.add("No se puede comprar, no se recibio un código de producto válido");
                    valido = false;
                } else {
                    if ( producto.getStock() <= 0 ) {
                        this.errores.add("No se puede comprar, no hay stock");
                        valido = false;
                    }
                                    }
            } catch (SQLException ex) {
                ex.printStackTrace();
                this.errores.add("No se puede comprar, no se recibio un código de producto válido");
                valido = false;
            }
            if (valido) {
                for (HashMap<String, Object> hm : detalleDAO.readByVenta(venta)) {
                    if (((String) hm.get("codProd")).equals(producto.getCodProd())) {
                        this.errores.add("No se puede comprar, ya se añadió este producto");
                        valido = false;
                    }
                }
            }

            if (valido) {
                try {
                    request.setAttribute("producto", producto);
                    request.getRequestDispatcher("addProducto.jsp").forward(request, response);
                } catch (ServletException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                request.setAttribute("errores", this.errores);
                request.setAttribute("urlBack", request.getRequestURI() + "?action=mostrarCatalogo");
                try {
                    request.getRequestDispatcher("noSeCompra.jsp").forward(request, response);
                } catch (ServletException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void mostrarCatalogo(HttpServletRequest request,
            HttpServletResponse response) {
        ArrayList<Producto> productos = null;
        try {
            productos = (ArrayList<Producto>) productoDAO.readAll();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        request.setAttribute("productos", productos);
        try {
            request.getRequestDispatcher("catalogo.jsp")
                    .forward(request, response);
        } catch (ServletException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void defaultAction(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            request.setAttribute("detalles", detalleDAO.readByVenta((Venta) request.getAttribute("venta")));
            request.getRequestDispatcher("listaCompra.jsp").forward(request, response);
        } catch (ServletException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) {
        doPost(request, response);
    }
}
