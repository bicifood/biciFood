package com.bicifood.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Entitat que representa una línia de comanda en el sistema BiciFood
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
@Entity
@Table(name = "linia_comanda")
public class LiniaComanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_linia")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_comanda", nullable = false)
    private Comanda comanda;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producte", nullable = false)
    private Producte producte;

    @NotNull(message = "El preu unitari és obligatori")
    @DecimalMin(value = "0.01", message = "El preu unitari ha de ser major que 0")
    @Digits(integer = 3, fraction = 2, message = "El preu unitari ha de tenir màxim 3 dígits enters i 2 decimals")
    @Column(name = "preu_unitari", nullable = false, precision = 5, scale = 2)
    private BigDecimal preuUnitari;

    @NotNull(message = "La quantitat és obligatòria")
    @Min(value = 1, message = "La quantitat ha de ser almenys 1")
    @Column(name = "quantitat", nullable = false)
    private Integer quantitat;

    @NotNull(message = "El subtotal és obligatori")
    @DecimalMin(value = "0.01", message = "El subtotal ha de ser major que 0")
    @Digits(integer = 4, fraction = 2, message = "El subtotal ha de tenir màxim 4 dígits enters i 2 decimals")
    @Column(name = "subtotal", nullable = false, precision = 6, scale = 2)
    private BigDecimal subtotal;

    // Constructors
    public LiniaComanda() {}

    public LiniaComanda(Comanda comanda, Producte producte, BigDecimal preuUnitari, Integer quantitat) {
        this.comanda = comanda;
        this.producte = producte;
        this.preuUnitari = preuUnitari;
        this.quantitat = quantitat;
        this.subtotal = preuUnitari.multiply(BigDecimal.valueOf(quantitat));
    }

    // Getters i Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public BigDecimal getPreuUnitari() {
        return preuUnitari;
    }

    public void setPreuUnitari(BigDecimal preuUnitari) {
        this.preuUnitari = preuUnitari;
    }

    public Integer getQuantitat() {
        return quantitat;
    }

    public void setQuantitat(Integer quantitat) {
        this.quantitat = quantitat;
        // Recalcular subtotal quan canvia la quantitat
        if (this.preuUnitari != null) {
            this.subtotal = this.preuUnitari.multiply(BigDecimal.valueOf(quantitat));
        }
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    // Mètodes de utilitat
    public void calcularSubtotal() {
        if (this.preuUnitari != null && this.quantitat != null) {
            this.subtotal = this.preuUnitari.multiply(BigDecimal.valueOf(this.quantitat));
        }
    }

    // equals, hashCode i toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LiniaComanda that)) return false;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "LiniaComanda{" +
                "id=" + id +
                ", producte=" + (producte != null ? producte.getNom() : "null") +
                ", preuUnitari=" + preuUnitari +
                ", quantitat=" + quantitat +
                ", subtotal=" + subtotal +
                '}';
    }
}