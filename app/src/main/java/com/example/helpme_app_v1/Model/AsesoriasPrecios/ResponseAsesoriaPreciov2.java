package com.example.helpme_app_v1.Model.AsesoriasPrecios;

import com.example.helpme_app_v1.Model.AsesoriaPrecio;

import java.util.List;

public class ResponseAsesoriaPreciov2 {

    private Object data;
    private String message;
    private int status;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
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
}
