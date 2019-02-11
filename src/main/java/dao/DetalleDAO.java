package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import javax.sql.DataSource;

import model.Detalle;
import model.Venta;

public class DetalleDAO implements DAO<Detalle> {
    public final static String[] SQL_INSERT = {
        "SET @codProd=?", 
        "SET @id_venta=?", 
        "SET @cantidad=?", 
        "SELECT * FROM Productos WHERE codProd=@codProd FOR UPDATE",
        "INSERT INTO Detalle (codProd, id_venta, cantidad)" 
                + "SELECT @codProd, @id_venta, @cantidad FROM Productos WHERE codProd=@codProd and stock-@cantidad >= 0",
        "UPDATE Productos set stock=stock-@cantidad WHERE stock-@cantidad >= 0 and codProd=@codProd",
    };
    public final static String SQL_READ = "SELECT * FROM Detalle WHERE id=?";
    public final static String SQL_READ_BY_VENTA = "SELECT Productos.codProd as codProd, Productos.nombreProd as nombre, Productos.precio as precio, Productos.precio*Detalle.cantidad as precio_total, Detalle.cantidad as cantidad, Detalle.id as id_detalle FROM Detalle INNER JOIN Productos ON Productos.codProd=Detalle.codProd WHERE Detalle.id_venta=?";
    public final static String[] SQL_UPDATE = {
        "SET @id=?",
        "SET @cantidad=?",
        "SELECT Detalle.*, Productos.* FROM Detalle INNER JOIN Productos ON Productos.codProd=Detalle.codProd WHERE Detalle.id=@id FOR UPDATE;",
        "SET @codProd:=(SELECT codProd FROM Detalle WHERE id=@id)",
        "SET @diferencia := (SELECT Detalle.cantidad-@cantidad FROM Detalle WHERE id=@id)",
        "UPDATE Detalle SET cantidad=@cantidad WHERE id=@id",
        "UPDATE Productos SET stock=stock+@diferencia WHERE codProd=@codProd"
    };

    public final static String[] SQL_DELETE = {
        "SET @id=?",
        "SELECT Detalle.*, Productos.* FROM Detalle INNER JOIN Productos ON Productos.codProd=Detalle.codProd WHERE Detalle.id=@id FOR UPDATE;",
        "SET @codProd:=(SELECT codProd FROM Detalle WHERE id=@id)",
        "SET @diferencia := (SELECT Detalle.cantidad-@cantidad FROM Detalle WHERE id=@id)",
        "SET @diferencia := (SELECT Detalle.cantidad FROM Detalle WHERE id=@id)",
        "DELETE FROM Detalle WHERE id=@id",
        "UPDATE Productos SET stock=stock+@diferencia WHERE codProd=@codProd"
    };

    DataSource dataSource;
    public DetalleDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public Detalle read(Object key) 
            throws SQLException {
        Integer id = (Integer) key;
        Connection conn = null;
        SQLException errorSQL = null;
        try {
            conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_READ);    
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
                Integer idVenta = rs.getInt("id_venta");
                Integer cantidad = rs.getInt("cantidad");
                String codProd = rs.getString("codProd");
                return new Detalle(id, idVenta, codProd, cantidad);
            } else {
                return null;
            }
        } catch (SQLException ex) {
            errorSQL = new SQLException(ex.getMessage());
            return null;
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            if (errorSQL != null ) {
                throw errorSQL;
            }
        }
    }

    public List<HashMap<String,Object>> readByVenta(Venta venta) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer ncol = null;
        try {
            conn = this.dataSource.getConnection();
            ps = conn.prepareStatement(SQL_READ_BY_VENTA);
            ps.setInt(1, venta.getId());
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            ncol = rsmd.getColumnCount();
            ArrayList<HashMap<String,Object>> listaCompra = new ArrayList<HashMap<String,Object>>();
            while (rs.next()) {
                HashMap<String,Object> hm = new HashMap<String,Object>();
                for (int i = 0; i < ncol; i++) {
                    hm.put(rsmd.getColumnLabel(i+1), rs.getObject(rsmd.getColumnLabel(i+1)));
                }
                listaCompra.add(hm);
            }
            return listaCompra;
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

    @Override
    public boolean create(Detalle detalle) {
        Connection conn = null;
        ArrayList<PreparedStatement> pss = new ArrayList<PreparedStatement>();
        try {
            conn = this.dataSource.getConnection();
            conn.setAutoCommit(false);
            Boolean retorno = false;
            for (int i = 0; i < SQL_INSERT.length; i++ ) {
                pss.add(conn.prepareStatement(SQL_INSERT[i]));
                switch (i) {
                    case 0:
                        pss.get(i).setString(1, detalle.getCodProd());
                        break;
                    case 1:
                        pss.get(i).setInt(1, detalle.getIdVenta());
                        break;
                    case 2:
                        pss.get(i).setInt(1, detalle.getCantidad());
                        break;
                }
                if (i != 3) {
                    if (pss.get(i).executeUpdate() > 0 && i == 4) {
                        retorno = true;
                    }
                } else {
                    pss.get(i).executeQuery();
                }
            }
            conn.commit();
            return retorno;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.rollback();
                    conn.close();
                }
                for (PreparedStatement ps : pss) {
                    if (ps != null)
                        ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    @Override
    public boolean update(Detalle detalle) {
        return false;
    }

    public boolean updateReal(Integer id, Integer cantidad) {
        Connection conn = null;
        ArrayList<PreparedStatement> pss = new ArrayList<PreparedStatement>();
        try {
            conn = this.dataSource.getConnection(); 
            conn.setAutoCommit(false);
            Boolean retorno = false;
            for (int i = 0; i < SQL_UPDATE.length; i++ ) {
                pss.add(conn.prepareStatement(SQL_UPDATE[i]));
                switch (i) {
                    case 0:
                        pss.get(i).setInt(1, id);
                        break;
                    case 1:
                        pss.get(i).setInt(1, cantidad);
                        break;
                }
                if (i == 5 || i == 6) {
                    if (pss.get(i).executeUpdate() > 0 && i == 5) {
                        retorno = true;
                    }
                } else {
                    pss.get(i).executeQuery();
                }
            }
            conn.commit();
            return retorno;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.rollback();
                    conn.close();
                }
                if (pss != null) {
                    for (PreparedStatement ps : pss) {
                        ps.close();
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public boolean delete(Object key) {
        Integer id = (Integer) key;
        Connection conn = null;
        ArrayList<PreparedStatement> pss = new ArrayList<PreparedStatement>();
        try {
            conn = this.dataSource.getConnection(); 
            conn.setAutoCommit(false);
            Boolean retorno = false;
            for (int i = 0; i < SQL_DELETE.length; i++ ) {
                pss.add(conn.prepareStatement(SQL_DELETE[i]));
                switch (i) {
                    case 0:
                        pss.get(i).setInt(1, id);
                        break;
                }
                if (i == 5 || i == 6) {
                    if (pss.get(i).executeUpdate() > 0 && i == 5) {
                        retorno = true;
                    }
                } else {
                    pss.get(i).executeQuery();
                }
            }
            conn.commit();
            return retorno;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.rollback();
                    conn.close();
                }
                if (pss != null) {
                    for (PreparedStatement ps : pss) {
                        ps.close();
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    @Override
    public List<Detalle> readAll() {
        return null;
    }
}
