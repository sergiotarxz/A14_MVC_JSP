package controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;;

import javax.naming.NamingException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.io.IOException;

import dao.UsuarioDAO;

import model.Usuario;
import util.DataSourceUtils;

@WebServlet("/")
public class LoginController extends HttpServlet {
    ArrayList<String> errores;
    UsuarioDAO usuarioDAO;
    Boolean criticalError = false;
    String initialError = "";
    public LoginController() {
        try {
            usuarioDAO = new UsuarioDAO(new DataSourceUtils().getDataSource());
        } catch (NamingException ex) {
            ex.printStackTrace();
            this.criticalError = true;
            this.initialError = "Imposible obtener fuente de datos en este contexto";
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) {
        this.errores = new ArrayList<String>();
        if (!this.criticalError) {
            HttpSession hs = request.getSession();
            String action = request.getParameter("action");
            action = action == null ? "" : action;
            switch (action) {
                case "login":
                    login(request, response);
                    break;
                case "logout":
                    logout(request, response);
                    break;
                case "loginView":
                    loginView(request, response);
                    break;
                case "registrar":
                    registrar(request, response);
                    break;
                case "registrarView":
                    registrarView(request, response);
                    break;
                default:
                    defaultAction(request, response);
                    break;
            }
        } else {
            this.errores.add(this.initialError);
            forwardError(request, response);
        }
    }

    private void registrar(HttpServletRequest request, 
            HttpServletResponse response) {
        String nombre = null;
        String apellidos = null;
        String usuario = null;
        String contrasena = null;
        String pais = null;
        String tecnologia = null;
        Boolean insertado = false;
        Boolean valido = true;

        if (request.getParameter("nombre") == null) {
            this.errores.add("El nombre no puede ser nulo"); 
            valido = false;
        } else if (request.getParameter("nombre").isEmpty()) {
            this.errores.add("El nombre no puede estar vacío"); 
            valido = false;
        } else {
            nombre = request.getParameter("nombre");
        }
        
        if (request.getParameter("apellidos") == null) {
            this.errores.add("Los apellidos no pueden ser nulos");
            valido = false;
        } else if (request.getParameter("apellidos").isEmpty()) {
            this.errores.add("Los apellidos no puede estar vacíos"); 
            valido = false;
        } else {
            apellidos = request.getParameter("apellidos");
        }

        if (request.getParameter("usuario") == null) {
            this.errores.add("El nombre de usuario no puede ser nulo"); 
            valido = false;
        } else if (request.getParameter("usuario").isEmpty()) {
            this.errores.add("El nombre de usuario no puede estar vacio"); 
            valido = false;
        } else {
            usuario = request.getParameter("usuario");
        }

        if (request.getParameter("contrasena") == null) {
            this.errores.add("La contraseña no puede ser nula"); 
            valido = false;
        } else if (request.getParameter("contrasena").isEmpty()) {
            this.errores.add("La contraseña no puede estar vacía");  
            valido = false;
        } else {
            contrasena = request.getParameter("contrasena");
        }

        if (request.getParameter("pais") == null) {
            this.errores.add("El país no puede ser nulo"); 
            valido = false;
        } else if (request.getParameter("pais").isEmpty()) {
            this.errores.add("El país no puede estar vacío");  
            valido = false;
        } else {
            pais = request.getParameter("pais");
        }

        if (request.getParameter("tecnologia") == null) {
            this.errores.add("La tecnología no puede ser nula"); 
            valido = false;
        } else if (request.getParameter("tecnologia").isEmpty()) {
            this.errores.add("La tecnología no puede estar vacía");  
            valido = false;
        } else {
            tecnologia = request.getParameter("tecnologia");
        }
        
        if (valido) {
            if (usuarioDAO.create(new Usuario(nombre, apellidos, usuario, contrasena, pais, tecnologia))) {
                insertado = true;
            } else {
                this.errores.add("No se pudieron insertar estos datos");
            }
        }

        if (insertado) {
            try {
                response.sendRedirect(request.getRequestURI());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            request.setAttribute("errores", this.errores);
            try {
                request.getRequestDispatcher("usuarios/create_usuario.jsp").forward(request, response);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ServletException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void registrarView(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            request.getRequestDispatcher("usuarios/create_usuario.jsp").forward(request, response);
        } catch (ServletException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void defaultAction(HttpServletRequest request,
            HttpServletResponse response) {
        if (request.getSession().getAttribute("usuario") == null) {
            try {
                response.sendRedirect(request.getRequestURI() + "?action=loginView");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                response.sendRedirect("menu_principal");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void logout(HttpServletRequest request, 
            HttpServletResponse response) {
        request.getSession().invalidate();
        try {
            response.sendRedirect(request.getRequestURI());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void login(HttpServletRequest request, 
            HttpServletResponse response) {
        if (request.getSession().getAttribute("usuario") == null) {
            if (checkUsuario(request, response) 
                    && checkPassword(request, response)) {
                String user = request.getParameter("user");
                String password = request.getParameter("pass");
                if (checkPass(request, response)) {
                    try {
                        response.sendRedirect(request.getRequestURI());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    try {
                        request.setAttribute("errores", this.errores);
                        request.getRequestDispatcher("login.jsp").forward(request, response);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (ServletException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                try {
                    request.setAttribute("errores", this.errores);
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                } catch (ServletException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            try {
                response.getWriter().print("<a href=\"" + request.getRequestURI() + "\">Volver</a>");
                response.getWriter().print("Ya estás logueado!");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
    private Boolean checkPass(HttpServletRequest request,
            HttpServletResponse response) {
        if (usuarioDAO.checkPassword(request.getParameter("user"), request.getParameter("pass"))) {
            request.getSession().setAttribute("usuario", request.getParameter("user"));    
            return true;
        } else {
            this.errores.add("El login falló, esta contraseña no es válida para este usuario");
            return false;
        }
    }

    private Boolean checkUsuario(HttpServletRequest request,
            HttpServletResponse response) {
        if (request.getParameter("user") == null) {
                this.errores.add("No se recibió ningún usuario");
                return false;
        } else if (request.getParameter("user").isEmpty()) {
                this.errores.add("El usuario esta vacío");
                return false;
        }
        return true;

    }

    private Boolean checkPassword(HttpServletRequest request,
            HttpServletResponse response) {
        if (request.getParameter("pass") == null) {
            this.errores.add("La contraseña esta vacía");
            return false;
        } else if (request.getParameter("pass").isEmpty()) {
            this.errores.add("No se recibió ninguna contraseña");
            return false;
        }
        return true;
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
    
    private void loginView(HttpServletRequest request, 
            HttpServletResponse response) {
        try {
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } catch (ServletException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
