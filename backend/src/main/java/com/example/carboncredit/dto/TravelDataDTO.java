package com.example.carboncredit.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TravelDataDTO {
    private Long vehicleId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double distanceKm;
    private Double energyConsumedKwh;
}