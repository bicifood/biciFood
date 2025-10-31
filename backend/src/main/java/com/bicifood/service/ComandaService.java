package com.bicifood.service;

import com.bicifood.model.*;
import com.bicifood.model.EstatComanda;
import com.bicifood.repository.ComandaRepository;
import com.bicifood.repository.ProducteRepository;
import com.bicifood.repository.UsuariRepository;
import com.bicifood.repository.DetallComandaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Servei per gestionar comandes
 * Capa de lògica de negoci per operacions de comandes
 */
@Service
@Transactional
public class ComandaService {

    @Autowired
    private ComandaRepository comandaRepository;

    @Autowired
    private ProducteRepository producteRepository;

    @Autowired
    private UsuariRepository usuariRepository;

    @Autowired
    private DetallComandaRepository detallComandaRepository;

    /**
     * Trobar totes les comandes amb paginació
     */
    public Map<String, Object> findAllWithPagination(int page, int size, Long usuariId, EstatComanda estat) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dataCreacio").descending());
        Page<Comanda> comandesPage;

        if (usuariId != null && estat != null) {
            comandesPage = comandaRepository.findByUsuariIdAndEstat(usuariId, estat, pageable);
        } else if (usuariId != null) {
            comandesPage = comandaRepository.findByUsuariId(usuariId, pageable);
        } else if (estat != null) {
            comandesPage = comandaRepository.findByEstat(estat, pageable);
        } else {
            comandesPage = comandaRepository.findAll(pageable);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("comandes", comandesPage.getContent());
        result.put("totalElements", comandesPage.getTotalElements());
        result.put("totalPages", comandesPage.getTotalPages());

        return result;
    }

    /**
     * Trobar comanda per ID
     */
    public Comanda findById(Long id) {
        Optional<Comanda> comanda = comandaRepository.findById(id);
        return comanda.orElse(null);
    }

    /**
     * Trobar comandes per usuari
     */
    public List<Comanda> findByUsuariId(Long usuariId) {
        return comandaRepository.findByUsuariIdOrderByDataComandaDesc(usuariId);
    }

    /**
     * Crear una nova comanda
     */
    public Comanda createComanda(Map<String, Object> comandaData) {
        // Validar dades obligatòries
        if (!comandaData.containsKey("usuari_id")) {
            throw new IllegalArgumentException("L'ID de l'usuari és obligatori");
        }

        Long usuariId = Long.valueOf(comandaData.get("usuari_id").toString());
        Usuari usuari = usuariRepository.findById(usuariId)
            .orElseThrow(() -> new IllegalArgumentException("Usuari no trobat"));

        // Crear la comanda
        Comanda comanda = new Comanda();
        comanda.setUsuari(usuari);
        comanda.setDataComanda(LocalDateTime.now());
        comanda.setEstat(EstatComanda.PENDENT);
        comanda.setPreuTotal(BigDecimal.ZERO);

        // Afegir adreça si està present
        if (comandaData.containsKey("adreca_lliurament")) {
            comanda.setAdrecaEnviament(comandaData.get("adreca_lliurament").toString());
        }

        // Afegir notes si estan presents
        if (comandaData.containsKey("notes")) {
            comanda.setNotes(comandaData.get("notes").toString());
        }

        // Afegir mètode de pagament si està present
        if (comandaData.containsKey("metode_pagament_id")) {
            Long metodePagamentId = Long.valueOf(comandaData.get("metode_pagament_id").toString());
            // Aquí podries validar que el mètode de pagament existeix
            // MetodePagament metodePagament = metodePagamentRepository.findById(metodePagamentId)...
        }

        comanda = comandaRepository.save(comanda);

        // Processar productes si estan presents
        if (comandaData.containsKey("productes")) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> productes = (List<Map<String, Object>>) comandaData.get("productes");
            for (Map<String, Object> producteData : productes) {
                afegirProducteAComanda(comanda, producteData);
            }
            
            // Recalcular total
            comanda = recalcularTotal(comanda);
        }

        return comanda;
    }

    /**
     * Afegir producte a una comanda existent
     */
    public Comanda afegirProducte(Long comandaId, Map<String, Object> producteData) {
        Comanda comanda = comandaRepository.findById(comandaId)
            .orElseThrow(() -> new IllegalArgumentException("Comanda no trobada"));

        if (!EstatComanda.PENDENT.equals(comanda.getEstat())) {
            throw new IllegalArgumentException("No es pot modificar una comanda en estat: " + comanda.getEstat());
        }

        afegirProducteAComanda(comanda, producteData);
        return recalcularTotal(comanda);
    }

    /**
     * Eliminar producte d'una comanda
     */
    public Comanda eliminarProducte(Long comandaId, Long detallId) {
        Comanda comanda = comandaRepository.findById(comandaId)
            .orElseThrow(() -> new IllegalArgumentException("Comanda no trobada"));

        if (!EstatComanda.PENDENT.equals(comanda.getEstat())) {
            throw new IllegalArgumentException("No es pot modificar una comanda en estat: " + comanda.getEstat());
        }

        DetallComanda detall = detallComandaRepository.findById(detallId)
            .orElseThrow(() -> new IllegalArgumentException("Detall de comanda no trobat"));

        if (!detall.getComanda().getId().equals(comandaId)) {
            throw new IllegalArgumentException("El detall no pertany a aquesta comanda");
        }

        detallComandaRepository.delete(detall);
        comanda.getDetalls().remove(detall);

        return recalcularTotal(comanda);
    }

    /**
     * Actualitzar estat de comanda
     */
    public Comanda actualitzarEstat(Long comandaId, EstatComanda nouEstat) {
        Comanda comanda = comandaRepository.findById(comandaId)
            .orElseThrow(() -> new IllegalArgumentException("Comanda no trobada"));

        if (!potCanviarEstat(comanda.getEstat(), nouEstat)) {
            throw new IllegalArgumentException(
                String.format("No es pot canviar l'estat de %s a %s", 
                    comanda.getEstat(), nouEstat));
        }

        comanda.setEstat(nouEstat);
        
        // Actualitzar timestamps segons l'estat
        switch (nouEstat) {
            case ENTREGADA:
                comanda.setDataEntregaReal(LocalDateTime.now());
                break;
            default:
                break;
        }

        return comandaRepository.save(comanda);
    }

    /**
     * Confirmar comanda
     */
    public Comanda confirmarComanda(Long comandaId) {
        return actualitzarEstat(comandaId, EstatComanda.CONFIRMADA);
    }

    /**
     * Cancel·lar comanda
     */
    public Comanda cancelarComanda(Long comandaId) {
        Comanda comanda = comandaRepository.findById(comandaId)
            .orElseThrow(() -> new IllegalArgumentException("Comanda no trobada"));

        // Restaurar stock dels productes si la comanda estava confirmada
        if (comanda.getEstat() == EstatComanda.CONFIRMADA || comanda.getEstat() == EstatComanda.EN_PREPARACIO) {
            for (DetallComanda detall : comanda.getDetalls()) {
                Producte producte = detall.getProducte();
                producte.setStock(producte.getStock() + detall.getQuantitat());
                producteRepository.save(producte);
            }
        }

        return actualitzarEstat(comandaId, EstatComanda.CANCEL_LADA);
    }

    /**
     * Obtenir estadístiques de comandes
     */
    public Map<String, Object> getStats(Long usuariId, String periode) {
        Map<String, Object> stats = new HashMap<>();

        LocalDateTime dataInici = calcularDataInici(periode);

        if (usuariId != null) {
            // Estadístiques per usuari específic
            stats.put("total_comandes", comandaRepository.countByUsuariId(usuariId));
            stats.put("comandes_periode", comandaRepository.countByUsuariIdAndDataComandaAfter(usuariId, dataInici));
            stats.put("import_total", comandaRepository.sumTotalByUsuariId(usuariId));
        } else {
            // Estadístiques generals
            stats.put("total_comandes", comandaRepository.count());
            stats.put("comandes_periode", comandaRepository.countByDataComandaAfter(dataInici));
            stats.put("import_total", comandaRepository.sumTotalAll());
            
            // Estadístiques per estat
            Map<String, Long> estatStats = new HashMap<>();
            for (EstatComanda estat : EstatComanda.values()) {
                estatStats.put(estat.name(), comandaRepository.countByEstat(estat));
            }
            stats.put("per_estat", estatStats);
        }

        return stats;
    }

    /**
     * Obtenir historial de canvis d'una comanda
     */
    public Map<String, Object> getHistorialCanvis(Long comandaId) {
        Comanda comanda = comandaRepository.findById(comandaId)
            .orElseThrow(() -> new IllegalArgumentException("Comanda no trobada"));

        Map<String, Object> historial = new HashMap<>();
        historial.put("comanda_id", comandaId);
        
        List<Map<String, Object>> canvis = new ArrayList<>();
        
        // Creació
        Map<String, Object> creacio = new HashMap<>();
        creacio.put("estat", "CREADA");
        creacio.put("data", comanda.getDataComanda().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        creacio.put("descripcio", "Comanda creada");
        canvis.add(creacio);

        // Lliurament
        if (comanda.getDataEntregaReal() != null) {
            Map<String, Object> lliurament = new HashMap<>();
            lliurament.put("estat", "ENTREGADA");
            lliurament.put("data", comanda.getDataEntregaReal().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            lliurament.put("descripcio", "Comanda entregada");
            canvis.add(lliurament);
        }

        // Estat actual
        Map<String, Object> actual = new HashMap<>();
        actual.put("estat", comanda.getEstat().name());
        actual.put("descripcio", "Estat actual: " + comanda.getEstat());
        canvis.add(actual);

        historial.put("canvis", canvis);
        return historial;
    }

    // Mètodes privats auxiliars

    /**
     * Verificar si es pot canviar d'un estat a un altre
     */
    private boolean potCanviarEstat(EstatComanda estatActual, EstatComanda nouEstat) {
        switch (estatActual) {
            case PENDENT:
                return nouEstat == EstatComanda.CONFIRMADA || nouEstat == EstatComanda.CANCEL_LADA;
            case CONFIRMADA:
                return nouEstat == EstatComanda.EN_PREPARACIO || nouEstat == EstatComanda.CANCEL_LADA;
            case EN_PREPARACIO:
                return nouEstat == EstatComanda.LLESTA || nouEstat == EstatComanda.CANCEL_LADA;
            case LLESTA:
                return nouEstat == EstatComanda.ENTREGADA;
            case ENTREGADA:
            case CANCEL_LADA:
                return false; // Estats finals
            default:
                return false;
        }
    }

    private void afegirProducteAComanda(Comanda comanda, Map<String, Object> producteData) {
        Long producteId = Long.valueOf(producteData.get("producte_id").toString());
        Integer quantitat = Integer.valueOf(producteData.get("quantitat").toString());

        Producte producte = producteRepository.findById(producteId)
            .orElseThrow(() -> new IllegalArgumentException("Producte no trobat"));

        if (quantitat <= 0) {
            throw new IllegalArgumentException("La quantitat ha de ser positiva");
        }

        if (producte.getStock() < quantitat) {
            throw new IllegalArgumentException("Stock insuficient per al producte: " + producte.getNom());
        }

        // Verificar si ja existeix aquest producte a la comanda
        DetallComanda detallExistent = comanda.getDetalls().stream()
            .filter(d -> d.getProducte().getId().equals(producteId))
            .findFirst()
            .orElse(null);

        if (detallExistent != null) {
            // Actualitzar quantitat existent
            detallExistent.setQuantitat(detallExistent.getQuantitat() + quantitat);
            detallExistent.setPreuTotal(detallExistent.getPreuUnitari().multiply(BigDecimal.valueOf(detallExistent.getQuantitat())));
        } else {
            // Crear nou detall
            DetallComanda nouDetall = new DetallComanda();
            nouDetall.setComanda(comanda);
            nouDetall.setProducte(producte);
            nouDetall.setQuantitat(quantitat);
            nouDetall.setPreuUnitari(producte.getPreu());
            nouDetall.setPreuTotal(producte.getPreu().multiply(BigDecimal.valueOf(quantitat)));

            comanda.getDetalls().add(nouDetall);
        }

        // Reduir stock
        producte.setStock(producte.getStock() - quantitat);
        producteRepository.save(producte);
    }

    private Comanda recalcularTotal(Comanda comanda) {
        BigDecimal total = comanda.getDetalls().stream()
            .map(DetallComanda::getPreuTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        comanda.setPreuTotal(total);
        return comandaRepository.save(comanda);
    }

    private LocalDateTime calcularDataInici(String periode) {
        LocalDateTime ara = LocalDateTime.now();
        
        if (periode == null) {
            return ara.minusMonths(1); // Per defecte, últim mes
        }

        switch (periode.toLowerCase()) {
            case "dia":
                return ara.minusDays(1);
            case "setmana":
                return ara.minusWeeks(1);
            case "mes":
                return ara.minusMonths(1);
            case "any":
                return ara.minusYears(1);
            default:
                return ara.minusMonths(1);
        }
    }
}