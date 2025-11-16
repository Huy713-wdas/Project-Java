package com.example.carboncredit.service;

import com.example.carboncredit.dto.ListingRequestDTO;
import com.example.carboncredit.entity.CarbonCredit;
import com.example.carboncredit.entity.CreditListing;
import com.example.carboncredit.entity.Wallet;
import com.example.carboncredit.repository.CarbonCreditRepository;
import com.example.carboncredit.repository.CreditListingRepository;
import com.example.carboncredit.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditListingService {
    
    private final CreditListingRepository listingRepository;
    private final CarbonCreditRepository carbonCreditRepository;
    private final WalletRepository walletRepository;
    private final AIPricingService aiPricingService;
    
    @Transactional
    public CreditListing createListing(ListingRequestDTO listingRequest) {
        CarbonCredit carbonCredit = carbonCreditRepository.findById(listingRequest.getCreditId())
                .orElseThrow(() -> new RuntimeException("Carbon credit not found"));
        
        if (carbonCredit.getStatus() != CarbonCredit.CreditStatus.AVAILABLE) {
            throw new RuntimeException("Carbon credit is not available for listing");
        }
        
        Wallet wallet = walletRepository.findByUserId(carbonCredit.getOwner().getId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        
        if (wallet.getCarbonCreditBalance().compareTo(BigDecimal.valueOf(listingRequest.getQuantity())) < 0) {
            throw new RuntimeException("Insufficient carbon credit balance");
        }
        
        CreditListing listing = new CreditListing();
        listing.setCarbonCredit(carbonCredit);
        listing.setType(CreditListing.ListingType.valueOf(listingRequest.getListingType()));
        listing.setListingPrice(listingRequest.getPrice());
        listing.setQuantity(listingRequest.getQuantity());
        listing.setAvailableQuantity(listingRequest.getQuantity());
        
        if (listing.getType() == CreditListing.ListingType.AUCTION) {
            listing.setAuctionEndTime(LocalDateTime.now().plusHours(listingRequest.getAuctionDurationHours()));
        }
        
        carbonCredit.setStatus(CarbonCredit.CreditStatus.LISTED);
        carbonCreditRepository.save(carbonCredit);
        
        wallet.setCarbonCreditBalance(wallet.getCarbonCreditBalance()
                .subtract(BigDecimal.valueOf(listingRequest.getQuantity())));
        walletRepository.save(wallet);
        
        return listingRepository.save(listing);
    }
    
    public List<CreditListing> getActiveListings(CreditListing.ListingType type, Double minPrice, Double maxPrice) {
        return listingRepository.findActiveListingsFiltered(type, minPrice, maxPrice);
    }
    
    public CreditListing getListingById(Long listingId) {
        return listingRepository.findById(listingId)
                .orElseThrow(() -> new RuntimeException("Listing not found"));
    }
    
    @Transactional
    public void cancelListing(Long listingId, Long ownerId) {
        CreditListing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new RuntimeException("Listing not found"));
        
        if (!listing.getCarbonCredit().getOwner().getId().equals(ownerId)) {
            throw new RuntimeException("Not authorized to cancel this listing");
        }
        
        listing.setStatus(CreditListing.ListingStatus.CANCELLED);
        listingRepository.save(listing);
        
        CarbonCredit carbonCredit = listing.getCarbonCredit();
        carbonCredit.setStatus(CarbonCredit.CreditStatus.AVAILABLE);
        carbonCreditRepository.save(carbonCredit);
        
        Wallet wallet = walletRepository.findByUserId(ownerId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        wallet.setCarbonCreditBalance(wallet.getCarbonCreditBalance()
                .add(BigDecimal.valueOf(listing.getQuantity())));
        walletRepository.save(wallet);
    }
}