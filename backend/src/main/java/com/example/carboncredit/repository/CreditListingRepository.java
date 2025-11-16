package com.example.carboncredit.repository;

import com.example.carboncredit.entity.CreditListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CreditListingRepository extends JpaRepository<CreditListing, Long> {
    List<CreditListing> findByStatus(CreditListing.ListingStatus status);
    
    @Query("SELECT cl FROM CreditListing cl WHERE cl.status = 'ACTIVE' AND " +
           "(:type IS NULL OR cl.type = :type) AND " +
           "(:minPrice IS NULL OR cl.listingPrice >= :minPrice) AND " +
           "(:maxPrice IS NULL OR cl.listingPrice <= :maxPrice)")
    List<CreditListing> findActiveListingsFiltered(CreditListing.ListingType type, 
                                                  Double minPrice, Double maxPrice);
    
    List<CreditListing> findByStatusAndAuctionEndTimeBefore(CreditListing.ListingStatus status, LocalDateTime time);
}