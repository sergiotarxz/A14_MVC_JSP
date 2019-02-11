package controller;

import model.Usuario;
import controller.UsuarioController;
import dao.UsuarioDAO;
import util.DataSourceUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.naming.NamingException;

import java.io.IOException;
import java.sql.SQLException;

import java.util.ArrayList;


@WebServlet("/usuarios")
public class UsuarioController extends HttpServlet {
    UsuarioDAO usuarioDAO;
    ArrayList<String> errores;
    Boolean criticalError;

    public UsuarioController() {
        this.criticalError = false;
        this.errores = new ArrayList<String>();
        try {
            usuarioDAO = new UsuarioDAO(new DataSourceUtils().getDataSource());
        } catch (NamingException ex) {
            ex.printStackTrace();
            this.errores.add("Error al obtener el recurso de la base de datos");
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
            forwardError(request, response);
        }
        String action = request.getParameter("action");
        action = action == null ? "" : action;
        switch(action) {
            case "deleteUsuario":
                deleteUsuario(request, response);
                break;
            case "addUsuario":
                crearUsuario(request, response);            
                break;
            case "addView":
                addView(request, response);
                break;
            case "mostrarUsuarios":
                mostrarUsuarios(request, response);
                break;
            case "updateView":
                updateView(request, response);
                break;
            case "updateUsuario":
                updateUsuario(request, response);
                break;
            default:
                mostrarUsuarios(request, response);
                break;
        }
    }
    private void addView(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            request.getRequestDispatcher("usuarios/create_usuario.jsp")
                    .forward(request, response); 
        } catch (ServletException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteUsuario(HttpServletRequest request,
            HttpServletResponse response) {
        Boolean valido = true;
        Integer id = null;
        if (request.getParameter("id") == null) {
            valido = false;
        } else {
            try {
                id = Integer.parseInt(request.getParameter("id"));
            } catch (NumberFormatException ex) {
                this.errores.add("El id no es un numero");
                valido = false;
            }
        }
        if (valido) {
            if (usuarioDAO.delete(id)) {
                try {
                    response.sendRedirect(request.getRequestURI()+ "?action=mostrarUsuarios");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                this.errores.add("No se pudo borrar ese registro, probablemente ni exista");
                forwardError(request,response);
            }
        } else {
            forwardError(request, response);
        }
    }

    private void updateUsuario(HttpServletRequest request,
            HttpServletResponse response) {
        Integer id = null;
        Boolean valido = true;
        if (request.getParameter("id") == null) {
            this.errores.add("No se ha recibido ningun id");
            valido = false; 
        }
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException ex) {
            this.errores.add("El id no es un número.");
            valido = false; 
        }
        String nombre = request.getParameter("nombre");  
        String apellidos = request.getParameter("apellidos");  
        String username = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");
        String pais = request.getParameter("pais");
        String tecnologia = request.getParameter("tecnologia");
        if (username == null) {
            valido = false; 
        }
        if (valido) {
            Usuario usuario = new Usuario(id, nombre, apellidos, username, contrasena, pais, tecnologia);
            if (usuarioDAO.update(usuario)) {
                try {
                response.sendRedirect(request.getRequestURI()+ "?action=mostrarUsuarios");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                this.errores.add("No se ha actualizado nada");
                forwardError(request, response);
            }
        } else {
            forwardError(request, response);
        }
    }
    private void updateView(HttpServletRequest request,
            HttpServletResponse response) {
            Integer id = null;
            Boolean valido = true;
            if (request.getParameter("id") == null) {
                this.errores.add("No se ha recibido ningun id");
                valido = false;
            }
            try {
                id = Integer.parseInt(request.getParameter("id"));
            } catch (NumberFormatException ex) {
                this.errores.add("El id no es un número.");
                valido = false;
            }
            if (valido) {
                try {
                    request.setAttribute("usuario", usuarioDAO.read(id));
                    request.getRequestDispatcher("usuarios/update_usuario.jsp").forward(request, response);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    this.errores.add("Este producto no se pudo encontrar.");
                    forwardError(request, response);
                } catch (ServletException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                forwardError(request, response);
            }
    }
    
    private void mostrarUsuarios(HttpServletRequest request, 
            HttpServletResponse response) {
        try {
            request.setAttribute("usuarios", usuarioDAO.readAll());
            request.getRequestDispatcher("usuarios/mostrar_usuarios.jsp")
                    .forward(request, response);
        } catch (ServletException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void crearUsuario(HttpServletRequest request,
            HttpServletResponse response) {
        String nombre = request.getParameter("nombre");  
        String apellidos = request.getParameter("apellidos");  
        String usuario = request.getParameter("usuario");  
        String contrasena = request.getParameter("contrasena");
        String pais = request.getParameter("pais");
        String tecnologia = request.getParameter("tecnologia");
        Boolean valido = true;
        if (!isValidString(usuario)) {
            errores.add("El usuario no puede estar vacio.");
            valido = false; 
        }
        if (valido) {
            usuarioDAO.create(new Usuario(nombre, apellidos, usuario, contrasena, pais, tecnologia));
            try {
                response.sendRedirect(request.getRequestURI()+ "?action=mostrarUsuarios");
            } catch (IOException ex) {
               ex.printStackTrace(); 
            }
        } else {
            forwardError(request, response);
        }
    }

    public static boolean isValidString(String string) {
        if (string != null)
            if (!string.isEmpty())
                return true;
        return false;
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
