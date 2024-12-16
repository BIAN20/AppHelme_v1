package com.example.helpme_app_v1.Model;

import java.io.Serializable;

public class AsesoriaPrecio implements Serializable {
    private String tipoasesoria;
    private int asesor;
    private int tokens;
    private int duracion;

    public String getTipoasesoria() {
        return tipoasesoria;
    }

    public void setTipoasesoria(String tipoasesoria) {
        this.tipoasesoria = tipoasesoria;
    }

    public int getAsesor() {
        return asesor;
    }

    public void setAsesor(int asesor) {
        this.asesor = asesor;
    }

    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }
}
