package controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;;

import javax.naming.NamingException;

import java.util.ArrayList;
import java.io.IOException;;
import java.sql.SQLException;

import dao.ProductoDAO;
import dao.UsuarioDAO;
import dao.VentaDAO;
import dao.DetalleDAO;

import model.Usuario;
import model.Venta;

import util.DataSourceUtils;

@WebServlet(urlPatterns = {"/menu_principal/", "/menu_principal"})
public class MenuController extends HttpServlet {
    ProductoDAO productoDAO;
    UsuarioDAO usuarioDAO;
    VentaDAO ventaDAO;
    DetalleDAO detalleDAO;
    ArrayList<String> errores;

    public MenuController() {
        try {
            productoDAO = new ProductoDAO(new DataSourceUtils().getDataSource());
            usuarioDAO = new UsuarioDAO(new DataSourceUtils().getDataSource());
            ventaDAO = new VentaDAO(new DataSourceUtils().getDataSource());
            detalleDAO = new DetalleDAO(new DataSourceUtils().getDataSource());
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
                request.getRequestDispatcher("logueateAntes.jsp").forward(request, response);
            } catch (ServletException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            String action = request.getParameter("action");
            action = action == null ? "" : action;
            switch(action) {
                case "listaUsuarios":
                    listaUsuarios(request, response);
                    break;
                case "misDatos":
                    misDatos(request, response);
                    break;
                case "seleccionarFactura":
                    seleccionarFactura(request, response);
                    break;
                case "factura":
                    factura(request, response);
                    break;
                default:
                    defaultAction(request, response); 
                    break;
            }
        }
    }

    private void factura(HttpServletRequest request,
            HttpServletResponse response) {
        Boolean valido = true;
        Integer id = null;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            valido = false;
        }
        if (valido) {
            try {
                Venta venta = ventaDAO.read(id);
                request.setAttribute("detalles", detalleDAO.readByVenta(venta));
            } catch (SQLException ex) {
                ex.printStackTrace();
                valido = false;
            }
        }

        if (request.getAttribute("detalles") == null) {
            valido = false;
        }
        
        if (valido) {
            try {
                request.getRequestDispatcher("factura.jsp").forward(request, response); 
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ServletException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                response.sendRedirect(request.getRequestURI());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void seleccionarFactura(HttpServletRequest request,
            HttpServletResponse response) {
        Usuario usuario = null;
        try {
             usuario = usuarioDAO.readByUsername((String) request.getSession().getAttribute("usuario"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (usuario != null) {
            request.setAttribute("ventas", ventaDAO.readGetByIdConf( usuario.getId() ) );
        }
        try {
            request.getRequestDispatcher("seleccionarFactura.jsp").forward(request, response);
        } catch (ServletException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void listaUsuarios(HttpServletRequest request,
            HttpServletResponse response) {
        ArrayList<Usuario> usuarios = (ArrayList<Usuario>) usuarioDAO.readAll();
        request.setAttribute("usuarios", usuarios);
        try {
            request.getRequestDispatcher("listaUsuarios.jsp").forward(request, response);
        } catch (ServletException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void misDatos(HttpServletRequest request,
            HttpServletResponse response) {
        String username = (String) request.getSession().getAttribute("usuario");
        try {
            Usuario usuario = usuarioDAO.readByUsername(username);
            request.setAttribute("usuario", usuario);
            try {
                request.getRequestDispatcher("misDatos.jsp").forward(request, response);
            } catch (ServletException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            try {
                response.sendRedirect(request.getContextPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
        }
    }

    private void defaultAction(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            request.getRequestDispatcher("menu.jsp").forward(request, response);        
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

    private void sendRedirect(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            response.sendRedirect(request.getRequestURI());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
