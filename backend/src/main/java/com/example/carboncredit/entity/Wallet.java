package com.example.carboncredit.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
@Data
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(precision = 15, scale = 4)
    private BigDecimal carbonCreditBalance = BigDecimal.ZERO;
    
    @Column(precision = 15, scale = 2)
    private BigDecimal cashBalance = BigDecimal.ZERO;
    
    @Version
    private Long version;
}