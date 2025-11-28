package com.example.evcarbon.controller;

import com.example.evcarbon.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        if ("admin".equals(request.getUsername()) && "admin123".equals(request.getPassword())) {
            return ResponseEntity.ok(Map.of("token", "admin-token"));
        }
        return ResponseEntity.status(401).body(Map.of("message", "Sai username hoặc password"));
    }
}

