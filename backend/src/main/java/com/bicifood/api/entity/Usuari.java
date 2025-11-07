package com.bicifood.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

/**
 * Entitat que representa un usuari en el sistema BiciFood
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
@Entity
@Table(name = "usuari")
public class Usuari {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuari")
    private Integer id;

    @NotBlank(message = "L'email és obligatori")
    @Email(message = "Format d'email invàlid")
    @Size(max = 150, message = "L'email no pot superar els 150 caràcters")
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "La contrasenya és obligatòria")
    @Size(max = 255, message = "La contrasenya hash no pot superar els 255 caràcters")
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @Column(name = "punts", columnDefinition = "int default 0")
    private Integer punts = 0;

    @Size(max = 100, message = "El nom i cognoms no pot superar els 100 caràcters")
    @Column(name = "Nom i cognoms", length = 100)
    private String nomComplet;

    @Size(max = 255, message = "L'adreça no pot superar els 255 caràcters")
    @Column(name = "adreca", length = 255)
    private String adreca;

    @Size(max = 10, message = "El codi postal no pot superar els 10 caràcters")
    @Column(name = "codi_postal", length = 10)
    private String codiPostal;

    @Size(max = 100, message = "La població no pot superar els 100 caràcters")
    @Column(name = "poblacio", length = 100)
    private String poblacio;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Comanda> comandesClient = new HashSet<>();

    @OneToMany(mappedBy = "repartidor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Lliurament> lliuramentsRepartidor = new HashSet<>();

    // Constructors
    public Usuari() {}

    public Usuari(String email, String passwordHash, Rol rol) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }

    // Getters i Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Integer getPunts() {
        return punts;
    }

    public void setPunts(Integer punts) {
        this.punts = punts;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getAdreca() {
        return adreca;
    }

    public void setAdreca(String adreca) {
        this.adreca = adreca;
    }

    public String getCodiPostal() {
        return codiPostal;
    }

    public void setCodiPostal(String codiPostal) {
        this.codiPostal = codiPostal;
    }

    public String getPoblacio() {
        return poblacio;
    }

    public void setPoblacio(String poblacio) {
        this.poblacio = poblacio;
    }

    public Set<Comanda> getComandesClient() {
        return comandesClient;
    }

    public void setComandesClient(Set<Comanda> comandesClient) {
        this.comandesClient = comandesClient;
    }

    public Set<Lliurament> getLliuramentsRepartidor() {
        return lliuramentsRepartidor;
    }

    public void setLliuramentsRepartidor(Set<Lliurament> lliuramentsRepartidor) {
        this.lliuramentsRepartidor = lliuramentsRepartidor;
    }

    // equals, hashCode i toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuari usuari)) return false;
        return getId() != null && getId().equals(usuari.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Usuari{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", rol=" + (rol != null ? rol.getNom() : "null") +
                ", nomComplet='" + nomComplet + '\'' +
                '}';
    }
}