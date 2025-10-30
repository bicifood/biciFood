package com.bicifood.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entitat JPA per la taula metode_pagament
 * Representa els mètodes de pagament disponibles al sistema
 */
@Entity
@Table(name = "metode_pagament")
public class MetodePagament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metode_id")
    private Long id;

    @NotBlank(message = "El nom del mètode de pagament és obligatori")
    @Size(max = 50, message = "El nom no pot superar els 50 caràcters")
    @Column(name = "nom", nullable = false, length = 50)
    private String nom;

    @Size(max = 255, message = "La descripció no pot superar els 255 caràcters")
    @Column(name = "descripcio", length = 255)
    private String descripcio;

    @Column(name = "actiu", nullable = false)
    private Boolean actiu = true;

    @Column(name = "data_creacio", nullable = false)
    private LocalDateTime dataCreacio;

    @Column(name = "data_modificacio")
    private LocalDateTime dataModificacio;

    // Relació One-to-Many amb Comanda
    @OneToMany(mappedBy = "metodePagament", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Comanda> comandes = new ArrayList<>();

    // Constructors
    public MetodePagament() {
        this.dataCreacio = LocalDateTime.now();
        this.actiu = true;
    }

    public MetodePagament(String nom, String descripcio) {
        this();
        this.nom = nom;
        this.descripcio = descripcio;
    }

    // Mètodes JPA Lifecycle
    @PrePersist
    protected void onCreate() {
        this.dataCreacio = LocalDateTime.now();
        this.dataModificacio = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dataModificacio = LocalDateTime.now();
    }

    // Getters i Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public Boolean getActiu() {
        return actiu;
    }

    public void setActiu(Boolean actiu) {
        this.actiu = actiu;
    }

    public LocalDateTime getDataCreacio() {
        return dataCreacio;
    }

    public void setDataCreacio(LocalDateTime dataCreacio) {
        this.dataCreacio = dataCreacio;
    }

    public LocalDateTime getDataModificacio() {
        return dataModificacio;
    }

    public void setDataModificacio(LocalDateTime dataModificacio) {
        this.dataModificacio = dataModificacio;
    }

    public List<Comanda> getComandes() {
        return comandes;
    }

    public void setComandes(List<Comanda> comandes) {
        this.comandes = comandes;
    }

    // equals, hashCode i toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetodePagament that = (MetodePagament) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MetodePagament{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", descripcio='" + descripcio + '\'' +
                ", actiu=" + actiu +
                '}';
    }
}