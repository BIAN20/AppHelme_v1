package com.example.helpme_app_v1.Model.Asesores;

import com.example.helpme_app_v1.Model.Asesor;
import com.example.helpme_app_v1.Model.Disponibilidad;
import com.example.helpme_app_v1.Model.Persona;
import com.example.helpme_app_v1.Model.Usuario;

import java.util.Arrays;

public class AsesorRequest {
    private Usuario usuario;
    private Persona persona;
    private Asesor asesor;
    private Disponibilidad disponibilidad;
    private String[] selectespecialidades;
    @Override
    public String toString() {
        return "AsesorRequest{" +
                "asesor=" + asesor +
                ", disponibilidad=" + disponibilidad +
                ", persona=" + persona +
                ", usuario=" + usuario +
                ", selectespecialidades=" + Arrays.toString(selectespecialidades) +
                '}';
    }
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Asesor getAsesor() {
        return asesor;
    }

    public void setAsesor(Asesor asesor) {
        this.asesor = asesor;
    }

    public Disponibilidad getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(Disponibilidad disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public String[] getSelectespecialidades() {
        return selectespecialidades;
    }

    public void setSelectespecialidades(String[] selectespecialidades) {
        this.selectespecialidades = selectespecialidades;
    }
}
