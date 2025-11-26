package com.example.evcarbon.service;
//QUẢN LÝ GIAO DỊCH TÍN CHỈ CARBON

import com.example.evcarbon.model.CarbonCredit;
import com.example.evcarbon.model.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TransactionService {

    private final List<Transaction> transactions = new ArrayList<>();
    private final List<CarbonCredit> credits = new ArrayList<>();
    private Long idCounter = 1L;

    public TransactionService() {
        credits.add(new CarbonCredit(1L, "Credit A", 50.0, 100, "Hà Nội"));
        credits.add(new CarbonCredit(2L, "Credit B", 70.0, 60, "HCM"));
    }

    public List<CarbonCredit> searchCredits(String region, Double maxPrice) {
        return credits.stream()
                .filter(c -> (region == null || c.getRegion().equalsIgnoreCase(region)) &&
                             (maxPrice == null || c.getPrice() <= maxPrice))
                .toList();
    }

    public Transaction buy(Long buyerId, Long creditId, int quantity, String paymentMethod) {
        CarbonCredit credit = credits.stream()
                .filter(c -> c.getId() == creditId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Credit not found"));

        if (credit.getQuantity() < quantity) {
            throw new RuntimeException("Not enough credits available");
        }

        double total = quantity * credit.getPrice();

        // trừ tồn
        credit.setQuantity(credit.getQuantity() - quantity);

        // tạo giao dịch
        Transaction tx = new Transaction(
                idCounter++,
                buyerId,
                999L,          // seller demo
                creditId,
                quantity,
                credit.getPrice(),
                total,
                paymentMethod,
                "SUCCESS",
                "https://certificates.example/" + UUID.randomUUID(),
                LocalDateTime.now()
        );

        transactions.add(tx);
        return tx;
    }

    public List<Transaction> history(Long buyerId) {
        return transactions.stream()
                .filter(t -> t.getBuyerId().equals(buyerId))
                .toList();
    }
}
