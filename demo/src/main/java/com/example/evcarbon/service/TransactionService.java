package com.example.evcarbon.service;

import com.example.evcarbon.model.CarbonListing;
import com.example.evcarbon.model.CarbonTransaction;
import com.example.evcarbon.repository.CarbonListingRepository;
import com.example.evcarbon.repository.CarbonTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final CarbonListingRepository listingRepository;
    private final CarbonTransactionRepository transactionRepository;
    private final CarbonWalletService walletService;

    public List<CarbonListing> searchCredits(String region, Double maxPrice) {
        return listingRepository.findByActiveTrue().stream()
                .filter(listing -> listing.getRemainingCredits() > 0)
                .filter(listing -> region == null || listing.getRegion().equalsIgnoreCase(region))
                .filter(listing -> maxPrice == null || listing.getPrice() <= maxPrice)
                .toList();
    }

    public CarbonTransaction buy(Long buyerId, Long listingId, int quantity, String paymentMethod) {
        CarbonListing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Listing không tồn tại"));

        if (!listing.isActive() || listing.getRemainingCredits() < quantity) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Listing không đủ tín chỉ");
        }

        listing.setRemainingCredits(listing.getRemainingCredits() - quantity);
        if (listing.getRemainingCredits() <= 0) {
            listing.setActive(false);
        }
        listingRepository.save(listing);

        // cộng tín chỉ cho người mua và tiền mặt cho người bán
        walletService.addCredits(buyerId, quantity);
        walletService.addCash(listing.getOwnerId(), quantity * listing.getPrice());

        CarbonTransaction tx = CarbonTransaction.builder()
                .buyerId(buyerId)
                .sellerId(listing.getOwnerId())
                .listingId(listing.getId())
                .quantity(quantity)
                .unitPrice(listing.getPrice())
                .totalPrice(quantity * listing.getPrice())
                .paymentMethod(paymentMethod)
                .status("SUCCESS")
                .certificateUrl("https://certificates.example/" + UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .build();

        return transactionRepository.save(tx);
    }

    public List<CarbonTransaction> history(Long buyerId) {
        return transactionRepository.findByBuyerIdOrSellerId(buyerId, buyerId);
    }
}
