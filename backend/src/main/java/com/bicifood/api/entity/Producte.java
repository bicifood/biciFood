package com.bicifood.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Entitat que representa un producte en el sistema BiciFood
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
@Entity
@Table(name = "producte")
public class Producte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producte")
    private Integer id;

    @NotBlank(message = "El nom del producte és obligatori")
    @Size(max = 150, message = "El nom del producte no pot superar els 150 caràcters")
    @Column(name = "nom", nullable = false, unique = true, length = 150)
    private String nom;

    @NotNull(message = "El preu és obligatori")
    @DecimalMin(value = "0.01", message = "El preu ha de ser major que 0")
    @Digits(integer = 3, fraction = 2, message = "El preu ha de tenir màxim 3 dígits enters i 2 decimals")
    @Column(name = "preu", nullable = false, precision = 5, scale = 2)
    private BigDecimal preu;

    @Size(max = 255, message = "El path de la imatge no pot superar els 255 caràcters")
    @Column(name = "imatge_path", length = 255)
    private String imatgePath;

    @NotNull(message = "L'stock és obligatori")
    @Min(value = 0, message = "L'stock no pot ser negatiu")
    @Column(name = "stock", nullable = false, columnDefinition = "int default 0")
    private Integer stock = 0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @NotBlank(message = "La descripció és obligatòria")
    @Size(max = 500, message = "La descripció no pot superar els 500 caràcters")
    @Column(name = "descripcio", nullable = false, length = 500)
    private String descripcio;

    @OneToMany(mappedBy = "producte", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<LiniaComanda> liniesComanda = new HashSet<>();

    // Constructors
    public Producte() {}

    public Producte(String nom, BigDecimal preu, Categoria categoria, String descripcio) {
        this.nom = nom;
        this.preu = preu;
        this.categoria = categoria;
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public Set<LiniaComanda> getLiniesComanda() {
        return liniesComanda;
    }

    public void setLiniesComanda(Set<LiniaComanda> liniesComanda) {
        this.liniesComanda = liniesComanda;
    }

    // equals, hashCode i toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producte producte)) return false;
        return getId() != null && getId().equals(producte.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Producte{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", preu=" + preu +
                ", stock=" + stock +
                ", categoria=" + (categoria != null ? categoria.getNom() : "null") +
                '}';
    }
}