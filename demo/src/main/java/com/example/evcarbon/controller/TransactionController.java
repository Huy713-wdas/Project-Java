package com.example.evcarbon.controller;

import com.example.evcarbon.model.CarbonCredit;
import com.example.evcarbon.model.Transaction;
import com.example.evcarbon.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService txService;

    @GetMapping("/credits")
    public List<CarbonCredit> search(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Double maxPrice) {
        return txService.searchCredits(region, maxPrice);
    }

    @PostMapping("/buy")
    public Transaction buy(
            @RequestParam Long buyerId,
            @RequestParam Long creditId,
            @RequestParam int quantity,
            @RequestParam(defaultValue = "BANKING") String paymentMethod) {
        return txService.buy(buyerId, creditId, quantity, paymentMethod);
    }

    @GetMapping("/history")
    public List<Transaction> history(@RequestParam Long buyerId) {
        return txService.history(buyerId);
    }
}
