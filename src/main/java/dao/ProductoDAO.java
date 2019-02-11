package dao;

import dao.DAO;
import model.Producto;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO implements DAO<model.Producto>{
    /*
    CREATE TABLE producto (
        codigo_prod VARCHAR(20) NOT NULL,
        seccion VARCHAR(20) NOT NULL,
        nombre_prod VARCHAR(20) NOT NULL,
        precio DOUBLE,
        fecha DATE,
        importado tinyint(1) DEFAULT false,
        pais_origen VARCHAR(40),
        PRIMARY KEY (codigo_prod)
    );
    */
    public final static String SQL_INSERT = "INSERT INTO Productos (codProd, seccion, nombreProd, precio, fecha, importado, paisOrigen, stock) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    public final static String SQL_READ_ALL = "SELECT * FROM Productos";
    public final static String SQL_READ = "SELECT * FROM Productos where codProd = ?";
    public final static String SQL_UPDATE = "UPDATE Productos SET seccion=?, nombreProd=?, precio=?, fecha=?, importado=?, paisOrigen=?, stock=? WHERE codProd=?";
    public final static String SQL_DELETE = "DELETE FROM Productos WHERE codProd = ?";

    private DataSource dataSource;
    private ArrayList<String> errores;

    public ProductoDAO(DataSource dataSource) {
        errores = new ArrayList<String>();
        this.dataSource = dataSource;
    }

    public ArrayList<String> getErrores() {
        return this.errores;
    }

    @Override
    public boolean create(Producto producto) {
        Connection conn = null;
        try {
            conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_INSERT);
            ps.setString(1, producto.getCodProd());
            ps.setString(2, producto.getSeccion());
            ps.setString(3, producto.getNombreProd());
            ps.setDouble(4, producto.getPrecio());
            ps.setDate(5, Date.valueOf(producto.getFecha()));
            ps.setBoolean(6, producto.getImportado());
            ps.setString(7, producto.getPais());
            ps.setInt(8, producto.getStock());
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
    public boolean delete(Object key) {
        String codProd = (String) key;
        Connection conn = null;
        try {
            conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_DELETE);
            ps.setString(1, codProd);
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
    public boolean update(Producto producto) {
        Connection conn = null;
        try {
            conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_UPDATE);
            ps.setString(1, producto.getSeccion());
            ps.setString(2, producto.getNombreProd());
            ps.setDouble(3, producto.getPrecio());
            ps.setDate(4, Date.valueOf(producto.getFecha()));
            ps.setBoolean(5, producto.getImportado());
            ps.setString(6, producto.getPais());
            ps.setInt(7, producto.getStock());
            ps.setString(8, producto.getCodProd());
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
    public Producto read(Object codigo) throws SQLException {
        Connection conn = null;
        SQLException errorSQL = null;
        try {
            String codigoProducto = (String) codigo;
            conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_READ);
            ps.setString(1, codigoProducto);
            ResultSet rs = ps.executeQuery(); 
            rs.next();
            Producto producto = new Producto();
            producto.setCodProd(rs.getString(1));
            producto.setSeccion(rs.getString(2));
            producto.setNombreProd(rs.getString(3));
            producto.setPrecio(rs.getDouble(4));
            producto.setFecha(rs.getDate(5).toLocalDate());
            producto.setImportado(rs.getBoolean(6));
            producto.setPais(rs.getString(7));
            producto.setStock(rs.getInt(8));
            return producto;
       } catch (SQLException ex) {
           errorSQL = new SQLException(ex.getMessage());
           return null;
       } finally {
            conn.close();
            if (errorSQL != null) {
                throw errorSQL;
            }
       }
    }

    @Override
    public List<Producto> readAll() throws SQLException {
        ArrayList<Producto> productos = new ArrayList<Producto>();
        Connection conn = this.dataSource.getConnection();
        SQLException errorSQL = null;
        try {
            PreparedStatement ps = conn.prepareStatement(SQL_READ_ALL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setCodProd(rs.getString(1));
                producto.setSeccion(rs.getString(2));
                producto.setNombreProd(rs.getString(3));
                producto.setPrecio(rs.getDouble(4));
                producto.setFecha(rs.getDate(5).toLocalDate());
                producto.setImportado(rs.getBoolean(6));
                producto.setPais(rs.getString(7));
                producto.setStock(rs.getInt(8));
                productos.add(producto);
            }
            return productos;
        } catch (SQLException ex) {
            errorSQL = new SQLException(ex.getMessage());
            return null;
        } finally {
            conn.close();
            if (errorSQL != null) {
                throw errorSQL;
            }
        }
    }

}
