package com.bicifood.api.controller;

import com.bicifood.api.entity.Producte;
import com.bicifood.api.dto.ProducteDto;
import com.bicifood.api.service.ProducteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller REST per gestionar productes
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/products")
@Tag(name = "Productes", description = "API per gestionar productes de BiciFood")
@CrossOrigin(origins = "*")
public class ProducteController {

    @Autowired
    private ProducteService producteService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Obté tots els productes amb paginació
     */
    @GetMapping
    @Operation(summary = "Llistar productes", description = "Retorna una llista paginada de tots els productes")
    public ResponseEntity<Page<ProducteDto>> getAllProducts(
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<Producte> productes = producteService.findAll(pageable);
        Page<ProducteDto> productesDto = productes.map(this::convertToDto);
        
        return ResponseEntity.ok(productesDto);
    }

    /**
     * Obté un producte per ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir producte per ID", description = "Retorna un producte específic pel seu ID")
    public ResponseEntity<ProducteDto> getProductById(
            @Parameter(description = "ID del producte") @PathVariable Integer id) {
        
        Optional<Producte> producte = producteService.findById(id);
        
        if (producte.isPresent()) {
            ProducteDto producteDto = convertToDto(producte.get());
            return ResponseEntity.ok(producteDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crea un nou producte
     */
    @PostMapping
    @Operation(summary = "Crear producte", description = "Crea un nou producte")
    public ResponseEntity<ProducteDto> createProduct(@Valid @RequestBody ProducteDto producteDto) {
        try {
            Producte producte = convertToEntity(producteDto);
            Producte nouProducte = producteService.save(producte);
            ProducteDto nouProducteDto = convertToDto(nouProducte);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(nouProducteDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualitza un producte existent
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualitzar producte", description = "Actualitza un producte existent")
    public ResponseEntity<ProducteDto> updateProduct(
            @Parameter(description = "ID del producte") @PathVariable Integer id,
            @Valid @RequestBody ProducteDto producteDto) {
        try {
            Producte producte = convertToEntity(producteDto);
            Producte producteActualitzat = producteService.update(id, producte);
            ProducteDto producteActualitzatDto = convertToDto(producteActualitzat);
            
            return ResponseEntity.ok(producteActualitzatDto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina un producte
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producte", description = "Elimina un producte pel seu ID")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID del producte") @PathVariable Integer id) {
        try {
            producteService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cerca productes per nom
     */
    @GetMapping("/search")
    @Operation(summary = "Cercar productes", description = "Cerca productes per nom o descripció")
    public ResponseEntity<Page<ProducteDto>> searchProducts(
            @Parameter(description = "Terme de cerca") @RequestParam String term,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<Producte> productes = producteService.searchProducts(term, pageable);
        Page<ProducteDto> productesDto = productes.map(this::convertToDto);
        
        return ResponseEntity.ok(productesDto);
    }

    /**
     * Obté productes per categoria
     */
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Productes per categoria", description = "Retorna productes d'una categoria específica")
    public ResponseEntity<Page<ProducteDto>> getProductsByCategory(
            @Parameter(description = "ID de la categoria") @PathVariable Integer categoryId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<Producte> productes = producteService.findByCategoria(categoryId, pageable);
        Page<ProducteDto> productesDto = productes.map(this::convertToDto);
        
        return ResponseEntity.ok(productesDto);
    }

    /**
     * Obté productes disponibles (amb stock)
     */
    @GetMapping("/available")
    @Operation(summary = "Productes disponibles", description = "Retorna només els productes amb stock disponible")
    public ResponseEntity<List<ProducteDto>> getAvailableProducts() {
        List<Producte> productes = producteService.findAvailableProducts();
        List<ProducteDto> productesDto = productes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(productesDto);
    }

    /**
     * Obté productes amb stock baix
     */
    @GetMapping("/low-stock")
    @Operation(summary = "Productes amb stock baix", description = "Retorna productes amb stock baix")
    public ResponseEntity<List<ProducteDto>> getLowStockProducts(
            @Parameter(description = "Límit de stock") @RequestParam(defaultValue = "10") Integer limit) {
        
        List<Producte> productes = producteService.findLowStockProducts(limit);
        List<ProducteDto> productesDto = productes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(productesDto);
    }

    /**
     * Filtra productes per rang de preus
     */
    @GetMapping("/price-range")
    @Operation(summary = "Filtrar per preu", description = "Filtra productes per rang de preus")
    public ResponseEntity<Page<ProducteDto>> getProductsByPriceRange(
            @Parameter(description = "Preu mínim") @RequestParam BigDecimal minPrice,
            @Parameter(description = "Preu màxim") @RequestParam BigDecimal maxPrice,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<Producte> productes = producteService.findByPreuRange(minPrice, maxPrice, pageable);
        Page<ProducteDto> productesDto = productes.map(this::convertToDto);
        
        return ResponseEntity.ok(productesDto);
    }

    /**
     * Actualitza l'stock d'un producte
     */
    @PatchMapping("/{id}/stock")
    @Operation(summary = "Actualitzar stock", description = "Actualitza l'stock d'un producte")
    public ResponseEntity<ProducteDto> updateStock(
            @Parameter(description = "ID del producte") @PathVariable Integer id,
            @Parameter(description = "Nou stock") @RequestParam Integer stock) {
        try {
            Producte producte = producteService.updateStock(id, stock);
            ProducteDto producteDto = convertToDto(producte);
            
            return ResponseEntity.ok(producteDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obté productes més populars
     */
    @GetMapping("/popular")
    @Operation(summary = "Productes populars", description = "Retorna els productes més populars")
    public ResponseEntity<List<ProducteDto>> getMostPopularProducts(
            @Parameter(description = "Número màxim de resultats") @RequestParam(defaultValue = "10") Integer limit,
            @PageableDefault(size = 10) Pageable pageable) {
        
        List<Producte> productes = producteService.findMostPopularProducts(pageable);
        List<ProducteDto> productesDto = productes.stream()
                .limit(limit)
                .map(this::convertToDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(productesDto);
    }

    // Mètodes de conversió
    private ProducteDto convertToDto(Producte producte) {
        ProducteDto dto = modelMapper.map(producte, ProducteDto.class);
        if (producte.getCategoria() != null) {
            dto.setCategoriaId(producte.getCategoria().getId());
            dto.setCategoriaNom(producte.getCategoria().getNom());
        }
        return dto;
    }

    private Producte convertToEntity(ProducteDto dto) {
        return modelMapper.map(dto, Producte.class);
    }
}