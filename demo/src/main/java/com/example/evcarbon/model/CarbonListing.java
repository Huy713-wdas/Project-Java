package com.example.evcarbon.model;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "carbon_listing")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarbonListing {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private Long ownerId;
private double credits;
private Double price; // fixed price per credit (if auction=false)
private boolean auction;
private LocalDateTime createdAt;
private boolean active;
}