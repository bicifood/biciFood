package com.bicifood.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

/**
 * Entitat que representa un rol d'usuari en el sistema BiciFood
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
@Entity
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer id;

    @NotBlank(message = "El nom del rol és obligatori")
    @Size(max = 50, message = "El nom del rol no pot superar els 50 caràcters")
    @Column(name = "nom_rol", nullable = false, unique = true, length = 50)
    private String nom;

    @OneToMany(mappedBy = "rol", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Usuari> usuaris = new HashSet<>();

    // Constructors
    public Rol() {}

    public Rol(String nom) {
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

    public Set<Usuari> getUsuaris() {
        return usuaris;
    }

    public void setUsuaris(Set<Usuari> usuaris) {
        this.usuaris = usuaris;
    }

    // equals, hashCode i toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rol rol)) return false;
        return getId() != null && getId().equals(rol.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Rol{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                '}';
    }
}