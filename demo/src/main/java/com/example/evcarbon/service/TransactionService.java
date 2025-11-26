package com.example.evcarbon.service;

import com.example.evcarbon.model.CarbonCredit;
import com.example.evcarbon.model.CarbonTransaction;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service

public class TransactionService {

    private final List<CarbonTransaction> transactions = new ArrayList<>();
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

    public CarbonTransaction buy(Long buyerId, Long creditId, int quantity, String paymentMethod) {
        CarbonCredit credit = credits.stream()
                .filter(c -> c.getId().equals(creditId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Credit not found"));

        if (credit.getQuantity() < quantity) {
            throw new RuntimeException("Not enough credits available");
        }

        double total = quantity * credit.getPrice();

        // trừ tồn
        credit.setQuantity(credit.getQuantity() - quantity);

        // tạo giao dịch
       CarbonTransaction tx = CarbonTransaction.builder()
        .buyerId(buyerId)
        .sellerId(999L)
        .creditId(creditId)
        .quantity(quantity)
        .unitPrice(credit.getPrice())
        .totalPrice(total)
        .paymentMethod(paymentMethod)
        .status("SUCCESS")
        .certificateUrl("https://certificates.example/" + UUID.randomUUID())
        .createdAt(LocalDateTime.now())
        .build();

        ;

        // lưu vào danh sách
        transactions.add(tx);

        return tx;
    }

    public List<CarbonTransaction> history(Long buyerId) {
        return transactions.stream()
                .filter(t -> t.getBuyerId().equals(buyerId))
                .toList();
    }
}
