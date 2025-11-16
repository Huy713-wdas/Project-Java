package com.example.carboncredit.service;

import com.example.carboncredit.dto.TravelDataDTO;
import com.example.carboncredit.entity.TravelData;
import com.example.carboncredit.entity.ElectricVehicle;
import com.example.carboncredit.repository.TravelDataRepository;
import com.example.carboncredit.repository.ElectricVehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelDataService {
    
    private final TravelDataRepository travelDataRepository;
    private final ElectricVehicleRepository vehicleRepository;
    private final CarbonCalculationService carbonCalculationService;
    
    @Transactional
    public TravelData recordTravelData(TravelDataDTO travelDataDTO) {
        ElectricVehicle vehicle = vehicleRepository.findById(travelDataDTO.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        
        TravelData travelData = new TravelData();
        travelData.setVehicle(vehicle);
        travelData.setStartTime(travelDataDTO.getStartTime());
        travelData.setEndTime(travelDataDTO.getEndTime());
        travelData.setDistanceKm(travelDataDTO.getDistanceKm());
        travelData.setEnergyConsumedKwh(travelDataDTO.getEnergyConsumedKwh());
        
        Double co2Reduced = carbonCalculationService.calculateCO2Reduction(
            travelDataDTO.getDistanceKm(), 
            travelDataDTO.getEnergyConsumedKwh()
        );
        travelData.setCo2ReducedKg(co2Reduced);
        
        return travelDataRepository.save(travelData);
    }
    
    public List<TravelData> getTravelDataByVehicle(Long vehicleId, LocalDateTime start, LocalDateTime end) {
        return travelDataRepository.findByVehicleIdAndStartTimeBetween(vehicleId, start, end);
    }
    
    public Double getTotalCo2ReducedByOwner(Long ownerId) {
        return travelDataRepository.getTotalCo2ReducedByOwner(ownerId);
    }
    
    public List<TravelData> getPendingVerification() {
        return travelDataRepository.findByStatus(TravelData.TravelStatus.PENDING);
    }
}