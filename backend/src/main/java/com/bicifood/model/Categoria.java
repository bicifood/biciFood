package com.bicifood.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entitat JPA per la taula categoria
 * Representa les categories de productes del sistema BiciFood
 */
@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoria_id")
    private Long id;

    @NotBlank(message = "El nom de la categoria és obligatori")
    @Size(max = 100, message = "El nom no pot superar els 100 caràcters")
    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Size(max = 255, message = "La descripció no pot superar els 255 caràcters")
    @Column(name = "descripcio", length = 255)
    private String descripcio;

    @Column(name = "data_creacio", nullable = false)
    private LocalDateTime dataCreacio;

    @Column(name = "data_modificacio")
    private LocalDateTime dataModificacio;

    @Column(name = "activa", nullable = false)
    private Boolean activa = true;

    // Relació One-to-Many amb Producte
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Producte> productes = new ArrayList<>();

    // Constructors
    public Categoria() {
        this.dataCreacio = LocalDateTime.now();
        this.activa = true;
    }

    public Categoria(String nom, String descripcio) {
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

    // Mètodes de conveniència
    public void addProducte(Producte producte) {
        productes.add(producte);
        producte.setCategoria(this);
    }

    public void removeProducte(Producte producte) {
        productes.remove(producte);
        producte.setCategoria(null);
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

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public List<Producte> getProductes() {
        return productes;
    }

    public void setProductes(List<Producte> productes) {
        this.productes = productes;
    }

    // equals, hashCode i toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return Objects.equals(id, categoria.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", descripcio='" + descripcio + '\'' +
                ", activa=" + activa +
                ", dataCreacio=" + dataCreacio +
                '}';
    }
}