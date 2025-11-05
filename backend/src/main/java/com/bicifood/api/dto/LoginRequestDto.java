package com.bicifood.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO per l'autenticació d'usuaris
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
public class LoginRequestDto {

    @NotBlank(message = "L'email és obligatori")
    @Email(message = "Format d'email invàlid")
    private String email;

    @NotBlank(message = "La contrasenya és obligatòria")
    @Size(min = 6, message = "La contrasenya ha de tenir almenys 6 caràcters")
    private String password;

    // Constructors
    public LoginRequestDto() {}

    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters i Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequestDto{" +
                "email='" + email + '\'' +
                ", password='[PROTECTED]'" +
                '}';
    }
}