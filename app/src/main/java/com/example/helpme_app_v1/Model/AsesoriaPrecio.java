package com.example.helpme_app_v1.Model;

import java.io.Serializable;

public class AsesoriaPrecio implements Serializable {
    private int idAsesoriaPrecio;
    private String tipoasesoria;
    private String url;
    private int asesor;
    private int tokens;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private int duracion;

    public int getIdAsesoriaPrecio() {
        return idAsesoriaPrecio;
    }

    public void setIdAsesoriaPrecio(int idAsesoriaPrecio) {
        this.idAsesoriaPrecio = idAsesoriaPrecio;
    }

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
