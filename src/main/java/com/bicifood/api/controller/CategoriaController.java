package com.bicifood.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bicifood.api.dto.CategoriaDto;
import com.bicifood.api.entity.Categoria;
import com.bicifood.api.service.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller REST per gestionar categories
 * 
 * @author BiciFood Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/categoria")
@Tag(name = "Categories", description = "API per gestionar categories de productes")
@CrossOrigin(origins = "*")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Obté totes les categories
     */
    @GetMapping
    @Operation(summary = "Llistar categories", description = "Retorna una llista de totes les categories")
    public ResponseEntity<List<CategoriaDto>> getAllCategories() {
        List<Categoria> categories = categoriaService.findAll();
        List<CategoriaDto> categoriesDto = categories.stream()
                .map(categoria -> modelMapper.map(categoria, CategoriaDto.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(categoriesDto);
    }

    /**
     * Obté una categoria per ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir categoria per ID", description = "Retorna una categoria específica per ID")
    public ResponseEntity<CategoriaDto> getCategoryById(@PathVariable Integer id) {
        Categoria categoria = categoriaService.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no trobada amb ID: " + id));

        CategoriaDto categoriaDto = modelMapper.map(categoria, CategoriaDto.class);
        return ResponseEntity.ok(categoriaDto);
    }
}