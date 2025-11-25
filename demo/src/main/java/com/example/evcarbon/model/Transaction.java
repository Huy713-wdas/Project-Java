package com.example.evcarbon.model;

import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Transaction {
    private Long id;
    private Long buyerId;
    private Long sellerId;
    private Long creditId;
    private int quantity;
    private double unitPrice;
    private double totalAmount;
    private String status;
    private String paymentmethod;
    private String certificateUrl;
    private LocalDateTime createdAt;

}
