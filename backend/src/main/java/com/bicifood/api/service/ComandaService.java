package com.bicifood.api.service;

import com.bicifood.api.entity.Usuari;
import com.bicifood.api.entity.Comanda;
import com.bicifood.api.entity.EstatComanda;
import com.bicifood.api.entity.LiniaComanda;
import com.bicifood.api.entity.Producte;
import com.bicifood.api.entity.Lliurament;
import com.bicifood.api.repository.UsuariRepository;
import com.bicifood.api.repository.ComandaRepository;
import com.bicifood.api.repository.EstatComandaRepository;
import com.bicifood.api.repository.LiniaComandaRepository;
import com.bicifood.api.repository.ProducteRepository;
import com.bicifood.api.repository.LliuramentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service per gestionar comandes
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
@Service
@Transactional
public class ComandaService {

    @Autowired
    private ComandaRepository comandaRepository;

    @Autowired
    private UsuariRepository usuariRepository;

    @Autowired
    private EstatComandaRepository estatComandaRepository;

    @Autowired
    private LiniaComandaRepository liniaComandaRepository;

    @Autowired
    private ProducteRepository producteRepository;

    @Autowired
    private LliuramentRepository lliuramentRepository;

    /**
     * Troba totes les comandes
     * @param pageable paginació
     * @return pàgina de comandes
     */
    @Transactional(readOnly = true)
    public Page<Comanda> findAll(Pageable pageable) {
        return comandaRepository.findAll(pageable);
    }

    /**
     * Troba una comanda per ID
     * @param id l'ID de la comanda
     * @return Optional amb la comanda si existeix
     */
    @Transactional(readOnly = true)
    public Optional<Comanda> findById(Integer id) {
        return comandaRepository.findById(id);
    }

    /**
     * Crea una nova comanda
     * @param comanda la comanda a crear
     * @return la comanda creada
     * @throws RuntimeException si hi ha errors en la creació
     */
    public Comanda save(Comanda comanda) {
        // Verificar que el client existeix
        if (comanda.getClient() == null || comanda.getClient().getId() == null) {
            throw new RuntimeException("El client és obligatori");
        }

        Usuari client = usuariRepository.findById(comanda.getClient().getId())
                .orElseThrow(() -> new RuntimeException("Client no trobat amb ID: " + comanda.getClient().getId()));

        // Verificar que l'estat existeix, si no, assignar PENDENT per defecte
        if (comanda.getEstat() == null) {
            EstatComanda estatPendent = estatComandaRepository.findByNom("PENDENT")
                    .orElseThrow(() -> new RuntimeException("Estat PENDENT no trobat"));
            comanda.setEstat(estatPendent);
        }

        comanda.setClient(client);
        
        if (comanda.getDataHoraComanda() == null) {
            comanda.setDataHoraComanda(LocalDateTime.now());
        }

        return comandaRepository.save(comanda);
    }

    /**
     * Crea una comanda completa amb línies
     * @param comanda la comanda
     * @param liniesComanda les línies de la comanda
     * @return la comanda creada amb les seves línies
     */
    public Comanda createComandaWithLines(Comanda comanda, List<LiniaComanda> liniesComanda) {
        // Crear la comanda primer
        Comanda comandaCreada = save(comanda);

        // Processar cada línia
        BigDecimal importTotal = BigDecimal.ZERO;
        
        for (LiniaComanda linia : liniesComanda) {
            // Verificar que el producte existeix i té stock suficient
            Producte producte = producteRepository.findById(linia.getProducte().getId())
                    .orElseThrow(() -> new RuntimeException("Producte no trobat amb ID: " + linia.getProducte().getId()));

            if (producte.getStock() < linia.getQuantitat()) {
                throw new RuntimeException("Stock insuficient per al producte: " + producte.getNom() + 
                                         ". Disponible: " + producte.getStock() + ", Sol·licitat: " + linia.getQuantitat());
            }

            // Configurar la línia
            linia.setComanda(comandaCreada);
            linia.setProducte(producte);
            linia.setPreuUnitari(producte.getPreu());
            linia.calcularSubtotal();

            // Guardar la línia
            liniaComandaRepository.save(linia);

            // Reduir stock del producte
            producte.setStock(producte.getStock() - linia.getQuantitat());
            producteRepository.save(producte);

            importTotal = importTotal.add(linia.getSubtotal());
        }

        // Actualitzar import total de la comanda
        comandaCreada.setImportTotal(importTotal);
        comandaCreada = comandaRepository.save(comandaCreada);

        // Crear el lliurament automàticament
        Lliurament lliurament = new Lliurament(comandaCreada);
        lliuramentRepository.save(lliurament);

        return comandaCreada;
    }

    /**
     * Actualitza l'estat d'una comanda
     * @param id l'ID de la comanda
     * @param nouEstatId l'ID del nou estat
     * @return la comanda actualitzada
     */
    public Comanda updateEstat(Integer id, Integer nouEstatId) {
        Comanda comanda = comandaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comanda no trobada amb ID: " + id));

        EstatComanda nouEstat = estatComandaRepository.findById(nouEstatId)
                .orElseThrow(() -> new RuntimeException("Estat no trobat amb ID: " + nouEstatId));

        comanda.setEstat(nouEstat);

        // Si l'estat és LLIURADA, marcar el lliurament com a completat
        if ("LLIURADA".equals(nouEstat.getNom())) {
            Optional<Lliurament> lliuramentOpt = lliuramentRepository.findByComandaId(id);
            if (lliuramentOpt.isPresent()) {
                Lliurament lliurament = lliuramentOpt.get();
                lliurament.marcarComLliurat();
                lliuramentRepository.save(lliurament);
            }
        }

        return comandaRepository.save(comanda);
    }

    /**
     * Elimina una comanda
     * @param id l'ID de la comanda a eliminar
     */
    public void deleteById(Integer id) {
        Comanda comanda = comandaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comanda no trobada amb ID: " + id));

        // Només es pot eliminar si està en estat PENDENT
        if (!"PENDENT".equals(comanda.getEstat().getNom())) {
            throw new RuntimeException("Només es poden eliminar comandes en estat PENDENT");
        }

        // Restaurar stock dels productes
        List<LiniaComanda> linies = liniaComandaRepository.findByComandaId(id);
        for (LiniaComanda linia : linies) {
            Producte producte = linia.getProducte();
            producte.setStock(producte.getStock() + linia.getQuantitat());
            producteRepository.save(producte);
        }

        comandaRepository.deleteById(id);
    }

    /**
     * Trova comandes per client
     * @param clientId l'ID del client
     * @param pageable paginació
     * @return pàgina de comandes del client
     */
    @Transactional(readOnly = true)
    public Page<Comanda> findByClient(Integer clientId, Pageable pageable) {
        return comandaRepository.findByClientId(clientId, pageable);
    }

    /**
     * Troba comandes per estat
     * @param estatId l'ID de l'estat
     * @param pageable paginació
     * @return pàgina de comandes amb aquest estat
     */
    @Transactional(readOnly = true)
    public Page<Comanda> findByEstat(Integer estatId, Pageable pageable) {
        return comandaRepository.findByEstatId(estatId, pageable);
    }

    /**
     * Troba comandes pendents
     * @return llista de comandes pendents
     */
    @Transactional(readOnly = true)
    public List<Comanda> findPendingOrders() {
        return comandaRepository.findPendingOrders();
    }

    /**
     * Troba comandes en preparació
     * @return llista de comandes en preparació
     */
    @Transactional(readOnly = true)
    public List<Comanda> findPreparingOrders() {
        return comandaRepository.findPreparingOrders();
    }

    /**
     * Troba comandes en ruta
     * @return llista de comandes en ruta
     */
    @Transactional(readOnly = true)
    public List<Comanda> findOrdersInRoute() {
        return comandaRepository.findOrdersInRoute();
    }

    /**
     * Troba comandes per rang de dates
     * @param startDate data d'inici
     * @param endDate data de fi
     * @param pageable paginació
     * @return pàgina de comandes en el rang de dates
     */
    @Transactional(readOnly = true)
    public Page<Comanda> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return comandaRepository.findByDataHoraComandaBetween(startDate, endDate, pageable);
    }

    /**
     * Calcula l'import total de vendes per un client
     * @param clientId l'ID del client
     * @return suma total dels imports
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalSalesByClient(Integer clientId) {
        return comandaRepository.calculateTotalSalesByClient(clientId);
    }

    /**
     * Calcula l'import total de vendes per un període
     * @param startDate data d'inici
     * @param endDate data de fi
     * @return suma total dels imports
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalSalesByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return comandaRepository.calculateTotalSalesByPeriod(startDate, endDate);
    }

    /**
     * Troba les comandes més recents
     * @param pageable paginació
     * @return llista de comandes més recents
     */
    @Transactional(readOnly = true)
    public List<Comanda> findRecentOrders(Pageable pageable) {
        return comandaRepository.findRecentOrders(pageable);
    }
}