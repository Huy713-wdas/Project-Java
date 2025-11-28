package com.example.evcarbon.dto;

import lombok.Data;

@Data
public class WalletOperationRequest {
    private Long ownerId;
    private double amount;
}

