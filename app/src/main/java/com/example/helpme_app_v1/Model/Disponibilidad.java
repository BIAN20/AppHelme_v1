package com.example.helpme_app_v1.Model;

import java.io.Serializable;
import java.time.LocalTime;

public class Disponibilidad implements Serializable {

    private String[] disponibilidad;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    public String[] getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String[] disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }
}
