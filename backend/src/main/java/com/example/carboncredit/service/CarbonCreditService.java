package com.example.carboncredit.service;

import com.example.carboncredit.entity.CarbonCredit;
import com.example.carboncredit.entity.TravelData;
import com.example.carboncredit.entity.User;
import com.example.carboncredit.entity.Wallet;
import com.example.carboncredit.repository.CarbonCreditRepository;
import com.example.carboncredit.repository.TravelDataRepository;
import com.example.carboncredit.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarbonCreditService {
    
    private final CarbonCreditRepository carbonCreditRepository;
    private final TravelDataRepository travelDataRepository;
    private final WalletRepository walletRepository;
    
    @Value("${app.carbon-credit.conversion-rate:0.85}")
    private Double conversionRate;
    
    @Transactional
    public CarbonCredit generateCarbonCredit(Long travelDataId, User verifier) {
        TravelData travelData = travelDataRepository.findById(travelDataId)
                .orElseThrow(() -> new RuntimeException("Travel data not found"));
        
        if (travelData.getStatus() != TravelData.TravelStatus.VERIFIED) {
            throw new RuntimeException("Travel data must be verified before generating credits");
        }
        
        Double creditAmount = travelData.getCo2ReducedKg() * conversionRate / 1000;
        
        CarbonCredit carbonCredit = new CarbonCredit();
        carbonCredit.setOwner(travelData.getVehicle().getOwner());
        carbonCredit.setTravelData(travelData);
        carbonCredit.setCreditAmount(creditAmount);
        carbonCredit.setCo2ReducedKg(travelData.getCo2ReducedKg());
        carbonCredit.setType(CarbonCredit.CreditType.VERIFIED);
        carbonCredit.setStatus(CarbonCredit.CreditStatus.AVAILABLE);
        carbonCredit.setExpiresAt(LocalDateTime.now().plusYears(1));
        
        CarbonCredit savedCredit = carbonCreditRepository.save(carbonCredit);
        
        updateWalletCarbonBalance(travelData.getVehicle().getOwner().getId(), creditAmount);
        
        return savedCredit;
    }
    
    private void updateWalletCarbonBalance(Long ownerId, Double creditAmount) {
        Wallet wallet = walletRepository.findByUserId(ownerId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        
        BigDecimal newBalance = wallet.getCarbonCreditBalance().add(BigDecimal.valueOf(creditAmount));
        wallet.setCarbonCreditBalance(newBalance);
        walletRepository.save(wallet);
    }
    
    public List<CarbonCredit> getAvailableCreditsByOwner(Long ownerId) {
        return carbonCreditRepository.findByOwnerIdAndStatus(ownerId, CarbonCredit.CreditStatus.AVAILABLE);
    }
    
    public Double getTotalAvailableCredits(Long ownerId) {
        return carbonCreditRepository.getAvailableCreditsByOwner(ownerId);
    }
}