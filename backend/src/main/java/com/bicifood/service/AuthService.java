package com.bicifood.service;

import com.bicifood.model.Usuari;
import com.bicifood.model.Usuari.RolUsuari;
import com.bicifood.repository.UsuariRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Servei per gestionar autenticació i autorització
 * Proporciona mètodes per login, registre i gestió de permisos
 */
@Service
@Transactional
public class AuthService {

    @Autowired
    private UsuariRepository usuariRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Autenticar usuari amb email i contrasenya
     */
    public Usuari authenticate(String email, String contrasenya) {
        Optional<Usuari> usuariOpt = usuariRepository.findByEmail(email);
        
        if (usuariOpt.isPresent()) {
            Usuari usuari = usuariOpt.get();
            
            // Verificar que l'usuari està actiu
            if (!usuari.getActiu()) {
                throw new IllegalArgumentException("L'usuari està desactivat");
            }
            
            // Verificar contrasenya
            if (passwordEncoder.matches(contrasenya, usuari.getContrasenya())) {
                return usuari;
            }
        }
        
        return null; // Credencials incorrectes
    }

    /**
     * Registrar nou usuari
     */
    public Usuari register(Usuari usuari) {
        // Validacions de negoci
        if (usuari.getEmail() == null || usuari.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("L'email és obligatori");
        }

        if (usuari.getContrasenya() == null || usuari.getContrasenya().trim().isEmpty()) {
            throw new IllegalArgumentException("La contrasenya és obligatòria");
        }

        if (usuari.getContrasenya().length() < 6) {
            throw new IllegalArgumentException("La contrasenya ha de tenir almenys 6 caràcters");
        }

        // Verificar que no existeixi l'email
        if (usuariRepository.existsByEmail(usuari.getEmail())) {
            throw new IllegalArgumentException("Ja existeix un usuari amb aquest email");
        }

        // Encriptar contrasenya
        usuari.setContrasenya(passwordEncoder.encode(usuari.getContrasenya()));

        // Configurar valors per defecte
        if (usuari.getRol() == null) {
            usuari.setRol(RolUsuari.CLIENT);
        }
        usuari.setActiu(true);
        usuari.setVerificat(false);
        usuari.setDataRegistre(LocalDateTime.now());

        return usuariRepository.save(usuari);
    }

    /**
     * Trobar usuari per ID
     */
    public Usuari findById(Long id) {
        Optional<Usuari> usuari = usuariRepository.findById(id);
        return usuari.orElse(null);
    }

    /**
     * Actualitzar últim accés de l'usuari
     */
    public void updateUltimAcces(Long usuariId) {
        Optional<Usuari> usuariOpt = usuariRepository.findById(usuariId);
        if (usuariOpt.isPresent()) {
            Usuari usuari = usuariOpt.get();
            usuari.setDataUltimAcces(LocalDateTime.now());
            usuariRepository.save(usuari);
        }
    }

    /**
     * Canviar contrasenya d'usuari
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
     * Obtenir permisos d'usuari segons el rol
     */
    public Map<String, Boolean> getUserPermissions(String rol) {
        Map<String, Boolean> permissions = new HashMap<>();

        if (rol == null) {
            return permissions;
        }

        try {
            RolUsuari rolEnum = RolUsuari.valueOf(rol);

            switch (rolEnum) {
                case ADMIN:
                    // Administrador té tots els permisos
                    permissions.put("canViewUsers", true);
                    permissions.put("canEditUsers", true);
                    permissions.put("canDeleteUsers", true);
                    permissions.put("canViewAllComandes", true);
                    permissions.put("canEditComandes", true);
                    permissions.put("canViewProducts", true);
                    permissions.put("canEditProducts", true);
                    permissions.put("canDeleteProducts", true);
                    permissions.put("canViewCategories", true);
                    permissions.put("canEditCategories", true);
                    permissions.put("canViewStats", true);
                    permissions.put("canManageSystem", true);
                    break;

                case DELIVERY:
                    // Repartidor pot veure i actualitzar comandes
                    permissions.put("canViewUsers", false);
                    permissions.put("canEditUsers", false);
                    permissions.put("canDeleteUsers", false);
                    permissions.put("canViewAllComandes", true);
                    permissions.put("canEditComandes", true);
                    permissions.put("canViewProducts", true);
                    permissions.put("canEditProducts", false);
                    permissions.put("canDeleteProducts", false);
                    permissions.put("canViewCategories", true);
                    permissions.put("canEditCategories", false);
                    permissions.put("canViewStats", false);
                    permissions.put("canManageSystem", false);
                    break;

                case CLIENT:
                default:
                    // Client només pot veure els seus propis recursos
                    permissions.put("canViewUsers", false);
                    permissions.put("canEditUsers", false);
                    permissions.put("canDeleteUsers", false);
                    permissions.put("canViewAllComandes", false);
                    permissions.put("canEditComandes", false);
                    permissions.put("canViewProducts", true);
                    permissions.put("canEditProducts", false);
                    permissions.put("canDeleteProducts", false);
                    permissions.put("canViewCategories", true);
                    permissions.put("canEditCategories", false);
                    permissions.put("canViewStats", false);
                    permissions.put("canManageSystem", false);
                    break;
            }

        } catch (IllegalArgumentException e) {
            // Rol desconegut, sense permisos
        }

        return permissions;
    }

    /**
     * Verificar si un usuari té un permís específic
     */
    public boolean hasPermission(Long usuariId, String permission) {
        Usuari usuari = findById(usuariId);
        if (usuari == null) {
            return false;
        }

        Map<String, Boolean> permissions = getUserPermissions(usuari.getRol().name());
        return permissions.getOrDefault(permission, false);
    }

    /**
     * Verificar si un usuari és administrador
     */
    public boolean isAdmin(Long usuariId) {
        Usuari usuari = findById(usuariId);
        return usuari != null && usuari.getRol() == RolUsuari.ADMIN;
    }

    /**
     * Verificar si un usuari és client
     */
    public boolean isClient(Long usuariId) {
        Usuari usuari = findById(usuariId);
        return usuari != null && usuari.getRol() == RolUsuari.CLIENT;
    }

    /**
     * Verificar si un usuari és repartidor
     */
    public boolean isDelivery(Long usuariId) {
        Usuari usuari = findById(usuariId);
        return usuari != null && usuari.getRol() == RolUsuari.DELIVERY;
    }

    /**
     * Validar format d'email
     */
    public boolean isValidEmail(String email) {
        return email != null && 
               email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }

    /**
     * Validar força de contrasenya
     */
    public boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        // Almenys una lletra minúscula, una majúscula, un número i un caràcter especial
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$");
    }

    /**
     * Generar suggeriments de contrasenya segura
     */
    public Map<String, String> getPasswordRequirements() {
        Map<String, String> requirements = new HashMap<>();
        requirements.put("length", "Mínim 8 caràcters");
        requirements.put("lowercase", "Almenys una lletra minúscula");
        requirements.put("uppercase", "Almenys una lletra majúscula");
        requirements.put("number", "Almenys un número");
        requirements.put("special", "Almenys un caràcter especial (@$!%*?&)");
        
        return requirements;
    }
}