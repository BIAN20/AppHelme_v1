package com.example.helpme_app_v1.Model.tokens;

public class ResponseToken {
    private TokensRequest data;
    private String menssage;
    private int status;

    public TokensRequest getData() {
        return data;
    }

    public void setData(TokensRequest data) {
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
