package com.example.carboncredit.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDTO {
    private Long id;
    private Long buyerId;
    private Long sellerId;
    private Long listingId;
    private Integer quantity;
    private BigDecimal totalAmount;
    private String status;
    private String paymentMethod;
    private LocalDateTime transactionDate;
}