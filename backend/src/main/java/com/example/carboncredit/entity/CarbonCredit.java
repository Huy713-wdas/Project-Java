package com.example.carboncredit.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "carbon_credits")
@Data
public class CarbonCredit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_data_id")
    private TravelData travelData;
    
    private Double creditAmount;
    private Double co2ReducedKg;
    
    @Enumerated(EnumType.STRING)
    private CreditStatus status = CreditStatus.AVAILABLE;
    
    @Enumerated(EnumType.STRING)
    private CreditType type;
    
    private BigDecimal price;
    private LocalDateTime generatedAt;
    private LocalDateTime expiresAt;
    
    @PrePersist
    protected void onCreate() {
        generatedAt = LocalDateTime.now();
    }
    
    public enum CreditStatus {
        AVAILABLE, LISTED, SOLD, EXPIRED, LOCKED
    }
    
    public enum CreditType {
        STANDARD, PREMIUM, VERIFIED
    }
}