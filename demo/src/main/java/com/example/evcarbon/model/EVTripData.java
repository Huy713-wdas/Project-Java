package com.example.evcarbon.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;


@Entity
@Table(name = "ev_trip")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EVTripData {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private String tripId;
private Long ownerId;
private double distanceKm;
private double energyKWh;
private LocalDateTime timestamp;
}