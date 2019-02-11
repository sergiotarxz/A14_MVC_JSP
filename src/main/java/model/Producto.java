package model;

import java.time.LocalDate;

public class Producto {
    private String codProd;
    private String seccion;
    private String nombreProd;
    private Double precio;
    private LocalDate fecha;
    private Boolean importado;
    private String pais;
    private Integer stock;

    public Producto() {
    }

    public Producto(String codProd, String nombreProd, String seccion, Double precio, LocalDate fecha, Boolean importado, String pais, Integer stock) {
        this.codProd = codProd;
        this.seccion = seccion;
        this.nombreProd = nombreProd;
        this.precio = precio;
        this.fecha = fecha;
        this.importado = importado;
        this.pais = pais;
        this.stock = stock;
    }
    public Producto( String seccion, String nombreProd, Double precio, LocalDate fecha, Boolean importado, String pais, Integer stock) {
        this.seccion = seccion;
        this.nombreProd = nombreProd;
        this.precio = precio;
        this.fecha = fecha;
        this.importado = importado;
        this.pais = pais;
        this.stock = stock;
    }

    public Integer getStock() {
        return this.stock;
    }

    public String getNombreProd() {
        return this.nombreProd;
    }

    public String getPais() {
        return this.pais;
    }

    public Boolean getImportado() {
        return this.importado;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Double getPrecio() {
        return this.precio;
    }

    public String getSeccion() {
        return this.seccion;
    }

    public String getCodProd() {
        return this.codProd;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }


    public void setPais(String pais) {
        this.pais = pais;
    }

    public void setImportado(Boolean importado) {
        this.importado = importado;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public void setCodProd(String codProd) {
        this.codProd = codProd;
    }
    
    public void setNombreProd(String nombreProd) {
        this.nombreProd = nombreProd;
    }

}
