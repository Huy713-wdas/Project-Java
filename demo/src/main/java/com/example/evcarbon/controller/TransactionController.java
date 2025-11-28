package com.example.evcarbon.controller;
//KIỂM SOÁT GIAO DỊCH TÍN CHỈ

import com.example.evcarbon.model.CarbonListing;
import com.example.evcarbon.model.CarbonTransaction;
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
    public List<CarbonListing> search(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Double maxPrice) {
        return txService.searchCredits(region, maxPrice);
    }

    @PostMapping("/buy")
    public CarbonTransaction buy(
            @RequestParam Long buyerId,
            @RequestParam Long listingId,
            @RequestParam int quantity,
            @RequestParam(defaultValue = "BANKING") String paymentMethod) {
        return txService.buy(buyerId, listingId, quantity, paymentMethod);
    }

    @GetMapping("/history")
    public List<CarbonTransaction> history(@RequestParam Long buyerId) {
        return txService.history(buyerId);
    }
}
