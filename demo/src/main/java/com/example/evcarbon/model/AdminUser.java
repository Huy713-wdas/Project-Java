package com.example.evcarbon.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUser {
    private Long id;
    private String username;
    private String role;  // ADMIN, EV_OWNER, BUYER, VERIFIER
    private boolean active;
    private double walletBalance;
}
