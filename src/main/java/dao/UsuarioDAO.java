package dao;

import dao.DAO;

import model.Usuario;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

/*
 * CREATE TABLE usuario (
 * id INT(11) AUTO_INCREMENT,
 * nombre VARCHAR(20) NOT NULL,
 * apellidos VARCHAR(20) NOT NULL,
 * usuario VARCHAR(20) NOT NULL,
 * contrasena VARCHAR(20) NOT NULL,
 * pais VARCHAR(20),
 * tecnologia VARCHAR(20),
 * PRIMARY KEY(id)
 * )
 */ 
public class UsuarioDAO implements DAO<Usuario> {

	private DataSource dataSource;

	public static final String SQL_INSERT = "INSERT INTO Usuarios (nombre, apellidos, usuario, contrasena, pais, tecnologia) VALUES (?, ?, ?, ?, ?, ?)"; 

	public static final String SQL_GETALL = "SELECT * FROM Usuarios" ;
	
	public static final String SQL_GET = "SELECT * FROM Usuarios WHERE id = ?";
	public static final String SQL_GET_BY_USERNAME = "SELECT * FROM Usuarios WHERE usuario = ?";
    public static final String SQL_DELETE = "DELETE FROM Usuarios WHERE id = ?";
    public static final String SQL_UPDATE = "UPDATE Usuarios SET nombre = ?, apellidos = ?, usuario = ?, contrasena = ?, pais = ?, tecnologia = ? WHERE id = ?";
    public static final String SQL_CHECKPASS = "SELECT (contrasena=?) FROM Usuarios WHERE usuario = ?"; 
	

	public UsuarioDAO(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public boolean create(Usuario usuario) {
        Connection conn = null;
		try {
            conn = this.dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(SQL_INSERT);
			ps.setString(1, usuario.getNombre());
			ps.setString(2, usuario.getApellidos());
			ps.setString(3, usuario.getUsuario());
			ps.setString(4, usuario.getContrasena());
			ps.setString(5, usuario.getPais());
			ps.setString(6, usuario.getTecnologia());
			return ps.executeUpdate() == 1 ? true : false;
		} catch (SQLException ex) {
			ex.getStackTrace();
			return false;
		} finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
	}	
	@Override
	public boolean delete(Object key) {
        Connection conn = null;
        try {
            Integer id = (Integer) key;
            conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_DELETE);
            ps.setInt(1, id);
            return ps.executeUpdate() == 1 ? true : false;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
	}

	@Override
	public boolean update(Usuario usuario) {
        Connection conn = null;
        try {
            conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_UPDATE);
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellidos());
            ps.setString(3, usuario.getUsuario());
            ps.setString(4, usuario.getContrasena());
            ps.setString(5, usuario.getPais());
            ps.setString(6, usuario.getTecnologia());
            ps.setInt(7, usuario.getId());
            return ps.executeUpdate() == 1 ? true : false;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
	}

	@Override
	public Usuario read(Object k) throws SQLException {
        Connection conn = this.dataSource.getConnection();
        SQLException errorSQL = null;
        try {
            conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_GET);
            Integer key = (int) k;
            ps.setInt(1, key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id"), rs.getString("nombre"),
                        rs.getString("apellidos"), rs.getString("usuario"),
                        rs.getString("contrasena"), rs.getString("pais"),
                        rs.getString("tecnologia"));
                return usuario;
            }
            return null; 
        } catch (SQLException ex) {
            errorSQL = new SQLException(ex.getMessage());
            return null;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            if (errorSQL != null) {
                throw errorSQL;
            }
        }
	}

	public Usuario readByUsername(String usuarioStr) throws SQLException {
        Connection conn = null;
        SQLException errorSQL = null;
        try {
            conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_GET_BY_USERNAME);
            ps.setString(1, usuarioStr);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id"), rs.getString("nombre"),
                        rs.getString("apellidos"), rs.getString("usuario"),
                        rs.getString("contrasena"), rs.getString("pais"),
                        rs.getString("tecnologia"));
                return usuario;
            } 
            return null;
        } catch (SQLException ex) {
            errorSQL = new SQLException(ex.getMessage());
            return null;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            if (errorSQL != null) {
                throw errorSQL;
            }
        }
	}

	@Override
	public List<Usuario> readAll() {
        Connection conn = null;
        PreparedStatement ps = null;
        ArrayList<Usuario> ret = new ArrayList<Usuario>();
        try {
            conn = this.dataSource.getConnection();
            ps = conn.prepareStatement(SQL_GETALL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id"), rs.getString("nombre"),
                        rs.getString("apellidos"), rs.getString("usuario"),
                        rs.getString("contrasena"), rs.getString("pais"),
                        rs.getString("tecnologia"));
                ret.add(usuario);
            }
            return ret;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null)
                    conn.close();
                if (ps != null)
                    ps.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                return null;
            }
        }
		return null;
	}
    
    public Boolean checkPassword(String user,
            String pass) {
        PreparedStatement ps = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            ps = conn.prepareStatement(SQL_CHECKPASS);
            ps.setString(1, pass);
            ps.setString(2, user);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1);
            } else {
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null)
                    conn.close();
                if (ps != null)
                    ps.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
