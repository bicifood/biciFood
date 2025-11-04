package com.bicifood.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * 🏷️ Entitat Categoria
 * Representa les categories de productes: CARNS, PEIXOS, AMANIDES, etc.
 */
@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    @NotBlank(message = "El nom de la categoria és obligatori")
    @Size(max = 100, message = "El nom de la categoria no pot superar els 100 caràcters")
    @Column(name = "nom_cat", unique = true, nullable = false)
    private String nomCat;

    @JsonIgnore
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<Producte> productes;

    // Constructors
    public Categoria() {}

    public Categoria(String nomCat) {
        this.nomCat = nomCat;
    }

    // Getters i Setters
    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNomCat() {
        return nomCat;
    }

    public void setNomCat(String nomCat) {
        this.nomCat = nomCat;
    }

    public List<Producte> getProductes() {
        return productes;
    }

    public void setProductes(List<Producte> productes) {
        this.productes = productes;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "idCategoria=" + idCategoria +
                ", nomCat='" + nomCat + '\'' +
                '}';
    }
}