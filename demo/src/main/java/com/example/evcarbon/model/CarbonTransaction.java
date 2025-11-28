package com.example.evcarbon.model;
//GIAO DỊCH CARBON

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "carbon_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarbonTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long buyerId;
    private Long sellerId;
    private Long listingId;

    private int quantity;
    private double unitPrice;
    private double totalPrice;

    private String certificateUrl;
    private String paymentMethod;
    private String status;
    private LocalDateTime createdAt;
}