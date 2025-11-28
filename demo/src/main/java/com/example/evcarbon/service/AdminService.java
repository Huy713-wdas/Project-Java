package com.example.evcarbon.service;

import com.example.evcarbon.model.*;
import com.example.evcarbon.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository repo;

    private long userIdCounter = 1;
    private long txIdCounter = 1;

    // --- User Management ---
    public AdminUser addUser(String username, String role) {
        AdminUser user = new AdminUser(userIdCounter++, username, role, true, 0.0);
        repo.users.add(user);
        return user;
    }

    public List<AdminUser> getUsers() {
        return repo.users;
    }

    public void toggleUserStatus(Long id, boolean active) {
        repo.users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .ifPresent(u -> u.setActive(active));
    }

    // --- Carbon Credit Listings ---
    public List<CarbonCredit> getCredits() {
        return repo.credits;
    }

    public void removeListing(Long id) {
        repo.credits.removeIf(c -> c.getId().equals(id));
    }

    // --- Transactions ---
    public List<CarbonTransaction> getTransactions() {
        return repo.transactions;
    }

    public CarbonTransaction markDisputed(Long txId) {
        CarbonTransaction tx = repo.transactions.stream()
                .filter(t -> t.getId().equals(txId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        tx.setStatus("DISPUTED");
        return tx;
    }

    // --- Reporting ---
    public Map<String, Object> platformReport() {
        double totalValue = repo.transactions.stream()
                .mapToDouble(CarbonTransaction::getTotalPrice)
                .sum();

        int count = repo.transactions.size();

        return Map.of(
                "totalTransactions", count,
                "totalValue", totalValue,
                "users", repo.users.size(),
                "listings", repo.credits.size()
        );
    }
}
