package com.bicifood.api.dto;

/**
 * DTO per la resposta d'autenticació
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
public class LoginResponseDto {

    private String token;
    private String type = "Bearer";
    private Integer userId;
    private String email;
    private String nomComplet;
    private String rol;

    // Constructors
    public LoginResponseDto() {}

    public LoginResponseDto(String token, Integer userId, String email, String nomComplet, String rol) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.nomComplet = nomComplet;
        this.rol = rol;
    }

    // Getters i Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "LoginResponseDto{" +
                "type='" + type + '\'' +
                ", userId=" + userId +
                ", email='" + email + '\'' +
                ", nomComplet='" + nomComplet + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}