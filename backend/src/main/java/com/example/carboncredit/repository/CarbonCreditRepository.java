package com.example.carboncredit.repository;

import com.example.carboncredit.entity.CarbonCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CarbonCreditRepository extends JpaRepository<CarbonCredit, Long> {
    List<CarbonCredit> findByOwnerIdAndStatus(Long ownerId, CarbonCredit.CreditStatus status);
    
    @Query("SELECT SUM(c.creditAmount) FROM CarbonCredit c WHERE c.owner.id = :ownerId AND c.status = 'AVAILABLE'")
    Double getAvailableCreditsByOwner(Long ownerId);
    
    List<CarbonCredit> findByStatus(CarbonCredit.CreditStatus status);
}