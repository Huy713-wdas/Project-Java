package com.example.evcarbon.model;
//HÀNH TRÌNH VÀ GIẢM THẢI CO2

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

    private String ownerId; // ID CHỦ SỠ HỮU
    private double distanceKm;// QUÃNG ĐƯỜNG DI CHUYỂN
    private double savedKgCO2; // GIẢM THIỂU CO2 (KG)
    private double credits; // CREDIT (TÍN CHỈ)
    private LocalDateTime timestamp;// THỜI GIAN
}