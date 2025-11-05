package com.bicifood.api.service;

import com.bicifood.api.entity.Usuari;
import com.bicifood.api.entity.Rol;
import com.bicifood.api.repository.UsuariRepository;
import com.bicifood.api.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service per gestionar usuaris
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
@Service
@Transactional
public class UsuariService {

    @Autowired
    private UsuariRepository usuariRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Troba tots els usuaris
     * @param pageable paginació
     * @return pàgina d'usuaris
     */
    @Transactional(readOnly = true)
    public Page<Usuari> findAll(Pageable pageable) {
        return usuariRepository.findAll(pageable);
    }

    /**
     * Troba un usuari per ID
     * @param id l'ID de l'usuari
     * @return Optional amb l'usuari si existeix
     */
    @Transactional(readOnly = true)
    public Optional<Usuari> findById(Integer id) {
        return usuariRepository.findById(id);
    }

    /**
     * Troba un usuari per email
     * @param email l'email de l'usuari
     * @return Optional amb l'usuari si existeix
     */
    @Transactional(readOnly = true)
    public Optional<Usuari> findByEmail(String email) {
        return usuariRepository.findByEmail(email);
    }

    /**
     * Crea un nou usuari
     * @param usuari l'usuari a crear
     * @return l'usuari creat
     * @throws RuntimeException si l'email ja existeix
     */
    public Usuari save(Usuari usuari) {
        if (usuariRepository.existsByEmail(usuari.getEmail())) {
            throw new RuntimeException("Ja existeix un usuari amb aquest email: " + usuari.getEmail());
        }

        // Encriptar la contrasenya
        usuari.setPasswordHash(passwordEncoder.encode(usuari.getPasswordHash()));

        // Si no té rol assignat, assignar rol CLIENT per defecte
        if (usuari.getRol() == null) {
            Rol rolClient = rolRepository.findByNom("CLIENT")
                    .orElseThrow(() -> new RuntimeException("Rol CLIENT no trobat"));
            usuari.setRol(rolClient);
        }

        // Inicialitzar punts si és null
        if (usuari.getPunts() == null) {
            usuari.setPunts(0);
        }

        return usuariRepository.save(usuari);
    }

    /**
     * Actualitza un usuari existent
     * @param id l'ID de l'usuari
     * @param usuariActualitzat les dades actualitzades
     * @return l'usuari actualitzat
     * @throws RuntimeException si l'usuari no existeix
     */
    public Usuari update(Integer id, Usuari usuariActualitzat) {
        Usuari usuariExistent = usuariRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuari no trobat amb ID: " + id));

        // Verificar si l'email ha canviat i si ja existeix
        if (!usuariExistent.getEmail().equals(usuariActualitzat.getEmail()) &&
            usuariRepository.existsByEmail(usuariActualitzat.getEmail())) {
            throw new RuntimeException("Ja existeix un usuari amb aquest email: " + usuariActualitzat.getEmail());
        }

        // Actualitzar camps
        usuariExistent.setEmail(usuariActualitzat.getEmail());
        usuariExistent.setNomComplet(usuariActualitzat.getNomComplet());
        usuariExistent.setAdreca(usuariActualitzat.getAdreca());
        usuariExistent.setCodiPostal(usuariActualitzat.getCodiPostal());
        usuariExistent.setPoblacio(usuariActualitzat.getPoblacio());

        // Actualitzar rol si s'ha proporcionat
        if (usuariActualitzat.getRol() != null) {
            usuariExistent.setRol(usuariActualitzat.getRol());
        }

        // Actualitzar punts si s'ha proporcionat
        if (usuariActualitzat.getPunts() != null) {
            usuariExistent.setPunts(usuariActualitzat.getPunts());
        }

        return usuariRepository.save(usuariExistent);
    }

    /**
     * Canvia la contrasenya d'un usuari
     * @param id l'ID de l'usuari
     * @param novaPassword la nova contrasenya
     * @return l'usuari actualitzat
     */
    public Usuari changePassword(Integer id, String novaPassword) {
        Usuari usuari = usuariRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuari no trobat amb ID: " + id));

        usuari.setPasswordHash(passwordEncoder.encode(novaPassword));
        return usuariRepository.save(usuari);
    }

    /**
     * Elimina un usuari
     * @param id l'ID de l'usuari a eliminar
     * @throws RuntimeException si l'usuari no existeix
     */
    public void deleteById(Integer id) {
        if (!usuariRepository.existsById(id)) {
            throw new RuntimeException("Usuari no trobat amb ID: " + id);
        }
        usuariRepository.deleteById(id);
    }

    /**
     * Troba usuaris per rol
     * @param rolNom el nom del rol
     * @return llista d'usuaris amb aquest rol
     */
    @Transactional(readOnly = true)
    public List<Usuari> findByRol(String rolNom) {
        return usuariRepository.findByRolNom(rolNom);
    }

    /**
     * Troba tots els clients
     * @return llista de clients
     */
    @Transactional(readOnly = true)
    public List<Usuari> findAllClients() {
        return usuariRepository.findAllClients();
    }

    /**
     * Troba tots els repartidors
     * @return llista de repartidors
     */
    @Transactional(readOnly = true)
    public List<Usuari> findAllRepartidors() {
        return usuariRepository.findAllRepartidors();
    }

    /**
     * Cerca usuaris per nom complet
     * @param nomComplet el nom complet o part d'ell
     * @param pageable paginació
     * @return pàgina d'usuaris que coincideixen
     */
    @Transactional(readOnly = true)
    public Page<Usuari> searchByNomComplet(String nomComplet, Pageable pageable) {
        return usuariRepository.findByNomCompletContainingIgnoreCase(nomComplet, pageable);
    }

    /**
     * Afegeix punts a un usuari
     * @param id l'ID de l'usuari
     * @param punts els punts a afegir
     * @return l'usuari actualitzat
     */
    public Usuari addPunts(Integer id, Integer punts) {
        Usuari usuari = usuariRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuari no trobat amb ID: " + id));

        Integer puntsActuals = usuari.getPunts() != null ? usuari.getPunts() : 0;
        usuari.setPunts(puntsActuals + punts);

        return usuariRepository.save(usuari);
    }

    /**
     * Comprova si existeix un usuari amb un email determinat
     * @param email l'email a verificar
     * @return true si existeix, false altrament
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return usuariRepository.existsByEmail(email);
    }

    /**
     * Conta usuaris per rol
     * @param rolId l'ID del rol
     * @return número d'usuaris amb aquest rol
     */
    @Transactional(readOnly = true)
    public Long countByRol(Integer rolId) {
        return usuariRepository.countByRolId(rolId);
    }
}