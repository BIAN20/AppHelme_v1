package com.example.helpme_app_v1.Model.Asesorias;

import java.util.List;

public class ResponseAsesoriaFiltre {

    private Data data;
    private String message;
    private int status;

    // Constructor
    public ResponseAsesoriaFiltre(Data data, String message, int status) {
        this.data = data;
        this.message = message;
        this.status = status;
    }

    // Getters y Setters
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    // Clase interna para representar los detalles de la asesoría
    public static class Data {
        private List<Asesoria> asesorias;  // Lista de asesorías
        private int idAsesor;

        // Constructor
        public Data(List<Asesoria> asesorias, int idAsesor) {
            this.asesorias = asesorias;
            this.idAsesor = idAsesor;
        }

        // Getters y Setters
        public List<Asesoria> getAsesorias() {
            return asesorias;
        }

        public void setAsesorias(List<Asesoria> asesorias) {
            this.asesorias = asesorias;
        }

        public int getIdAsesor() {
            return idAsesor;
        }

        public void setIdAsesor(int idAsesor) {
            this.idAsesor = idAsesor;
        }
    }

    // Clase interna para representar cada asesoría
    public static class Asesoria {
        private int idAsesoria;
        private int estudiante;
        private String fecha;
        private String horainicio;
        private String horafinal;
        private int duracion;
        private String url;
        private String estado;
        private int idAsesoriaPrecio;
        private String tipoasesoria;
        private int tokens;
        private int duracion_precio;
        private String url_imagen;

        // Constructor
        public Asesoria(int idAsesoria, int estudiante, String fecha, String horainicio, String horafinal,
                        int duracion, String url, String estado, int idAsesoriaPrecio, String tipoasesoria,
                        int tokens, int duracion_precio, String url_imagen) {
            this.idAsesoria = idAsesoria;
            this.estudiante = estudiante;
            this.fecha = fecha;
            this.horainicio = horainicio;
            this.horafinal = horafinal;
            this.duracion = duracion;
            this.url = url;
            this.estado = estado;
            this.idAsesoriaPrecio = idAsesoriaPrecio;
            this.tipoasesoria = tipoasesoria;
            this.tokens = tokens;
            this.duracion_precio = duracion_precio;
            this.url_imagen = url_imagen;
        }

        // Getters y Setters
        public int getIdAsesoria() {
            return idAsesoria;
        }

        public void setIdAsesoria(int idAsesoria) {
            this.idAsesoria = idAsesoria;
        }

        public int getEstudiante() {
            return estudiante;
        }

        public void setEstudiante(int estudiante) {
            this.estudiante = estudiante;
        }

        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

        public String getHorainicio() {
            return horainicio;
        }

        public void setHorainicio(String horainicio) {
            this.horainicio = horainicio;
        }

        public String getHorafinal() {
            return horafinal;
        }

        public void setHorafinal(String horafinal) {
            this.horafinal = horafinal;
        }

        public int getDuracion() {
            return duracion;
        }

        public void setDuracion(int duracion) {
            this.duracion = duracion;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

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

        public int getTokens() {
            return tokens;
        }

        public void setTokens(int tokens) {
            this.tokens = tokens;
        }

        public int getDuracion_precio() {
            return duracion_precio;
        }

        public void setDuracion_precio(int duracion_precio) {
            this.duracion_precio = duracion_precio;
        }

        public String getUrl_imagen() {
            return url_imagen;
        }

        public void setUrl_imagen(String url_imagen) {
            this.url_imagen = url_imagen;
        }
    }
}
