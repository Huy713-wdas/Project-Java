package com.example.evcarbon.repository;


import com.example.evcarbon.model.EVTripData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface EVTripRepository extends JpaRepository<EVTripData, Long> {
List<EVTripData> findByOwnerId(Long ownerId);
}