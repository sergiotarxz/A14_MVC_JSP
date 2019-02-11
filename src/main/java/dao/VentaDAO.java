package dao;

import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.Venta;

public class VentaDAO 
        implements DAO<Venta> {
    public final static String SQL_DELETE = "DELETE FROM Ventas WHERE id = ?"; 
    public final static String SQL_INSERT = "INSERT INTO Ventas (id_usuario, fecha, conf) SELECT ?, CURDATE(), ?"; 
    public final static String SQL_READ_ALL = "SELECT * FROM Ventas";
    public final static String SQL_READ = "SELECT * FROM Ventas WHERE id = ?";
    public final static String SQL_UPDATE = "UPDATE Productos SET id_usuario=?, fecha=?, conf=? WHERE id=?";
    public final static String SQL_READ_NO_CONF = "SELECT * FROM Ventas WHERE conf=0 and id_usuario=?";
    public final static String SQL_READ_CONF = "SELECT * FROM Ventas WHERE conf=1 and id_usuario=?";
    public final static String SQL_CONF = "Update Ventas set conf=1 WHERE id=?";
    private DataSource dataSource;
    private ArrayList<String> errores;

    public VentaDAO(DataSource dataSource) {
        this.dataSource = dataSource;
        errores = new ArrayList<String>();
    }

    public ArrayList<String> getErrores() {
        return this.errores;
    }

    @Override
    public boolean create(Venta venta) {
        Connection conn = null;
        try {
            conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_INSERT);
            ps.setInt(1, venta.getIdUsuario());
            ps.setBoolean(2, venta.getConf());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.errores.add("No se pudo crear venta.");
            return false;
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    public Integer createGetKey(Integer idUsuario, Boolean conf) {
        Connection conn = null;
        try {
            conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, idUsuario);
            ps.setBoolean(2, conf);
            if ( ps.executeUpdate() > 0 ) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.errores.add("No se pudo crear venta.");
            return null;
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public boolean delete(Object key) {
        Integer id = (Integer) key;
        Connection conn = null;
        try {
            conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_DELETE);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.errores.add("No se pudo borrar esta venta.");
            return false;
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public boolean update(Venta venta) {
        Connection conn = null;
        try {
            conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_UPDATE);
            ps.setInt(1, venta.getIdUsuario());
            ps.setDate(2, java.sql.Date.valueOf(venta.getFecha()));
            ps.setInt(3, venta.getId());
            ps.setBoolean(4, venta.getConf());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.errores.add(ex.getMessage());
            return false;
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public Venta read(Object key) 
            throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer id = (Integer) key;
        SQLException errorSQL = null;
        try {
            conn = this.dataSource.getConnection();
            ps = conn.prepareStatement(SQL_READ);    
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Venta venta = new Venta();
                venta.setId(rs.getInt("id")); 
                venta.setFecha(rs.getDate("fecha").toLocalDate());
                venta.setIdUsuario(rs.getInt("id_usuario"));
                venta.setConf(rs.getBoolean("conf"));
                return venta;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            errorSQL = new SQLException(ex.getMessage());
            return null;
        } finally {
            if (conn != null) 
                conn.close();
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();

            if (errorSQL != null) {
                throw errorSQL;
            }
        }
    }

    public Venta readGetByIdNotConf(Integer id) {
        Connection conn = null;
        PreparedStatement ps = null;  
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection(); 
            ps = conn.prepareStatement(SQL_READ_NO_CONF);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Venta venta = new Venta();
                venta.setId(rs.getInt("id")); 
                venta.setFecha(rs.getDate("fecha").toLocalDate());
                venta.setIdUsuario(rs.getInt("id_usuario"));
                venta.setConf(rs.getBoolean("conf"));
                return venta;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
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

    public boolean confirmar(Integer id) {
        Connection conn = null;
        PreparedStatement ps = null;  
        try {
            conn = this.dataSource.getConnection();
            ps = conn.prepareStatement(SQL_CONF);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                return true;
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public List<Venta> readAll() throws SQLException {
        return null;
    }

    public List<Venta> readGetByIdConf(Integer idUsuario) {
        Connection conn = null;
        PreparedStatement ps = null;  
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            ps = conn.prepareStatement(SQL_READ_CONF);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            List<Venta> ventas = new ArrayList<Venta>();
            while (rs.next()) {
                Venta venta = new Venta();
                venta.setId(rs.getInt("id")); 
                venta.setFecha(rs.getDate("fecha").toLocalDate());
                venta.setIdUsuario(rs.getInt("id_usuario"));
                venta.setConf(rs.getBoolean("conf"));
                ventas.add(venta);
            }
            if (ventas.size() == 0) {
                return null;
            } else {
                return ventas;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
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
