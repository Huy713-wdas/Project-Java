package com.example.carboncredit.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class WalletDTO {
    private Long userId;
    private BigDecimal carbonCreditBalance;
    private BigDecimal cashBalance;
}