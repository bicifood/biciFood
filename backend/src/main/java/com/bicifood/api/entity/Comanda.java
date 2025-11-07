package com.bicifood.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entitat que representa una comanda en el sistema BiciFood
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
@Entity
@Table(name = "comanda")
public class Comanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comanda")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_client", nullable = false)
    private Usuari client;

    @Column(name = "data_hora_comanda", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dataHoraComanda;

    @NotNull(message = "L'import total és obligatori")
    @DecimalMin(value = "0.01", message = "L'import total ha de ser major que 0")
    @Digits(integer = 4, fraction = 2, message = "L'import total ha de tenir màxim 4 dígits enters i 2 decimals")
    @Column(name = "import_total", nullable = false, precision = 6, scale = 2)
    private BigDecimal importTotal;

    @NotBlank(message = "L'adreça de lliurament és obligatòria")
    @Size(max = 255, message = "L'adreça de lliurament no pot superar els 255 caràcters")
    @Column(name = "adreca_lliurament", nullable = false, length = 255)
    private String adrecaLliurament;

    @NotBlank(message = "El codi postal de lliurament és obligatori")
    @Size(max = 10, message = "El codi postal de lliurament no pot superar els 10 caràcters")
    @Column(name = "cp_lliurament", nullable = false, length = 10)
    private String cpLliurament;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_estat", nullable = false)
    private EstatComanda estat;

    @OneToMany(mappedBy = "comanda", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<LiniaComanda> liniesComanda = new HashSet<>();

    @OneToOne(mappedBy = "comanda", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Lliurament lliurament;

    // Constructors
    public Comanda() {
        this.dataHoraComanda = LocalDateTime.now();
    }

    public Comanda(Usuari client, BigDecimal importTotal, String adrecaLliurament, 
                   String cpLliurament, EstatComanda estat) {
        this();
        this.client = client;
        this.importTotal = importTotal;
        this.adrecaLliurament = adrecaLliurament;
        this.cpLliurament = cpLliurament;
        this.estat = estat;
    }

    // Getters i Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuari getClient() {
        return client;
    }

    public void setClient(Usuari client) {
        this.client = client;
    }

    public LocalDateTime getDataHoraComanda() {
        return dataHoraComanda;
    }

    public void setDataHoraComanda(LocalDateTime dataHoraComanda) {
        this.dataHoraComanda = dataHoraComanda;
    }

    public BigDecimal getImportTotal() {
        return importTotal;
    }

    public void setImportTotal(BigDecimal importTotal) {
        this.importTotal = importTotal;
    }

    public String getAdrecaLliurament() {
        return adrecaLliurament;
    }

    public void setAdrecaLliurament(String adrecaLliurament) {
        this.adrecaLliurament = adrecaLliurament;
    }

    public String getCpLliurament() {
        return cpLliurament;
    }

    public void setCpLliurament(String cpLliurament) {
        this.cpLliurament = cpLliurament;
    }

    public EstatComanda getEstat() {
        return estat;
    }

    public void setEstat(EstatComanda estat) {
        this.estat = estat;
    }

    public Set<LiniaComanda> getLiniesComanda() {
        return liniesComanda;
    }

    public void setLiniesComanda(Set<LiniaComanda> liniesComanda) {
        this.liniesComanda = liniesComanda;
    }

    public Lliurament getLliurament() {
        return lliurament;
    }

    public void setLliurament(Lliurament lliurament) {
        this.lliurament = lliurament;
    }

    // equals, hashCode i toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comanda comanda)) return false;
        return getId() != null && getId().equals(comanda.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Comanda{" +
                "id=" + id +
                ", client=" + (client != null ? client.getEmail() : "null") +
                ", dataHoraComanda=" + dataHoraComanda +
                ", importTotal=" + importTotal +
                ", estat=" + (estat != null ? estat.getNom() : "null") +
                '}';
    }
}