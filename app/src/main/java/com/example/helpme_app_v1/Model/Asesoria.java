package com.example.helpme_app_v1.Model;

import java.io.Serializable;
import java.util.Date;

public class Asesoria implements Serializable {
    private int estudiante;
    private Date fecha;
    private String horainicio;
    private String horafin;
    private int duracion;
    private int asesoriaprecio;
    private String url;
    private String estado;

    public int getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(int estudiante) {
        this.estudiante = estudiante;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHorainicio() {
        return horainicio;
    }

    public void setHorainicio(String horainicio) {
        this.horainicio = horainicio;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getHorafin() {
        return horafin;
    }

    public void setHorafin(String horafin) {
        this.horafin = horafin;
    }

    public int getAsesoriaprecio() {
        return asesoriaprecio;
    }

    public void setAsesoriaprecio(int asesoriaprecio) {
        this.asesoriaprecio = asesoriaprecio;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
