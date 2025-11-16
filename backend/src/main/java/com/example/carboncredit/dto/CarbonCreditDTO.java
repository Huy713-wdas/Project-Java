package com.example.carboncredit.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CarbonCreditDTO {
    private Long id;
    private Long ownerId;
    private Double creditAmount;
    private Double co2ReducedKg;
    private String status;
    private String type;
    private BigDecimal price;
    private LocalDateTime generatedAt;
}