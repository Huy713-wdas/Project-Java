package com.example.evcarbon.repository;


import com.example.evcarbon.model.CarbonTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface CarbonTransactionRepository extends JpaRepository<CarbonTransaction, Long> {
List<CarbonTransaction> findByBuyerIdOrSellerId(Long buyerId, Long sellerId);
}