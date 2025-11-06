package com.bicifood.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO per a la transferència de dades de Categoria
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
public class CategoriaDto {

    private Integer id;

    @NotBlank(message = "El nom de la categoria és obligatori")
    @Size(max = 100, message = "El nom de la categoria no pot superar els 100 caràcters")
    private String nom;

    // Constructors
    public CategoriaDto() {}

    public CategoriaDto(String nom) {
        this.nom = nom;
    }

    // Getters i Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    // toString
    @Override
    public String toString() {
        return "CategoriaDto{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                '}';
    }
}