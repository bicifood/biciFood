package com.bicifood.api.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entitat que representa un lliurament en el sistema BiciFood
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
@Entity
@Table(name = "lliurament")
public class Lliurament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lliurament")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_comanda", nullable = false, unique = true)
    private Comanda comanda;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_repartidor")
    private Usuari repartidor;

    @Column(name = "data_hora_assignacio", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dataHoraAssignacio;

    @Column(name = "data_hora_lliurament_real")
    private LocalDateTime dataHoraLliuramentReal;

    // Constructors
    public Lliurament() {
        this.dataHoraAssignacio = LocalDateTime.now();
    }

    public Lliurament(Comanda comanda) {
        this();
        this.comanda = comanda;
    }

    public Lliurament(Comanda comanda, Usuari repartidor) {
        this(comanda);
        this.repartidor = repartidor;
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

    public Usuari getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(Usuari repartidor) {
        this.repartidor = repartidor;
    }

    public LocalDateTime getDataHoraAssignacio() {
        return dataHoraAssignacio;
    }

    public void setDataHoraAssignacio(LocalDateTime dataHoraAssignacio) {
        this.dataHoraAssignacio = dataHoraAssignacio;
    }

    public LocalDateTime getDataHoraLliuramentReal() {
        return dataHoraLliuramentReal;
    }

    public void setDataHoraLliuramentReal(LocalDateTime dataHoraLliuramentReal) {
        this.dataHoraLliuramentReal = dataHoraLliuramentReal;
    }

    // Mètodes de utilitat
    public boolean isLliurat() {
        return this.dataHoraLliuramentReal != null;
    }

    public void marcarComLliurat() {
        this.dataHoraLliuramentReal = LocalDateTime.now();
    }

    // equals, hashCode i toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lliurament that)) return false;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Lliurament{" +
                "id=" + id +
                ", comanda=" + (comanda != null ? comanda.getId() : "null") +
                ", repartidor=" + (repartidor != null ? repartidor.getEmail() : "null") +
                ", dataHoraAssignacio=" + dataHoraAssignacio +
                ", dataHoraLliuramentReal=" + dataHoraLliuramentReal +
                '}';
    }
}