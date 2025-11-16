package com.example.carboncredit.repository;

import com.example.carboncredit.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByBuyerId(Long buyerId);
    List<Transaction> findBySellerId(Long sellerId);
    
    @Query("SELECT SUM(t.totalAmount) FROM Transaction t WHERE t.seller.id = :sellerId AND t.status = 'COMPLETED'")
    Double getTotalRevenueBySeller(Long sellerId);
    
    List<Transaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);
}