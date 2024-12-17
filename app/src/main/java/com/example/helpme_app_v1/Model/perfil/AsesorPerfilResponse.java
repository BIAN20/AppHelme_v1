package com.example.helpme_app_v1.Model.Asesores;

import java.util.List;

public class AsesorPerfilResponse {
    private AsesorPerfilData data;
    private String message;
    private int status;

    public AsesorPerfilResponse(AsesorPerfilData data, String message, int status) {
        this.data = data;
        this.message = message;
        this.status = status;
    }

    public AsesorPerfilResponse() {
    }

    // Getter y Setter para 'data'
    public AsesorPerfilData getData() {
        return data;
    }

    public void setData(AsesorPerfilData data) {
        this.data = data;
    }

    // Getter y Setter para 'message'
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Getter y Setter para 'status'
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    // Clase interna para manejar los datos dentro de 'data'
    public static class AsesorPerfilData {
        private String presentacion;
        private int aniosExperiencia;
        private String nombre;
        private String apellidos;
        private List<String> especialidades;
        private String saldo;

        public String getEnsenanzaPreferida() {
            return ensenanzaPreferida;
        }

        public void setEnsenanzaPreferida(String ensenanzaPreferida) {
            this.ensenanzaPreferida = ensenanzaPreferida;
        }

        public String getCodigoColegiatura() {
            return codigoColegiatura;
        }

        public void setCodigoColegiatura(String codigoColegiatura) {
            this.codigoColegiatura = codigoColegiatura;
        }

        private int tokens;
        private String ensenanzaPreferida;
        private String codigoColegiatura;

        public AsesorPerfilData(String presentacion, int aniosExperiencia, String nombre, String apellidos,
                                List<String> especialidades, String saldo, int tokens) {
            this.presentacion = presentacion;
            this.aniosExperiencia = aniosExperiencia;
            this.nombre = nombre;
            this.apellidos = apellidos;
            this.especialidades = especialidades;
            this.saldo = saldo;
            this.tokens = tokens;
        }

        public AsesorPerfilData() {
        }

        // Getters y Setters para los campos
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

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getApellidos() {
            return apellidos;
        }

        public void setApellidos(String apellidos) {
            this.apellidos = apellidos;
        }

        public List<String> getEspecialidades() {
            return especialidades;
        }

        public void setEspecialidades(List<String> especialidades) {
            this.especialidades = especialidades;
        }

        public String getSaldo() {
            return saldo;
        }

        public void setSaldo(String saldo) {
            this.saldo = saldo;
        }

        public int getTokens() {
            return tokens;
        }

        public void setTokens(int tokens) {
            this.tokens = tokens;
        }
    }
}
