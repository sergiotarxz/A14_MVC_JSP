package model;

public class Detalle {
    private Integer id;
    private Integer idVenta;
    private String codProd;
    private Integer cantidad;

    public Detalle() {
    }
    public Detalle(Integer idVenta, String codProd, Integer cantidad) {
        this.idVenta = idVenta;
        this.codProd = codProd;
        this.cantidad = cantidad;
    }

    public Detalle(Integer id, Integer idVenta, String codProd, Integer cantidad) {
        this.id = id;
        this.idVenta = idVenta;
        this.codProd = codProd;
        this.cantidad = cantidad;
    }
    
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public void setCodProd(String codProd) {
        this.codProd = codProd;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getCantidad() {
        return this.cantidad;
    }

    public String getCodProd() {
        return this.codProd;
    }

    public Integer getIdVenta() {
        return this.idVenta;
    }

    public Integer getId() {
        return this.id;
    }

}
