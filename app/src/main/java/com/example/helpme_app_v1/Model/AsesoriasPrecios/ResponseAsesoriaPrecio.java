package com.example.helpme_app_v1.Model.AsesoriasPrecios;

public class ResponseAsesoriaPrecio {
    private Object data;
    private String menssage;
    private int status;

    public ResponseAsesoriaPrecio(Object data, String menssage, int status) {
        this.data = data;
        this.menssage = menssage;
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMenssage() {
        return menssage;
    }

    public void setMenssage(String menssage) {
        this.menssage = menssage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
