package com.example.helpme_app_v1.Model.tokens;

public class ResponseToken {
    private TokensRequest data;
    private String message;
    private int status;

    public TokensRequest getData() {
        return data;
    }

    public void setData(TokensRequest data) {
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
