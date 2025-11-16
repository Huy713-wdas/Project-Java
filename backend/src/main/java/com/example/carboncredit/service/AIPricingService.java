package com.example.carboncredit.service;

import com.example.carboncredit.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AIPricingService {
    
    private final TransactionRepository transactionRepository;
    
    public BigDecimal suggestListingPrice(Double creditAmount, String creditType) {
        Double basePrice = getMarketBasePrice();
        Double typeMultiplier = getTypeMultiplier(creditType);
        Double volumeAdjustment = getVolumeAdjustment(creditAmount);
        
        Double suggestedPrice = basePrice * typeMultiplier * volumeAdjustment;
        
        return BigDecimal.valueOf(suggestedPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    private Double getMarketBasePrice() {
        Double avgPrice = transactionRepository
                .findAll()
                .stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.COMPLETED)
                .mapToDouble(t -> t.getUnitPrice().doubleValue())
                .average()
                .orElse(15.0);
        
        return Math.max(10.0, Math.min(avgPrice * 1.1, 25.0));
    }
    
    private Double getTypeMultiplier(String creditType) {
        return switch (creditType) {
            case "PREMIUM" -> 1.2;
            case "VERIFIED" -> 1.15;
            default -> 1.0;
        };
    }
    
    private Double getVolumeAdjustment(Double volume) {
        if (volume > 1000) return 0.95;
        if (volume > 500) return 0.97;
        if (volume > 100) return 0.99;
        return 1.0;
    }
}