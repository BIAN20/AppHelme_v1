package com.example.helpme_app_v1.Model.Estudiantes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EstudiantePerfilResponse {
    private EstudiantePerfilData data;
    private String message;
    private int status;

    public EstudiantePerfilResponse(EstudiantePerfilData data, String message, int status) {
        this.data = data;
        this.message = message;
        this.status = status;
    }

    public EstudiantePerfilResponse() {
    }

    // Getter y Setter para 'data'
    public EstudiantePerfilData getData() {
        return data;
    }

    public void setData(EstudiantePerfilData data) {
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
    public static class EstudiantePerfilData {
        private String nombre;
        private String apellidos;
        private String dni;
        private String carrera;
        private String universidad;

        private String saldo;
        private int tokens;
        @SerializedName("intereses_academicos")
        private List<String> interesesAcademicos;
        public EstudiantePerfilData(String nombre, String apellidos, String dni, String carrera, String universidad, List<String> interesesAcademicos, String saldo, int tokens) {
            this.nombre = nombre;
            this.apellidos = apellidos;
            this.dni = dni;
            this.carrera = carrera;
            this.universidad = universidad;
            this.interesesAcademicos = interesesAcademicos;
            this.saldo = saldo;
            this.tokens = tokens;
        }

        public EstudiantePerfilData() {
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

        // Getters y Setters para los campos
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

        public String getDni() {
            return dni;
        }

        public void setDni(String dni) {
            this.dni = dni;
        }

        public String getCarrera() {
            return carrera;
        }

        public void setCarrera(String carrera) {
            this.carrera = carrera;
        }

        public String getUniversidad() {
            return universidad;
        }

        public void setUniversidad(String universidad) {
            this.universidad = universidad;
        }

        public List<String> getInteresesAcademicos() {
            return interesesAcademicos;
        }

        public void setInteresesAcademicos(List<String> interesesAcademicos) {
            this.interesesAcademicos = interesesAcademicos;
        }
    }
}
