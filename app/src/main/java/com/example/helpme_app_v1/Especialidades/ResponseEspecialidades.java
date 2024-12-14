package com.example.helpme_app_v1.Especialidades;

import com.example.helpme_app_v1.Model.InteresesAcademic.ResponseInteresesAcademic;

import java.util.List;

public class ResponseEspecialidades {
    private int status;
    private String message;
    private List<Especialidades> data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<Especialidades> getData() {
        return data;
    }

    public static class Especialidades {
        private int idEspecialidad;
        private String nombreEspecialidad;

        public int getIdEspecialidad() {
            return idEspecialidad;
        }

        public String getNombreEspecialidad() {
            return nombreEspecialidad;
        }
    }
}
