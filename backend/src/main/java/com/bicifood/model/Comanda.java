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
 * Entitat JPA per la taula comanda
 * Representa les comandes realitzades pels usuaris
 */
@Entity
@Table(name = "comanda")
public class Comanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comanda_id")
    private Long id;

    @NotNull(message = "La data de la comanda és obligatòria")
    @Column(name = "data_comanda", nullable = false)
    private LocalDateTime dataComanda;

    @NotNull(message = "El preu total és obligatori")
    @DecimalMin(value = "0.0", inclusive = false, message = "El preu total ha de ser positiu")
    @Digits(integer = 10, fraction = 2, message = "El preu total no pot tenir més de 2 decimals")
    @Column(name = "preu_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal preuTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "estat", nullable = false)
    private EstatComanda estat = EstatComanda.PENDENT;

    @Size(max = 255, message = "L'adreça d'enviament no pot superar els 255 caràcters")
    @Column(name = "adreca_enviament", length = 255)
    private String adrecaEnviament;

    @Pattern(regexp = "^[+]?[0-9]{9,15}$", message = "El telèfon de contacte ha de tenir un format vàlid")
    @Column(name = "telefon_contacte", length = 20)
    private String telefonContacte;

    @Size(max = 500, message = "Les notes no poden superar els 500 caràcters")
    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "data_entrega_estimada")
    private LocalDateTime dataEntregaEstimada;

    @Column(name = "data_entrega_real")
    private LocalDateTime dataEntregaReal;

    @Column(name = "data_modificacio")
    private LocalDateTime dataModificacio;

    // Relació Many-to-One amb Usuari
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuari_id", nullable = false)
    @JsonBackReference
    private Usuari usuari;

    // Relació Many-to-One amb MetodePagament
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metode_pagament_id")
    @JsonBackReference
    private MetodePagament metodePagament;

    // Relació One-to-Many amb DetallComanda
    @OneToMany(mappedBy = "comanda", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private List<DetallComanda> detalls = new ArrayList<>();

    // Constructors
    public Comanda() {
        this.dataComanda = LocalDateTime.now();
        this.estat = EstatComanda.PENDENT;
    }

    public Comanda(Usuari usuari, BigDecimal preuTotal) {
        this();
        this.usuari = usuari;
        this.preuTotal = preuTotal;
    }

    // Mètodes JPA Lifecycle
    @PrePersist
    protected void onCreate() {
        this.dataComanda = LocalDateTime.now();
        this.dataModificacio = LocalDateTime.now();
        // Estimem l'entrega a 45 minuts
        this.dataEntregaEstimada = LocalDateTime.now().plusMinutes(45);
    }

    @PreUpdate
    protected void onUpdate() {
        this.dataModificacio = LocalDateTime.now();
    }

    // Mètodes de negoci
    public void addDetall(DetallComanda detall) {
        detalls.add(detall);
        detall.setComanda(this);
        recalcularPreuTotal();
    }

    public void removeDetall(DetallComanda detall) {
        detalls.remove(detall);
        detall.setComanda(null);
        recalcularPreuTotal();
    }

    public void recalcularPreuTotal() {
        this.preuTotal = detalls.stream()
                .map(DetallComanda::getPreuTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean potCancelar() {
        return EstatComanda.PENDENT.equals(this.estat) || 
               EstatComanda.CONFIRMADA.equals(this.estat);
    }

    public void confirmar() {
        if (EstatComanda.PENDENT.equals(this.estat)) {
            this.estat = EstatComanda.CONFIRMADA;
        } else {
            throw new IllegalStateException("La comanda no es pot confirmar en l'estat actual: " + this.estat);
        }
    }

    public void marcarEnPreparacio() {
        if (EstatComanda.CONFIRMADA.equals(this.estat)) {
            this.estat = EstatComanda.EN_PREPARACIO;
        } else {
            throw new IllegalStateException("La comanda no es pot marcar en preparació en l'estat actual: " + this.estat);
        }
    }

    public void marcarEnCami() {
        if (EstatComanda.EN_PREPARACIO.equals(this.estat)) {
            this.estat = EstatComanda.LLESTA;
        } else {
            throw new IllegalStateException("La comanda no es pot marcar en camí en l'estat actual: " + this.estat);
        }
    }

    public void marcarEntregada() {
        if (EstatComanda.LLESTA.equals(this.estat)) {
            this.estat = EstatComanda.ENTREGADA;
            this.dataEntregaReal = LocalDateTime.now();
        } else {
            throw new IllegalStateException("La comanda no es pot marcar com entregada en l'estat actual: " + this.estat);
        }
    }

    public void cancelar() {
        if (potCancelar()) {
            this.estat = EstatComanda.CANCEL_LADA;
        } else {
            throw new IllegalStateException("La comanda no es pot cancel·lar en l'estat actual: " + this.estat);
        }
    }

    // Getters i Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataComanda() {
        return dataComanda;
    }

    public void setDataComanda(LocalDateTime dataComanda) {
        this.dataComanda = dataComanda;
    }

    public BigDecimal getPreuTotal() {
        return preuTotal;
    }

    public void setPreuTotal(BigDecimal preuTotal) {
        this.preuTotal = preuTotal;
    }

    public EstatComanda getEstat() {
        return estat;
    }

    public void setEstat(EstatComanda estat) {
        this.estat = estat;
    }

    public String getAdrecaEnviament() {
        return adrecaEnviament;
    }

    public void setAdrecaEnviament(String adrecaEnviament) {
        this.adrecaEnviament = adrecaEnviament;
    }

    public String getTelefonContacte() {
        return telefonContacte;
    }

    public void setTelefonContacte(String telefonContacte) {
        this.telefonContacte = telefonContacte;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getDataEntregaEstimada() {
        return dataEntregaEstimada;
    }

    public void setDataEntregaEstimada(LocalDateTime dataEntregaEstimada) {
        this.dataEntregaEstimada = dataEntregaEstimada;
    }

    public LocalDateTime getDataEntregaReal() {
        return dataEntregaReal;
    }

    public void setDataEntregaReal(LocalDateTime dataEntregaReal) {
        this.dataEntregaReal = dataEntregaReal;
    }

    public LocalDateTime getDataModificacio() {
        return dataModificacio;
    }

    public void setDataModificacio(LocalDateTime dataModificacio) {
        this.dataModificacio = dataModificacio;
    }

    public Usuari getUsuari() {
        return usuari;
    }

    public void setUsuari(Usuari usuari) {
        this.usuari = usuari;
    }

    public MetodePagament getMetodePagament() {
        return metodePagament;
    }

    public void setMetodePagament(MetodePagament metodePagament) {
        this.metodePagament = metodePagament;
    }

    public List<DetallComanda> getDetalls() {
        return detalls;
    }

    public void setDetalls(List<DetallComanda> detalls) {
        this.detalls = detalls;
    }

    // equals, hashCode i toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comanda comanda = (Comanda) o;
        return Objects.equals(id, comanda.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Comanda{" +
                "id=" + id +
                ", dataComanda=" + dataComanda +
                ", preuTotal=" + preuTotal +
                ", estat=" + estat +
                ", usuari=" + (usuari != null ? usuari.getId() : null) +
                '}';
    }
}