package com.example.carboncredit.dto;

import lombok.Data;

@Data
public class ResolveTransactionRequest {
    private String action; // "refund" or "complete" or "adjust"
    private String comment;
}
