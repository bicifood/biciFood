package com.bicifood.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * 🍽️ Entitat Producte  
 * Representa els productes del menú: plats, begudes, postres, etc.
 */
@Entity
@Table(name = "producte")
public class Producte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producte")
    private Integer idProducte;

    @NotBlank(message = "El nom del producte és obligatori")
    @Size(max = 150, message = "El nom no pot superar els 150 caràcters")
    @Column(name = "nom", unique = true, nullable = false)
    private String nom;

    @NotNull(message = "El preu és obligatori")
    @DecimalMin(value = "0.0", inclusive = false, message = "El preu ha de ser positiu")
    @Column(name = "preu", precision = 5, scale = 2, nullable = false)
    private BigDecimal preu;

    @Size(max = 255, message = "La ruta de la imatge no pot superar els 255 caràcters")
    @Column(name = "imatge_path")
    private String imatgePath;

    @Min(value = 0, message = "L'stock no pot ser negatiu")
    @Column(name = "stock", nullable = false)
    private Integer stock = 0;

    @NotBlank(message = "La descripció és obligatòria")
    @Size(max = 500, message = "La descripció no pot superar els 500 caràcters")
    @Column(name = "descripcio", nullable = false)
    private String descripcio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    // Constructors
    public Producte() {}

    public Producte(String nom, BigDecimal preu, String descripcio, Categoria categoria) {
        this.nom = nom;
        this.preu = preu;
        this.descripcio = descripcio;
        this.categoria = categoria;
    }

    // Getters i Setters
    public Integer getIdProducte() {
        return idProducte;
    }

    public void setIdProducte(Integer idProducte) {
        this.idProducte = idProducte;
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

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Producte{" +
                "idProducte=" + idProducte +
                ", nom='" + nom + '\'' +
                ", preu=" + preu +
                ", stock=" + stock +
                '}';
    }
}