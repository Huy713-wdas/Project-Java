package com.example.carboncredit.service;

import com.example.carboncredit.entity.*;
import com.example.carboncredit.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final CreditListingRepository listingRepository;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final CertificateService certificateService;
    
    @Value("${app.carbon-credit.commission-rate:0.05}")
    private Double commissionRate;
    
    @Transactional
    public Transaction purchaseCredits(Long listingId, Integer quantity, Long buyerId, Transaction.PaymentMethod paymentMethod) {
        CreditListing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new RuntimeException("Listing not found"));
        
        if (listing.getStatus() != CreditListing.ListingStatus.ACTIVE) {
            throw new RuntimeException("Listing is not active");
        }
        
        if (listing.getAvailableQuantity() < quantity) {
            throw new RuntimeException("Insufficient credits available");
        }
        
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));
        
        Wallet buyerWallet = walletRepository.findByUserId(buyerId)
                .orElseThrow(() -> new RuntimeException("Buyer wallet not found"));
        
        BigDecimal totalAmount = listing.getListingPrice().multiply(BigDecimal.valueOf(quantity));
        BigDecimal platformFee = totalAmount.multiply(BigDecimal.valueOf(commissionRate));
        BigDecimal sellerAmount = totalAmount.subtract(platformFee);
        
        if (buyerWallet.getCashBalance().compareTo(totalAmount) < 0) {
            throw new RuntimeException("Insufficient cash balance");
        }
        
        Transaction transaction = new Transaction();
        transaction.setBuyer(buyer);
        transaction.setSeller(listing.getCarbonCredit().getOwner());
        transaction.setListing(listing);
        transaction.setQuantity(quantity);
        transaction.setUnitPrice(listing.getListingPrice());
        transaction.setTotalAmount(totalAmount);
        transaction.setPlatformFee(platformFee);
        transaction.setPaymentMethod(paymentMethod);
        
        buyerWallet.setCashBalance(buyerWallet.getCashBalance().subtract(totalAmount));
        walletRepository.save(buyerWallet);
        
        Wallet sellerWallet = walletRepository.findByUserId(listing.getCarbonCredit().getOwner().getId())
                .orElseThrow(() -> new RuntimeException("Seller wallet not found"));
        sellerWallet.setCashBalance(sellerWallet.getCashBalance().add(sellerAmount));
        walletRepository.save(sellerWallet);
        
        listing.setAvailableQuantity(listing.getAvailableQuantity() - quantity);
        if (listing.getAvailableQuantity() == 0) {
            listing.setStatus(CreditListing.ListingStatus.SOLD);
        }
        listingRepository.save(listing);
        
        transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
        transaction.setCompletedAt(LocalDateTime.now());
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        certificateService.generateCertificate(savedTransaction);
        
        return savedTransaction;
    }
    
    public List<Transaction> getBuyerTransactions(Long buyerId) {
        return transactionRepository.findByBuyerId(buyerId);
    }
    
    public List<Transaction> getSellerTransactions(Long sellerId) {
        return transactionRepository.findBySellerId(sellerId);
    }
}