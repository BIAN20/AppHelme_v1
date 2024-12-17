package com.example.helpme_app_v1.Model.tokens;

public class TokensRequest {
    private int idUsuario;
    private int tokens;
    private double saldo;

    // Constructor
    public TokensRequest(int idUsuario, int tokens, double saldo) {
        this.idUsuario = idUsuario;
        this.tokens = tokens;
        this.saldo = saldo;
    }

    public TokensRequest() {

    }

    // Getters
    public int getIdUsuario() {
        return idUsuario;
    }

    public int getTokens() {
        return tokens;
    }

    public double getSaldo() {
        return saldo;
    }

    // Setters
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "TokensRequest{" +
                "idUsuario=" + idUsuario +
                ", tokens='" + tokens + '\'' +
                ", saldo=" + saldo +
                '}';
    }
}
