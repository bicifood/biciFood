package com.bicifood.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entitat JPA per la taula usuari
 * Representa els usuaris del sistema BiciFood
 */
@Entity
@Table(name = "usuari", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "email"),
           @UniqueConstraint(columnNames = "telefon")
       })
public class Usuari {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuari_id")
    private Long id;

    @NotBlank(message = "El nom és obligatori")
    @Size(max = 100, message = "El nom no pot superar els 100 caràcters")
    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @NotBlank(message = "El cognom és obligatori")
    @Size(max = 100, message = "El cognom no pot superar els 100 caràcters")
    @Column(name = "cognom", nullable = false, length = 100)
    private String cognom;

    @NotBlank(message = "L'email és obligatori")
    @Email(message = "L'email ha de tenir un format vàlid")
    @Size(max = 150, message = "L'email no pot superar els 150 caràcters")
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "La contrasenya és obligatòria")
    @Size(min = 6, message = "La contrasenya ha de tenir com a mínim 6 caràcters")
    @JsonIgnore
    @Column(name = "contrasenya", nullable = false, length = 255)
    private String contrasenya;

    @Pattern(regexp = "^[+]?[0-9]{9,15}$", message = "El telèfon ha de tenir un format vàlid")
    @Column(name = "telefon", unique = true, length = 20)
    private String telefon;

    @Size(max = 255, message = "L'adreça no pot superar els 255 caràcters")
    @Column(name = "adreca", length = 255)
    private String adreca;

    @Size(max = 100, message = "La ciutat no pot superar els 100 caràcters")
    @Column(name = "ciutat", length = 100)
    private String ciutat;

    @Pattern(regexp = "^[0-9]{5}$", message = "El codi postal ha de tenir 5 dígits")
    @Column(name = "codi_postal", length = 5)
    private String codiPostal;

    @Column(name = "data_registre", nullable = false)
    private LocalDateTime dataRegistre;

    @Column(name = "data_ultim_acces")
    private LocalDateTime dataUltimAcces;

    @Column(name = "actiu", nullable = false)
    private Boolean actiu = true;

    @Column(name = "verificat", nullable = false)
    private Boolean verificat = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private RolUsuari rol = RolUsuari.CLIENT;

    // Relació One-to-Many amb Comanda
    @OneToMany(mappedBy = "usuari", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Comanda> comandes = new ArrayList<>();

    // Enum per definir els rols d'usuari
    public enum RolUsuari {
        CLIENT, ADMIN, DELIVERY
    }

    // Constructors
    public Usuari() {
        this.dataRegistre = LocalDateTime.now();
        this.actiu = true;
        this.verificat = false;
        this.rol = RolUsuari.CLIENT;
    }

    public Usuari(String nom, String cognom, String email, String contrasenya) {
        this();
        this.nom = nom;
        this.cognom = cognom;
        this.email = email;
        this.contrasenya = contrasenya;
    }

    // Mètodes JPA Lifecycle
    @PrePersist
    protected void onCreate() {
        this.dataRegistre = LocalDateTime.now();
    }

    // Mètodes de conveniència
    public String getNomComplet() {
        return this.nom + " " + this.cognom;
    }

    public void addComanda(Comanda comanda) {
        comandes.add(comanda);
        comanda.setUsuari(this);
    }

    public void removeComanda(Comanda comanda) {
        comandes.remove(comanda);
        comanda.setUsuari(null);
    }

    public boolean isAdmin() {
        return RolUsuari.ADMIN.equals(this.rol);
    }

    public boolean isClient() {
        return RolUsuari.CLIENT.equals(this.rol);
    }

    public void updateUltimAcces() {
        this.dataUltimAcces = LocalDateTime.now();
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

    public String getCognom() {
        return cognom;
    }

    public void setCognom(String cognom) {
        this.cognom = cognom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getAdreca() {
        return adreca;
    }

    public void setAdreca(String adreca) {
        this.adreca = adreca;
    }

    public String getCiutat() {
        return ciutat;
    }

    public void setCiutat(String ciutat) {
        this.ciutat = ciutat;
    }

    public String getCodiPostal() {
        return codiPostal;
    }

    public void setCodiPostal(String codiPostal) {
        this.codiPostal = codiPostal;
    }

    public LocalDateTime getDataRegistre() {
        return dataRegistre;
    }

    public void setDataRegistre(LocalDateTime dataRegistre) {
        this.dataRegistre = dataRegistre;
    }

    public LocalDateTime getDataUltimAcces() {
        return dataUltimAcces;
    }

    public void setDataUltimAcces(LocalDateTime dataUltimAcces) {
        this.dataUltimAcces = dataUltimAcces;
    }

    public Boolean getActiu() {
        return actiu;
    }

    public void setActiu(Boolean actiu) {
        this.actiu = actiu;
    }

    public Boolean getVerificat() {
        return verificat;
    }

    public void setVerificat(Boolean verificat) {
        this.verificat = verificat;
    }

    public RolUsuari getRol() {
        return rol;
    }

    public void setRol(RolUsuari rol) {
        this.rol = rol;
    }

    public List<Comanda> getComandes() {
        return comandes;
    }

    public void setComandes(List<Comanda> comandes) {
        this.comandes = comandes;
    }

    // equals, hashCode i toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuari usuari = (Usuari) o;
        return Objects.equals(id, usuari.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Usuari{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", cognom='" + cognom + '\'' +
                ", email='" + email + '\'' +
                ", rol=" + rol +
                ", actiu=" + actiu +
                '}';
    }
}