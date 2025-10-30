package com.bicifood.service;

import com.bicifood.model.Usuari;
import com.bicifood.model.Usuari.RolUsuari;
import com.bicifood.model.Comanda;
import com.bicifood.model.EstatComanda;
import com.bicifood.model.Producte;
import com.bicifood.repository.UsuariRepository;
import com.bicifood.repository.ComandaRepository;
import com.bicifood.repository.ProducteRepository;
import com.bicifood.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servei per funcions administratives
 * Proporciona mètodes per gestió del sistema i estadístiques avançades
 */
@Service
@Transactional
public class AdminService {

    @Autowired
    private UsuariRepository usuariRepository;

    @Autowired
    private ComandaRepository comandaRepository;

    @Autowired
    private ProducteRepository producteRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * Obtenir dades del dashboard principal
     */
    public Map<String, Object> getDashboardData() {
        Map<String, Object> dashboard = new HashMap<>();
        
        // Estadístiques generals
        dashboard.put("total_usuaris", usuariRepository.count());
        dashboard.put("usuaris_actius", usuariRepository.findByActiuTrue().size());
        dashboard.put("total_comandes", comandaRepository.count());
        dashboard.put("total_productes", producteRepository.count());
        dashboard.put("total_categories", categoriaRepository.count());
        
        // Estadístiques dels últims 30 dies
        LocalDateTime fa30Dies = LocalDateTime.now().minusDays(30);
        LocalDateTime fa7Dies = LocalDateTime.now().minusDays(7);
        dashboard.put("nous_usuaris_30d", usuariRepository.findRecentRegisteredUsers(fa7Dies).size());
        dashboard.put("comandes_30d", comandaRepository.countByDataComandaAfter(fa30Dies));
        
        // Estadístiques per estat de comandes
        Map<String, Long> comandesPerEstat = new HashMap<>();
        for (EstatComanda estat : EstatComanda.values()) {
            comandesPerEstat.put(estat.name(), comandaRepository.countByEstat(estat));
        }
        dashboard.put("comandes_per_estat", comandesPerEstat);
        
        // Estadístiques per rol d'usuaris
        Map<String, Long> usuarisPerRol = new HashMap<>();
        for (RolUsuari rol : RolUsuari.values()) {
            usuarisPerRol.put(rol.name(), usuariRepository.countByRol(rol));
        }
        dashboard.put("usuaris_per_rol", usuarisPerRol);
        
        // Ingressos totals
        BigDecimal ingressosTotals = comandaRepository.sumTotalAll();
        dashboard.put("ingressos_totals", ingressosTotals != null ? ingressosTotals : BigDecimal.ZERO);
        
        // Alertes de productes amb stock baix
        List<Producte> stockBaix = producteRepository.findByStockLessThan(10);
        dashboard.put("alertes_stock_baix", stockBaix.size());
        dashboard.put("productes_stock_baix", stockBaix.stream()
            .filter(p -> p.getActiu())
            .map(p -> Map.of(
                "id", p.getId(),
                "nom", p.getNom(),
                "stock", p.getStock(),
                "categoria", p.getCategoria().getNom()
            ))
            .collect(Collectors.toList()));
        
        return dashboard;
    }

    /**
     * Obtenir estadístiques detallades
     */
    public Map<String, Object> getDetailedStats(String periode, String tipus) {
        Map<String, Object> stats = new HashMap<>();
        LocalDateTime dataInici = calcularDataInici(periode);
        
        switch (tipus.toLowerCase()) {
            case "vendes":
                stats.putAll(getVendesStats(dataInici));
                break;
            case "usuaris":
                stats.putAll(getUsuarisStats(dataInici));
                break;
            case "productes":
                stats.putAll(getProductesStats(dataInici));
                break;
            default:
                stats.putAll(getGeneralStats(dataInici));
                break;
        }
        
        return stats;
    }

    /**
     * Gestió d'usuaris amb filtres avançats
     */
    public Map<String, Object> getUsersWithAdvancedFilters(int page, int size, String rol, Boolean actiu, String ordenar, String cerca) {
        Pageable pageable = createPageable(page, size, ordenar);
        Page<Usuari> usuarisPage;
        
        // Aplicar filtres
        if (cerca != null && !cerca.trim().isEmpty()) {
            usuarisPage = usuariRepository.findByNomContainingIgnoreCase(cerca, pageable);
        } else if (rol != null && actiu != null) {
            try {
                RolUsuari rolEnum = RolUsuari.valueOf(rol.toUpperCase());
                usuarisPage = usuariRepository.findByRolAndActiu(rolEnum, actiu, pageable);
            } catch (IllegalArgumentException e) {
                usuarisPage = usuariRepository.findByActiu(actiu, pageable);
            }
        } else if (rol != null) {
            try {
                RolUsuari rolEnum = RolUsuari.valueOf(rol.toUpperCase());
                usuarisPage = usuariRepository.findByRol(rolEnum, pageable);
            } catch (IllegalArgumentException e) {
                usuarisPage = usuariRepository.findAll(pageable);
            }
        } else if (actiu != null) {
            usuarisPage = usuariRepository.findByActiu(actiu, pageable);
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
     * Actualitzar estat d'usuaris en massa
     */
    public int bulkUpdateUserStatus(List<Long> userIds, Boolean actiu) {
        int updated = 0;
        for (Long userId : userIds) {
            Optional<Usuari> usuariOpt = usuariRepository.findById(userId);
            if (usuariOpt.isPresent()) {
                Usuari usuari = usuariOpt.get();
                usuari.setActiu(actiu);
                usuariRepository.save(usuari);
                updated++;
            }
        }
        return updated;
    }

    /**
     * Gestió de comandes amb filtres avançats
     */
    public Map<String, Object> getOrdersWithAdvancedFilters(int page, int size, String estat, Long usuariId, String dataInici, String dataFi) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dataComanda").descending());
        Page<Comanda> comandesPage;
        
        // Aplicar filtres
        LocalDateTime inici = parseDate(dataInici);
        LocalDateTime fi = parseDate(dataFi);
        
        if (usuariId != null && estat != null) {
            EstatComanda estatEnum = EstatComanda.valueOf(estat.toUpperCase());
            comandesPage = comandaRepository.findByUsuariIdAndEstat(usuariId, estatEnum, pageable);
        } else if (usuariId != null) {
            comandesPage = comandaRepository.findByUsuariId(usuariId, pageable);
        } else if (estat != null) {
            EstatComanda estatEnum = EstatComanda.valueOf(estat.toUpperCase());
            comandesPage = comandaRepository.findByEstat(estatEnum, pageable);
        } else if (inici != null && fi != null) {
            comandesPage = comandaRepository.findByDataComandaBetween(inici, fi, pageable);
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
     * Gestió de productes amb filtres avançats i alertes
     */
    public Map<String, Object> getProductsWithAdvancedFilters(int page, int size, Boolean stockBaix, Long categoriaId, Boolean actiu) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nom").ascending());
        Page<Producte> productesPage;
        
        // Aplicar filtres
        if (stockBaix != null && stockBaix) {
            productesPage = producteRepository.findByStockLessThan(10, pageable);
        } else if (categoriaId != null && actiu != null) {
            productesPage = producteRepository.findByCategoriaIdAndActiu(categoriaId, actiu, pageable);
        } else if (categoriaId != null) {
            productesPage = producteRepository.findByCategoriaId(categoriaId, pageable);
        } else if (actiu != null) {
            productesPage = producteRepository.findByActiu(actiu, pageable);
        } else {
            productesPage = producteRepository.findAll(pageable);
        }
        
        // Generar alertes
        List<Map<String, Object>> alerts = new ArrayList<>();
        List<Producte> stockBaixList = producteRepository.findByStockLessThan(10).stream()
            .filter(p -> p.getActiu())
            .collect(Collectors.toList());
        for (Producte p : stockBaixList) {
            alerts.add(Map.of(
                "tipus", "STOCK_BAIX",
                "producte_id", p.getId(),
                "producte_nom", p.getNom(),
                "stock_actual", p.getStock(),
                "missatge", String.format("Stock baix: només queden %d unitats", p.getStock())
            ));
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("productes", productesPage.getContent());
        result.put("totalElements", productesPage.getTotalElements());
        result.put("totalPages", productesPage.getTotalPages());
        result.put("alerts", alerts);
        
        return result;
    }

    /**
     * Generar reports
     */
    public Map<String, Object> generateReport(String tipus, String format, String periode) {
        Map<String, Object> reportData = new HashMap<>();
        LocalDateTime dataInici = calcularDataInici(periode);
        
        switch (tipus.toLowerCase()) {
            case "vendes":
                reportData = generateSalesReport(dataInici);
                break;
            case "usuaris":
                reportData = generateUsersReport(dataInici);
                break;
            case "productes":
                reportData = generateProductsReport(dataInici);
                break;
            default:
                reportData.put("error", "Tipus de report no suportat: " + tipus);
                break;
        }
        
        reportData.put("generat_a", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        reportData.put("periode", periode);
        reportData.put("format", format);
        
        return reportData;
    }

    /**
     * Obtenir configuració del sistema
     */
    public Map<String, Object> getSystemConfiguration() {
        Map<String, Object> config = new HashMap<>();
        
        // Configuracions simulades (en un sistema real vindrien d'una base de dades de configuració)
        config.put("nom_aplicacio", "BiciFood");
        config.put("versio", "1.0.0");
        config.put("manteniment_actiu", false);
        config.put("registre_obert", true);
        config.put("stock_minim_alerta", 10);
        config.put("temps_sessio_max", 3600);
        config.put("email_admin", "admin@bicifood.com");
        config.put("idioma_per_defecte", "ca");
        
        return config;
    }

    /**
     * Actualitzar configuració del sistema
     */
    public Map<String, Object> updateSystemConfiguration(Map<String, Object> configData) {
        // En un sistema real, guardaria a base de dades
        Map<String, Object> updatedConfig = new HashMap<>(getSystemConfiguration());
        updatedConfig.putAll(configData);
        updatedConfig.put("darrera_actualitzacio", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        return updatedConfig;
    }

    /**
     * Obtenir logs del sistema (simulat)
     */
    public Map<String, Object> getSystemLogs(int page, int size, String nivell, String dataInici, String dataFi) {
        // Simulació de logs
        List<Map<String, Object>> logs = new ArrayList<>();
        
        for (int i = 0; i < size; i++) {
            Map<String, Object> log = new HashMap<>();
            log.put("id", page * size + i + 1);
            log.put("timestamp", LocalDateTime.now().minusHours(i).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            log.put("nivell", nivell != null ? nivell : (i % 4 == 0 ? "ERROR" : i % 3 == 0 ? "WARN" : "INFO"));
            log.put("missatge", "Log simulat número " + (i + 1));
            log.put("component", "SystemService");
            logs.add(log);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("logs", logs);
        result.put("totalElements", 1000L); // Simulat
        result.put("totalPages", 20); // Simulat
        
        return result;
    }

    /**
     * Realitzar operacions de manteniment
     */
    public Map<String, Object> performMaintenance(String operation, Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        switch (operation.toLowerCase()) {
            case "clear_cache":
                result.put("message", "Cache esborrada correctament");
                result.put("cache_cleared", true);
                break;
                
            case "cleanup_logs":
                result.put("message", "Logs antics eliminats");
                result.put("logs_deleted", 150); // Simulat
                break;
                
            case "optimize_db":
                result.put("message", "Base de dades optimitzada");
                result.put("tables_optimized", 12); // Simulat
                break;
                
            case "backup_data":
                result.put("message", "Backup creat correctament");
                result.put("backup_file", "backup_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".sql");
                break;
                
            default:
                result.put("error", "Operació de manteniment no reconeguda: " + operation);
                break;
        }
        
        result.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return result;
    }

    // Mètodes auxiliars privats

    private LocalDateTime calcularDataInici(String periode) {
        LocalDateTime ara = LocalDateTime.now();
        
        switch (periode.toLowerCase()) {
            case "dia":
                return ara.minusDays(1);
            case "setmana":
                return ara.minusWeeks(1);
            case "any":
                return ara.minusYears(1);
            case "mes":
            default:
                return ara.minusMonths(1);
        }
    }

    private Pageable createPageable(int page, int size, String ordenar) {
        Sort sort = Sort.by("id").descending(); // Per defecte
        
        if (ordenar != null) {
            switch (ordenar.toLowerCase()) {
                case "nom":
                    sort = Sort.by("nom").ascending();
                    break;
                case "data_registre":
                    sort = Sort.by("dataRegistre").descending();
                    break;
                case "email":
                    sort = Sort.by("email").ascending();
                    break;
            }
        }
        
        return PageRequest.of(page, size, sort);
    }

    private LocalDateTime parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        
        try {
            return LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (Exception e) {
            return null;
        }
    }

    private Map<String, Object> getVendesStats(LocalDateTime dataInici) {
        Map<String, Object> stats = new HashMap<>();
        
        long totalComandes = comandaRepository.countByDataComandaAfter(dataInici);
        BigDecimal totalIngressos = comandaRepository.sumTotalByDataAfter(dataInici);
        
        stats.put("total_comandes", totalComandes);
        stats.put("total_ingressos", totalIngressos != null ? totalIngressos : BigDecimal.ZERO);
        stats.put("mitjana_per_comanda", totalComandes > 0 && totalIngressos != null ? 
            totalIngressos.divide(BigDecimal.valueOf(totalComandes), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
        
        return stats;
    }

    private Map<String, Object> getUsuarisStats(LocalDateTime dataInici) {
        Map<String, Object> stats = new HashMap<>();
        
        LocalDateTime fa7Dies = LocalDateTime.now().minusDays(7);
        stats.put("nous_usuaris", usuariRepository.findRecentRegisteredUsers(fa7Dies).size());
        stats.put("usuaris_actius", usuariRepository.findByActiuTrue().size());
        stats.put("total_usuaris", usuariRepository.count());
        
        return stats;
    }

    private Map<String, Object> getProductesStats(LocalDateTime dataInici) {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("total_productes", producteRepository.count());
        stats.put("productes_actius", producteRepository.countByActiuTrue());
        stats.put("productes_stock_baix", producteRepository.findByStockLessThan(10).stream()
            .filter(p -> p.getActiu())
            .collect(Collectors.toList()).size());
        
        return stats;
    }

    private Map<String, Object> getGeneralStats(LocalDateTime dataInici) {
        Map<String, Object> stats = new HashMap<>();
        
        stats.putAll(getVendesStats(dataInici));
        stats.putAll(getUsuarisStats(dataInici));
        stats.putAll(getProductesStats(dataInici));
        
        return stats;
    }

    private Map<String, Object> generateSalesReport(LocalDateTime dataInici) {
        Map<String, Object> report = new HashMap<>();
        
        report.put("periode_inici", dataInici.format(DateTimeFormatter.ISO_LOCAL_DATE));
        report.put("periode_fi", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        report.put("total_comandes", comandaRepository.countByDataComandaAfter(dataInici));
        report.put("total_ingressos", comandaRepository.sumTotalByDataAfter(dataInici));
        
        return report;
    }

    private Map<String, Object> generateUsersReport(LocalDateTime dataInici) {
        Map<String, Object> report = new HashMap<>();
        
        report.put("periode_inici", dataInici.format(DateTimeFormatter.ISO_LOCAL_DATE));
        LocalDateTime fa7Dies = LocalDateTime.now().minusDays(7);
        report.put("nous_usuaris", usuariRepository.findRecentRegisteredUsers(fa7Dies).size());
        report.put("total_usuaris", usuariRepository.count());
        
        return report;
    }

    private Map<String, Object> generateProductsReport(LocalDateTime dataInici) {
        Map<String, Object> report = new HashMap<>();
        
        report.put("total_productes", producteRepository.count());
        report.put("productes_stock_baix", producteRepository.findByStockLessThan(10).stream()
            .filter(p -> p.getActiu())
            .collect(Collectors.toList()).size());
        
        return report;
    }
}