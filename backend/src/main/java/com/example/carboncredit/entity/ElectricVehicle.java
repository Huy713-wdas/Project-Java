package com.example.carboncredit.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "electric_vehicles")
@Data
public class ElectricVehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    
    private String vehicleModel;
    private String licensePlate;
    private String vin;
    private Double batteryCapacity;
    private Double energyConsumption;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    private LocalDateTime registeredAt;
}