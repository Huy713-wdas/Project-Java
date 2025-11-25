package com.example.evcarbon.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Journey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ownerId; // user id or wallet id
    private double distanceKm;
    private double savedKgCO2; // saved CO2 in kg
    private double credits; // credits (tons)
    private LocalDateTime timestamp;
}