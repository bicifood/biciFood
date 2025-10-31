package com.bicifood.service;

import com.bicifood.model.Usuari;
import com.bicifood.model.Usuari.RolUsuari;
import com.bicifood.model.Comanda;
import com.bicifood.repository.UsuariRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Servei per gestionar usuaris
 * Capa de lògica de negoci per operacions d'usuaris
 */
@Service
@Transactional
public class UsuariService {

    @Autowired
    private UsuariRepository usuariRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Trobar tots els usuaris amb paginació
     */
    public Map<String, Object> findAllWithPagination(int page, int size, String rol, String cerca) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dataRegistre").descending());
        Page<Usuari> usuarisPage;

        if (cerca != null && !cerca.trim().isEmpty()) {
            // Cerca per nom, cognoms o email
            usuarisPage = usuariRepository.findByNomContainingIgnoreCase(cerca, pageable);
        } else if (rol != null) {
            try {
                RolUsuari rolEnum = RolUsuari.valueOf(rol.toUpperCase());
                usuarisPage = usuariRepository.findByRol(rolEnum, pageable);
            } catch (IllegalArgumentException e) {
                usuarisPage = usuariRepository.findAll(pageable);
            }
        } else {
            usuarisPage = usuariRepository.findAll(pageable);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("usuaris", usuarisPage.getContent());
        result.put("totalElements", usuarisPage.getTotalElements());
        result.put("totalPages", usuarisPage.getTotalPages());

        return result;
    }

    /**
     * Trobar usuari per ID
     */
    public Usuari findById(Long id) {
        Optional<Usuari> usuari = usuariRepository.findById(id);
        return usuari.orElse(null);
    }

    /**
     * Trobar usuari per email
     */
    public Usuari findByEmail(String email) {
        Optional<Usuari> usuari = usuariRepository.findByEmail(email);
        return usuari.orElse(null);
    }



    /**
     * Crear nou usuari
     */
    public Usuari create(Usuari usuari) {
        // Validacions de negoci
        if (usuari.getEmail() == null || usuari.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("L'email és obligatori");
        }

        if (usuari.getContrasenya() == null || usuari.getContrasenya().trim().isEmpty()) {
            throw new IllegalArgumentException("La contrasenya és obligatòria");
        }

        // Verificar que no existeixi l'email
        if (usuariRepository.existsByEmail(usuari.getEmail())) {
            throw new IllegalArgumentException("Ja existeix un usuari amb aquest email");
        }

        // Nota: L'entitat Usuari no té camp nomUsuari

        // Encriptar contrasenya
        usuari.setContrasenya(passwordEncoder.encode(usuari.getContrasenya()));

        // Configurar valors per defecte
        if (usuari.getRol() == null) {
            usuari.setRol(RolUsuari.CLIENT);
        }
        usuari.setActiu(true);
        usuari.setDataRegistre(LocalDateTime.now());

        return usuariRepository.save(usuari);
    }

    /**
     * Actualitzar usuari
     */
    public Usuari update(Usuari usuari) {
        Usuari usuariExistent = usuariRepository.findById(usuari.getId())
            .orElseThrow(() -> new IllegalArgumentException("Usuari no trobat"));

        // Verificar email si ha canviat
        if (!usuariExistent.getEmail().equals(usuari.getEmail())) {
            if (usuariRepository.existsByEmail(usuari.getEmail())) {
                throw new IllegalArgumentException("Ja existeix un usuari amb aquest email");
            }
        }

        // Actualitzar camps (excepte contrasenya i dates del sistema)
        usuariExistent.setNom(usuari.getNom());
        usuariExistent.setCognom(usuari.getCognom());
        usuariExistent.setEmail(usuari.getEmail());
        usuariExistent.setTelefon(usuari.getTelefon());
        usuariExistent.setAdreca(usuari.getAdreca());
        usuariExistent.setCiutat(usuari.getCiutat());
        usuariExistent.setCodiPostal(usuari.getCodiPostal());

        return usuariRepository.save(usuariExistent);
    }

    /**
     * Canviar contrasenya
     */
    public void canviarContrasenya(Long usuariId, String contrasenyaActual, String novaContrasenya) {
        Usuari usuari = usuariRepository.findById(usuariId)
            .orElseThrow(() -> new IllegalArgumentException("Usuari no trobat"));

        // Verificar contrasenya actual
        if (!passwordEncoder.matches(contrasenyaActual, usuari.getContrasenya())) {
            throw new IllegalArgumentException("La contrasenya actual no és correcta");
        }

        // Validar nova contrasenya
        if (novaContrasenya == null || novaContrasenya.length() < 6) {
            throw new IllegalArgumentException("La nova contrasenya ha de tenir almenys 6 caràcters");
        }

        // Actualitzar contrasenya
        usuari.setContrasenya(passwordEncoder.encode(novaContrasenya));
        usuariRepository.save(usuari);
    }

    /**
     * Canviar estat d'usuari (actiu/inactiu)
     */
    public Usuari canviarEstat(Long usuariId, Boolean actiu) {
        Usuari usuari = usuariRepository.findById(usuariId)
            .orElseThrow(() -> new IllegalArgumentException("Usuari no trobat"));

        usuari.setActiu(actiu);
        return usuariRepository.save(usuari);
    }

    /**
     * Obtenir comandes d'un usuari
     */
    public List<Comanda> getComandesUsuari(Long usuariId) {
        Usuari usuari = usuariRepository.findById(usuariId)
            .orElseThrow(() -> new IllegalArgumentException("Usuari no trobat"));

        return usuari.getComandes();
    }

    /**
     * Eliminar usuari (soft delete)
     */
    public void delete(Long usuariId) {
        Usuari usuari = usuariRepository.findById(usuariId)
            .orElseThrow(() -> new IllegalArgumentException("Usuari no trobat"));

        // En lloc d'eliminar físicament, desactivem l'usuari
        usuari.setActiu(false);
        usuariRepository.save(usuari);
    }

    /**
     * Verificar si un email està disponible
     */
    public boolean isEmailDisponible(String email) {
        return !usuariRepository.existsByEmail(email);
    }

    /**
     * Verificar si un nom d'usuari està disponible (ja no utilitzat - l'entitat no té nomUsuari)
     */
    public boolean isUsernameDisponible(String nomUsuari) {
        return true; // Sempre disponible perquè el camp no existeix
    }

    /**
     * Obtenir estadístiques d'un usuari
     */
    public Map<String, Object> getUsuariStats(Long usuariId) {
        Usuari usuari = usuariRepository.findById(usuariId)
            .orElseThrow(() -> new IllegalArgumentException("Usuari no trobat"));

        Map<String, Object> stats = new HashMap<>();
        
        List<Comanda> comandes = usuari.getComandes();
        stats.put("total_comandes", comandes.size());
        
        if (!comandes.isEmpty()) {
            // Calcular import total gastat
            stats.put("import_total_gastat", comandes.stream()
                .mapToDouble(c -> c.getPreuTotal().doubleValue())
                .sum());
            
            // Comanda més recent
            stats.put("darrera_comanda", comandes.stream()
                .max((c1, c2) -> c1.getDataComanda().compareTo(c2.getDataComanda()))
                .map(c -> c.getDataComanda())
                .orElse(null));
        } else {
            stats.put("import_total_gastat", 0.0);
            stats.put("darrera_comanda", null);
        }

        stats.put("data_registre", usuari.getDataRegistre());
        stats.put("actiu", usuari.getActiu());
        stats.put("rol", usuari.getRol());

        return stats;
    }

    /**
     * Obtenir estadístiques generals del sistema
     */
    public Map<String, Object> getGeneralStats() {
        Map<String, Object> stats = new HashMap<>();

        // Total d'usuaris
        stats.put("total_usuaris", usuariRepository.count());

        // Usuaris actius
        stats.put("usuaris_actius", usuariRepository.findByActiuTrue().size());

        // Usuaris per rol
        Map<String, Long> usuarisPerRol = new HashMap<>();
        for (RolUsuari rol : RolUsuari.values()) {
            usuarisPerRol.put(rol.name(), usuariRepository.countByRol(rol));
        }
        stats.put("usuaris_per_rol", usuarisPerRol);

        // Usuaris registrats recentment (últims 7 dies)
        LocalDateTime fa7Dies = LocalDateTime.now().minusDays(7);
        stats.put("usuaris_recents", usuariRepository.findRecentRegisteredUsers(fa7Dies).size());

        // Usuaris amb comandes
        stats.put("usuaris_amb_comandes", usuariRepository.findUsersWithComandes().size());

        return stats;
    }
}