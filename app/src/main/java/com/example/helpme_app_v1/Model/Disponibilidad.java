package com.example.helpme_app_v1.Model;

import java.io.Serializable;
import java.time.LocalTime;

public class Disponibilidad implements Serializable {

    private String[] disponibilidad;
    private String horaInicio;
    private String horaFin;

    public String[] getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String[] disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }
}
