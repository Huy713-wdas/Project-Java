package com.example.carboncredit.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "credit_listings")
@Data
public class CreditListing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_id", nullable = false)
    private CarbonCredit carbonCredit;
    
    @Enumerated(EnumType.STRING)
    private ListingType type;
    
    private BigDecimal listingPrice;
    private BigDecimal currentBid;
    private Integer quantity;
    private Integer availableQuantity;
    
    @Enumerated(EnumType.STRING)
    private ListingStatus status = ListingStatus.ACTIVE;
    
    private LocalDateTime listedAt;
    private LocalDateTime auctionEndTime;
    
    @PrePersist
    protected void onCreate() {
        listedAt = LocalDateTime.now();
    }
    
    public enum ListingType {
        FIXED_PRICE, AUCTION
    }
    
    public enum ListingStatus {
        ACTIVE, SOLD, EXPIRED, CANCELLED
    }
}