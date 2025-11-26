package com.example.evcarbon.controller;

import com.example.evcarbon.model.AdminUser;
import com.example.evcarbon.model.CarbonTransaction;
import com.example.evcarbon.model.CarbonCredit;
import com.example.evcarbon.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin
public class AdminController {

    private final AdminService service;

    // Users
    @PostMapping("/users")
    public AdminUser addUser(@RequestParam String username, @RequestParam String role) {
        return service.addUser(username, role);
    }

    @GetMapping("/users")
    public List<AdminUser> getUsers() {
        return service.getUsers();
    }

    @PostMapping("/users/{id}/status")
    public void changeStatus(@PathVariable Long id, @RequestParam boolean active) {
        service.toggleUserStatus(id, active);
    }

    // Credits
    @GetMapping("/credits")
    public List<CarbonCredit> getCredits() {
        return service.getCredits();
    }

    @DeleteMapping("/credits/{id}")
    public void deleteCredit(@PathVariable Long id) {
        service.removeListing(id);
    }

    // GIAO DỊCH
    @GetMapping("/transactions")
    public List<CarbonTransaction> getTransactions() {
        return service.getTransactions();
    }

    @PostMapping("/transactions/{id}/dispute")
    public CarbonTransaction dispute(@PathVariable Long id) {
        return service.markDisputed(id);
    }

    // BÁO CÁO NỀN TẢNG
    @GetMapping("/reports/platform")
    public Map<String, Object> report() {
        return service.platformReport();
    }
}
