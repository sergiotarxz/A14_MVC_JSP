package model;

import java.time.LocalDate;

public class Venta {
    private Integer id;
    private LocalDate fecha;
    private Integer idUsuario;
    private Boolean conf;

    public Venta() {
    }

    public Venta(Integer id, LocalDate fecha,
            Integer idUsuario, Boolean conf) {
        this.id = id;
        this.fecha = fecha;
        this.idUsuario = idUsuario;
        this.conf = conf;
    }

    public Venta(LocalDate fecha, 
            Integer idUsuario, Boolean conf) {
        this.fecha = fecha;
        this.idUsuario = idUsuario;
        this.conf = conf;
    }

    public Boolean getConf() {
        return this.conf;
    }

    public Integer getIdUsuario() {
        return this.idUsuario;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Integer getId() {
        return this.id;
    }

    public void setConf(Boolean conf) {
        this.conf = conf;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
