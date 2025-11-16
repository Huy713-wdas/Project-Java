package com.example.carboncredit.repository;

import com.example.carboncredit.entity.TravelData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TravelDataRepository extends JpaRepository<TravelData, Long> {
    List<TravelData> findByVehicleIdAndStartTimeBetween(Long vehicleId, LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT SUM(t.co2ReducedKg) FROM TravelData t WHERE t.vehicle.owner.id = :ownerId AND t.status = 'VERIFIED'")
    Double getTotalCo2ReducedByOwner(Long ownerId);
    
    List<TravelData> findByStatus(TravelData.TravelStatus status);
}