package com.example.evcarbon.controller;


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


@PostMapping("/create")
public ResponseEntity<CarbonListing> create(@RequestBody CarbonListing listing) {
listing.setCreatedAt(LocalDateTime.now());
listing.setActive(true);
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