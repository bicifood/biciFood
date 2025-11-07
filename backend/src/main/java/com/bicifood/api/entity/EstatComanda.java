package com.bicifood.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

/**
 * Entitat que representa l'estat d'una comanda en el sistema BiciFood
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
@Entity
@Table(name = "estat_comanda")
public class EstatComanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estat")
    private Integer id;

    @NotBlank(message = "El nom de l'estat és obligatori")
    @Size(max = 50, message = "El nom de l'estat no pot superar els 50 caràcters")
    @Column(name = "nom_estat", nullable = false, unique = true, length = 50)
    private String nom;

    @OneToMany(mappedBy = "estat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Comanda> comandes = new HashSet<>();

    // Constructors
    public EstatComanda() {}

    public EstatComanda(String nom) {
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

    public Set<Comanda> getComandes() {
        return comandes;
    }

    public void setComandes(Set<Comanda> comandes) {
        this.comandes = comandes;
    }

    // equals, hashCode i toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EstatComanda that)) return false;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "EstatComanda{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                '}';
    }
}