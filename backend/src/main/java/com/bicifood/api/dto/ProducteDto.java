package com.bicifood.api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * DTO per crear o actualitzar productes
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
public class ProducteDto {

    private Integer id;

    @NotBlank(message = "El nom del producte és obligatori")
    @Size(max = 150, message = "El nom del producte no pot superar els 150 caràcters")
    private String nom;

    @NotNull(message = "El preu és obligatori")
    @DecimalMin(value = "0.01", message = "El preu ha de ser major que 0")
    @Digits(integer = 3, fraction = 2, message = "El preu ha de tenir màxim 3 dígits enters i 2 decimals")
    private BigDecimal preu;

    @Size(max = 255, message = "El path de la imatge no pot superar els 255 caràcters")
    private String imatgePath;

    @NotNull(message = "L'stock és obligatori")
    @Min(value = 0, message = "L'stock no pot ser negatiu")
    private Integer stock;

    @NotNull(message = "La categoria és obligatòria")
    private Integer categoriaId;

    private String categoriaNom;

    @NotBlank(message = "La descripció és obligatòria")
    @Size(max = 500, message = "La descripció no pot superar els 500 caràcters")
    private String descripcio;

    // Constructors
    public ProducteDto() {}

    public ProducteDto(String nom, BigDecimal preu, Integer stock, Integer categoriaId, String descripcio) {
        this.nom = nom;
        this.preu = preu;
        this.stock = stock;
        this.categoriaId = categoriaId;
        this.descripcio = descripcio;
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

    public BigDecimal getPreu() {
        return preu;
    }

    public void setPreu(BigDecimal preu) {
        this.preu = preu;
    }

    public String getImatgePath() {
        return imatgePath;
    }

    public void setImatgePath(String imatgePath) {
        this.imatgePath = imatgePath;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoriaNom() {
        return categoriaNom;
    }

    public void setCategoriaNom(String categoriaNom) {
        this.categoriaNom = categoriaNom;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    @Override
    public String toString() {
        return "ProducteDto{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", preu=" + preu +
                ", stock=" + stock +
                ", categoriaId=" + categoriaId +
                ", categoriaNom='" + categoriaNom + '\'' +
                '}';
    }
}