package com.example.evcarbon.controller;
//HIỂN THỊ DANH SÁCH TÍN CHỈ CÁ NHÂN

import com.example.evcarbon.model.CarbonListing;
import com.example.evcarbon.repository.CarbonListingRepository;
import com.example.evcarbon.service.CarbonWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/listings")
@RequiredArgsConstructor
public class ListingController {
    private final CarbonListingRepository listingRepository;
    private final CarbonWalletService walletService;

    @PostMapping("/create")
    public ResponseEntity<CarbonListing> create(@RequestBody CarbonListing listing) {
        if (listing.getCredits() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credits phải lớn hơn 0");
        }
        boolean reserved = walletService.deductCredits(listing.getOwnerId(), listing.getCredits());
        if (!reserved) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ví không đủ tín chỉ để niêm yết");
        }

        listing.setCreatedAt(LocalDateTime.now());
        listing.setActive(true);
        listing.setRemainingCredits(listing.getCredits());
        if (listing.getRegion() == null) listing.setRegion("Không xác định");
        if (listing.getTitle() == null || listing.getTitle().isBlank()) {
            listing.setTitle("Listing #" + listing.getOwnerId());
        }

        CarbonListing saved = listingRepository.save(listing);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/active")
    public ResponseEntity<List<CarbonListing>> active() {
        return ResponseEntity.ok(listingRepository.findByActiveTrue());
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<CarbonListing>> byOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(listingRepository.findByOwnerId(ownerId));
    }
}