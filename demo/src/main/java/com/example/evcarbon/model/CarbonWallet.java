package com.example.evcarbon.model;
//VÍ TÍN CHỈ CARBON

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "carbon_wallet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarbonWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ownerId;

    private double balance; // carbon credits

    private double cashBalance; // doanh thu quy đổi
}