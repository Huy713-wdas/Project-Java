package com.example.carboncredit.controller;

import com.example.carboncredit.dto.TravelDataDTO;
import com.example.carboncredit.dto.ListingRequestDTO;
import com.example.carboncredit.entity.*;
import com.example.carboncredit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
public class EVOwnerController {
    
    private final TravelDataService travelDataService;
    private final CarbonCreditService carbonCreditService;
    private final CreditListingService listingService;
    private final TransactionService transactionService;
    private final WalletService walletService;
    private final AIPricingService aiPricingService;
    
    @PostMapping("/travel-data")
    public ResponseEntity<TravelData> recordTravelData(@RequestBody TravelDataDTO travelDataDTO) {
        TravelData travelData = travelDataService.recordTravelData(travelDataDTO);
        return ResponseEntity.ok(travelData);
    }
    
    @GetMapping("/credits/{ownerId}")
    public ResponseEntity<List<CarbonCredit>> getAvailableCredits(@PathVariable Long ownerId) {
        List<CarbonCredit> credits = carbonCreditService.getAvailableCreditsByOwner(ownerId);
        return ResponseEntity.ok(credits);
    }
    
    @PostMapping("/listings")
    public ResponseEntity<CreditListing> createListing(@RequestBody ListingRequestDTO listingRequest) {
        CreditListing listing = listingService.createListing(listingRequest);
        return ResponseEntity.ok(listing);
    }
    
    @DeleteMapping("/listings/{listingId}")
    public ResponseEntity<Void> cancelListing(@PathVariable Long listingId, @RequestParam Long ownerId) {
        listingService.cancelListing(listingId, ownerId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/wallet/{ownerId}")
    public ResponseEntity<Wallet> getWallet(@PathVariable Long ownerId) {
        Wallet wallet = walletService.getWalletByUserId(ownerId);
        return ResponseEntity.ok(wallet);
    }
    
    @GetMapping("/transactions/seller/{sellerId}")
    public ResponseEntity<List<Transaction>> getSellerTransactions(@PathVariable Long sellerId) {
        List<Transaction> transactions = transactionService.getSellerTransactions(sellerId);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/reports/co2-reduced/{ownerId}")
    public ResponseEntity<Map<String, Object>> getCo2ReductionReport(@PathVariable Long ownerId) {
        Double totalCo2Reduced = travelDataService.getTotalCo2ReducedByOwner(ownerId);
        Double totalRevenue = transactionService.getTotalRevenueBySeller(ownerId);
        
        return ResponseEntity.ok(Map.of(
            "totalCo2ReducedKg", totalCo2Reduced,
            "totalRevenue", totalRevenue != null ? totalRevenue : 0.0
        ));
    }
    
    @GetMapping("/ai-pricing-suggestion")
    public ResponseEntity<Map<String, Object>> getAIPricingSuggestion(
            @RequestParam Double creditAmount, 
            @RequestParam String creditType) {
        
        var suggestedPrice = aiPricingService.suggestListingPrice(creditAmount, creditType);
        
        return ResponseEntity.ok(Map.of(
            "suggestedPrice", suggestedPrice,
            "creditAmount", creditAmount,
            "creditType", creditType
        ));
    }
}