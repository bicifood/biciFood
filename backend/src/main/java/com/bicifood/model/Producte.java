package com.bicifood.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entitat JPA per la taula producte
 * Representa els productes disponibles al sistema BiciFood
 */
@Entity
@Table(name = "producte")
@NamedQueries({
    @NamedQuery(
        name = "Producte.findByCategoria", 
        query = "SELECT p FROM Producte p WHERE p.categoria.id = :categoriaId AND p.stock > 0"
    ),
    @NamedQuery(
        name = "Producte.findAvailable", 
        query = "SELECT p FROM Producte p WHERE p.stock > 0 ORDER BY p.nom"
    )
})
public class Producte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producte_id")
    private Long id;

    @NotBlank(message = "El nom del producte és obligatori")
    @Size(max = 150, message = "El nom no pot superar els 150 caràcters")
    @Column(name = "nom", nullable = false, length = 150)
    private String nom;

    @Size(max = 500, message = "La descripció no pot superar els 500 caràcters")
    @Column(name = "descripcio", length = 500)
    private String descripcio;

    @NotNull(message = "El preu és obligatori")
    @DecimalMin(value = "0.0", inclusive = false, message = "El preu ha de ser positiu")
    @Digits(integer = 8, fraction = 2, message = "El preu no pot tenir més de 2 decimals")
    @Column(name = "preu", nullable = false, precision = 10, scale = 2)
    private BigDecimal preu;

    @Min(value = 0, message = "L'stock no pot ser negatiu")
    @Column(name = "stock", nullable = false)
    private Integer stock = 0;

    @Size(max = 255, message = "La URL de la imatge no pot superar els 255 caràcters")
    @Column(name = "imatge_url", length = 255)
    private String imatgeUrl;

    @Column(name = "data_creacio", nullable = false)
    private LocalDateTime dataCreacio;

    @Column(name = "data_modificacio")
    private LocalDateTime dataModificacio;

    @Column(name = "actiu", nullable = false)
    private Boolean actiu = true;

    @Column(name = "destacat")
    private Boolean destacat = false;

    @Column(name = "calories")
    private Integer calories;

    @Size(max = 100, message = "Les al·lèrgies no poden superar els 100 caràcters")
    @Column(name = "allergies", length = 100)
    private String allergies;

    // Relació Many-to-One amb Categoria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    @JsonBackReference
    private Categoria categoria;

    // Relació One-to-Many amb DetallComanda
    @OneToMany(mappedBy = "producte", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<DetallComanda> detallsComanda = new ArrayList<>();

    // Constructors
    public Producte() {
        this.dataCreacio = LocalDateTime.now();
        this.actiu = true;
        this.destacat = false;
        this.stock = 0;
    }

    public Producte(String nom, String descripcio, BigDecimal preu, Integer stock, Categoria categoria) {
        this();
        this.nom = nom;
        this.descripcio = descripcio;
        this.preu = preu;
        this.stock = stock;
        this.categoria = categoria;
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

    // Mètodes de negoci
    public boolean isAvailable() {
        return this.actiu && this.stock > 0;
    }

    public void reduirStock(Integer quantitat) {
        if (this.stock >= quantitat) {
            this.stock -= quantitat;
        } else {
            throw new IllegalArgumentException("Stock insuficient. Stock actual: " + this.stock);
        }
    }

    public void augmentarStock(Integer quantitat) {
        this.stock += quantitat;
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

    public BigDecimal getPreu() {
        return preu;
    }

    public void setPreu(BigDecimal preu) {
        this.preu = preu;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImatgeUrl() {
        return imatgeUrl;
    }

    public void setImatgeUrl(String imatgeUrl) {
        this.imatgeUrl = imatgeUrl;
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

    public Boolean getActiu() {
        return actiu;
    }

    public void setActiu(Boolean actiu) {
        this.actiu = actiu;
    }

    public Boolean getDestacat() {
        return destacat;
    }

    public void setDestacat(Boolean destacat) {
        this.destacat = destacat;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<DetallComanda> getDetallsComanda() {
        return detallsComanda;
    }

    public void setDetallsComanda(List<DetallComanda> detallsComanda) {
        this.detallsComanda = detallsComanda;
    }

    // equals, hashCode i toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producte producte = (Producte) o;
        return Objects.equals(id, producte.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Producte{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", preu=" + preu +
                ", stock=" + stock +
                ", actiu=" + actiu +
                '}';
    }
}