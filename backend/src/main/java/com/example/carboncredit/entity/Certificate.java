package com.example.carboncredit.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "certificates")
@Data
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;
    
    private String certificateNumber;
    private Double certifiedCo2Reduction;
    private LocalDateTime issueDate;
    private LocalDateTime expiryDate;
    
    @PrePersist
    protected void onCreate() {
        issueDate = LocalDateTime.now();
        certificateNumber = "CC-" + System.currentTimeMillis();
    }
}