package com.example.evcarbon.model;
//VÍ TÍN CHỈ CARBON

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "carbon_wallet")//VÍ TÍN CHỈ CARBON
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarbonWallet {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)

private Long ownerId; // ID CHỦ SỞ HỮU
private double balance; // SỐ DƯ TÍN CHỈ CARBON
}