package com.example.carboncredit.service;

import com.example.carboncredit.entity.Certificate;
import com.example.carboncredit.entity.Transaction;
import com.example.carboncredit.repository.CertificateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CertificateService {
    
    private final CertificateRepository certificateRepository;
    
    public Certificate generateCertificate(Transaction transaction) {
        Certificate certificate = new Certificate();
        certificate.setBuyer(transaction.getBuyer());
        certificate.setTransaction(transaction);
        certificate.setCertifiedCo2Reduction(transaction.getQuantity() * 1000.0);
        certificate.setExpiryDate(LocalDateTime.now().plusYears(2));
        
        return certificateRepository.save(certificate);
    }
    
    public Certificate getCertificateByTransaction(Long transactionId) {
        return certificateRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));
    }
}