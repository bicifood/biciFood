package com.bicifood.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entitat JPA per la taula detall_comanda
 * Representa els detalls de cada producte dins una comanda
 */
@Entity
@Table(name = "detall_comanda")
public class DetallComanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detall_id")
    private Long id;

    @NotNull(message = "La quantitat és obligatòria")
    @Min(value = 1, message = "La quantitat ha de ser com a mínim 1")
    @Column(name = "quantitat", nullable = false)
    private Integer quantitat;

    @NotNull(message = "El preu unitari és obligatori")
    @DecimalMin(value = "0.0", inclusive = false, message = "El preu unitari ha de ser positiu")
    @Digits(integer = 8, fraction = 2, message = "El preu unitari no pot tenir més de 2 decimals")
    @Column(name = "preu_unitari", nullable = false, precision = 10, scale = 2)
    private BigDecimal preuUnitari;

    @NotNull(message = "El preu total és obligatori")
    @DecimalMin(value = "0.0", inclusive = false, message = "El preu total ha de ser positiu")
    @Digits(integer = 10, fraction = 2, message = "El preu total no pot tenir més de 2 decimals")
    @Column(name = "preu_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal preuTotal;

    @Size(max = 255, message = "Les notes no poden superar els 255 caràcters")
    @Column(name = "notes", length = 255)
    private String notes;

    // Relació Many-to-One amb Comanda
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comanda_id", nullable = false)
    @JsonBackReference
    private Comanda comanda;

    // Relació Many-to-One amb Producte
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producte_id", nullable = false)
    @JsonBackReference
    private Producte producte;

    // Constructors
    public DetallComanda() {
    }

    public DetallComanda(Integer quantitat, BigDecimal preuUnitari, Comanda comanda, Producte producte) {
        this.quantitat = quantitat;
        this.preuUnitari = preuUnitari;
        this.comanda = comanda;
        this.producte = producte;
        calculatePreuTotal();
    }

    // Mètodes JPA Lifecycle
    @PrePersist
    @PreUpdate
    protected void calculatePreuTotal() {
        if (this.quantitat != null && this.preuUnitari != null) {
            this.preuTotal = this.preuUnitari.multiply(BigDecimal.valueOf(this.quantitat));
        }
    }

    // Mètodes de negoci
    public void updateQuantitat(Integer novaQuantitat) {
        this.quantitat = novaQuantitat;
        calculatePreuTotal();
    }

    // Getters i Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantitat() {
        return quantitat;
    }

    public void setQuantitat(Integer quantitat) {
        this.quantitat = quantitat;
        calculatePreuTotal();
    }

    public BigDecimal getPreuUnitari() {
        return preuUnitari;
    }

    public void setPreuUnitari(BigDecimal preuUnitari) {
        this.preuUnitari = preuUnitari;
        calculatePreuTotal();
    }

    public BigDecimal getPreuTotal() {
        return preuTotal;
    }

    public void setPreuTotal(BigDecimal preuTotal) {
        this.preuTotal = preuTotal;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Comanda getComanda() {
        return comanda;
    }

    public void setComanda(Comanda comanda) {
        this.comanda = comanda;
    }

    public Producte getProducte() {
        return producte;
    }

    public void setProducte(Producte producte) {
        this.producte = producte;
    }

    // equals, hashCode i toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetallComanda that = (DetallComanda) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DetallComanda{" +
                "id=" + id +
                ", quantitat=" + quantitat +
                ", preuUnitari=" + preuUnitari +
                ", preuTotal=" + preuTotal +
                '}';
    }
}