package com.example.evcarbon.repository;


import com.example.evcarbon.model.Journey;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface JourneyRepository extends JpaRepository<Journey, Long> {
List<Journey> findByOwnerId(String ownerId);
}