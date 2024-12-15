package com.example.helpme_app_v1.Model;

import java.io.Serializable;

import kotlin.text.StringsKt;
import kotlin.text.UStringsKt;

public class Asesor implements Serializable {
    private int aniosExperiencia;
    private String presentacion;
    private String enseniazaPreferida;
    private String codigoColegiatura;
    private int calificacion;
    private String certigoogledrive;
    private String estado;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCertigoogledrive() {
        return certigoogledrive;
    }

    public void setCertigoogledrive(String certigoogledrive) {
        this.certigoogledrive = certigoogledrive;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public int getAniosExperiencia() {
        return aniosExperiencia;
    }

    public void setAniosExperiencia(int aniosExperiencia) {
        this.aniosExperiencia = aniosExperiencia;
    }

    public String getEnseniazaPreferida() {
        return enseniazaPreferida;
    }

    public void setEnseniazaPreferida(String enseniazaPreferida) {
        this.enseniazaPreferida = enseniazaPreferida;
    }

    public String getCodigoColegiatura() {
        return codigoColegiatura;
    }

    public void setCodigoColegiatura(String codigoColegiatura) {
        this.codigoColegiatura = codigoColegiatura;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }
}
