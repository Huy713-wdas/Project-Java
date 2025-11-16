package com.example.carboncredit.controller;

import com.example.carboncredit.entity.CreditListing;
import com.example.carboncredit.entity.Transaction;
import com.example.carboncredit.service.CreditListingService;
import com.example.carboncredit.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buyer")
@RequiredArgsConstructor
public class BuyerController {
    
    private final CreditListingService listingService;
    private final TransactionService transactionService;
    
    @GetMapping("/listings")
    public ResponseEntity<List<CreditListing>> getActiveListings(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        