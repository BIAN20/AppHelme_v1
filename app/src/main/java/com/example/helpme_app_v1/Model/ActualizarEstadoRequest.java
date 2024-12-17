package com.example.helpme_app_v1.Model;
public class ActualizarEstadoRequest {
    private String estado;

    public ActualizarEstadoRequest(String estado) {
        this.estado = estado;
    }

    // Getters and setters
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
