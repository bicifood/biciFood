package com.bicifood.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

/**
 * Entitat que representa una categoria de productes en el sistema BiciFood
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer id;

    @NotBlank(message = "El nom de la categoria és obligatori")
    @Size(max = 100, message = "El nom de la categoria no pot superar els 100 caràcters")
    @Column(name = "nom_cat", nullable = false, unique = true, length = 100)
    private String nom;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Producte> productes = new HashSet<>();

    // Constructors
    public Categoria() {}

    public Categoria(String nom) {
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

    public Set<Producte> getProductes() {
        return productes;
    }

    public void setProductes(Set<Producte> productes) {
        this.productes = productes;
    }

    // equals, hashCode i toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria categoria)) return false;
        return getId() != null && getId().equals(categoria.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                '}';
    }
}