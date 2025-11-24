package com.example.evcarbon.model;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "carbon_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarbonTransaction {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private Long buyerId;
private Long sellerId;
private Long listingId;
private double credits;
private double totalPrice;
private String status; // PENDING, CANCELED, COMPLETED
private LocalDateTime createdAt;
}