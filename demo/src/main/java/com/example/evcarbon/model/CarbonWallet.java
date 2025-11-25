package com.example.evcarbon.model;


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

private Long ownerId; // same as EV owner id
private double balance; // in credits (1 credit = 1 kg CO2)
}