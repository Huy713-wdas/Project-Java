package com.example.evcarbon.controller;
//HIỂN THỊ DANH SÁCH TÍN CHỈ CÁ NHÂN

import com.example.evcarbon.model.CarbonListing;
import com.example.evcarbon.repository.CarbonListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/listings")
@RequiredArgsConstructor
public class ListingController {
private final CarbonListingRepository listingRepository;

// TẠO MỚI DANH SÁCH TÍN CHỈ
@PostMapping("/create")
public ResponseEntity<CarbonListing> create(@RequestBody CarbonListing listing) {
listing.setCreatedAt(LocalDateTime.now());
listing.setActive(true);
CarbonListing saved = listingRepository.save(listing);
return ResponseEntity.ok(saved);
}

// LẤY DANH SÁCH TÍN CHỈ ĐANG HOẠT ĐỘNG
@GetMapping("/active")
public ResponseEntity<List<CarbonListing>> active() {
return ResponseEntity.ok(listingRepository.findByActiveTrue());
}

// LẤY DANH SÁCH TÍN CHỈ THEO CHỦ SỠ HỮU
@GetMapping("/owner/{ownerId}")
public ResponseEntity<List<CarbonListing>> byOwner(@PathVariable Long ownerId) {
return ResponseEntity.ok(listingRepository.findByOwnerId(ownerId));
}
}