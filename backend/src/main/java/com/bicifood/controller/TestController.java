package com.bicifood.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller de prova per verificar la configuració JSON
 */
@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class TestController {

    @PostMapping("/json")
    public ResponseEntity<Map<String, Object>> testJson(@RequestBody Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "JSON rebut correctament");
        response.put("received_data", data);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> hello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hola des de BiciFood API!");
        return ResponseEntity.ok(response);
    }
}