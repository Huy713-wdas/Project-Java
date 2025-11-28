package com.example.evcarbon.controller;

import com.example.evcarbon.model.CarbonTransaction;
import com.example.evcarbon.model.CarbonWallet;
import com.example.evcarbon.model.Journey;
import com.example.evcarbon.repository.CarbonTransactionRepository;
import com.example.evcarbon.repository.JourneyRepository;
import com.example.evcarbon.service.CarbonWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final JourneyRepository journeyRepository;
    private final CarbonTransactionRepository transactionRepository;
    private final CarbonWalletService walletService;

    @GetMapping("/personal")
    public ResponseEntity<Map<String, Object>> personal(@RequestParam String ownerId) {
        List<Journey> journeys = journeyRepository.findByOwnerId(ownerId);
        double distance = journeys.stream().mapToDouble(Journey::getDistanceKm).sum();
        double saved = journeys.stream().mapToDouble(Journey::getSavedKgCO2).sum();
        double credits = journeys.stream().mapToDouble(Journey::getCredits).sum();

        Long numericOwner;
        try {
            numericOwner = Long.parseLong(ownerId);
        } catch (NumberFormatException e) {
            numericOwner = (long) ownerId.hashCode();
        }

        CarbonWallet wallet = walletService.getOrCreate(numericOwner);
        List<CarbonTransaction> sales = transactionRepository.findBySellerId(numericOwner);
        double revenue = sales.stream().mapToDouble(CarbonTransaction::getTotalPrice).sum();

        return ResponseEntity.ok(Map.of(
                "ownerId", ownerId,
                "totalDistanceKm", distance,
                "totalSavedKgCO2", saved,
                "totalCreditsFromJourneys", credits,
                "walletCredits", wallet.getBalance(),
                "walletCash", wallet.getCashBalance(),
                "totalSalesRevenue", revenue,
                "journeyCount", journeys.size()
        ));
    }
}

