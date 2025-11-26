package com.example.evcarbon.repository;
//LỮU TRỮ DANH SÁCH TÍN CHỈ CARBON

import com.example.evcarbon.model.CarbonListing;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface CarbonListingRepository extends JpaRepository<CarbonListing, Long> {
List<CarbonListing> findByActiveTrue();
List<CarbonListing> findByOwnerId(Long ownerId);
}