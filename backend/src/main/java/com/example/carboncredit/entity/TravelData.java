package com.example.carboncredit.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "travel_data")
@Data
public class TravelData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private ElectricVehicle vehicle;
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double distanceKm;
    private Double energyConsumedKwh;
    private Double co2ReducedKg;
    
    @Enumerated(EnumType.STRING)
    private TravelStatus status = TravelStatus.PENDING;
    
    private LocalDateTime recordedAt;
    
    @PrePersist
    protected void onCreate() {
        recordedAt = LocalDateTime.now();
    }
    
    public enum TravelStatus {
        PENDING, VERIFIED, REJECTED
    }
}