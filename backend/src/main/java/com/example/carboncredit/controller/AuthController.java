package com.example.carboncredit.controller;

import com.example.carboncredit.entity.User;
import com.example.carboncredit.repository.UserRepository;
import com.example.carboncredit.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String,String> body) {
        String email = body.get("email");
        String username = body.getOrDefault("username", email == null ? null : email.split("@")[0]);
        String name = body.get("name");
        String role = body.getOrDefault("role","BUYER");
        if (email == null || username == null) return ResponseEntity.badRequest().body("email/username required");
        if (userRepository.existsByEmail(email) || userRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("user already exists");
        }
        User u = new User();
        u.setEmail(email);
        u.setUsername(username);
        u.setFullName(name);
        u.setPassword(body.getOrDefault("password","")); // TODO: hash in real
        try {
            u.setType(User.UserType.valueOf(role.toUpperCase()));
        } catch (Exception ex) {
            u.setType(User.UserType.BUYER);
        }
        u.setCreatedAt(LocalDateTime.now());
        u.setActive(true);
        userRepository.save(u);
        return ResponseEntity.ok(Map.of("msg","ok"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body) {
        String email = body.get("email");
        String pw = body.get("password");
        var opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) return ResponseEntity.status(401).body("invalid");
        // Note: for demo password is stored plaintext — replace with bcrypt in prod
        if (!pw.equals(opt.get().getPassword())) return ResponseEntity.status(401).body("invalid");
        String token = jwtUtil.generateToken(opt.get().getUsername(), opt.get().getType().name());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
