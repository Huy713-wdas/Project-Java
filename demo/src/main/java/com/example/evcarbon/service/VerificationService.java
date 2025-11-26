package com.example.evcarbon.service;
//XÁC MINH VÀ PHÁT HÀNHT TÍN CHỈ

import com.example.evcarbon.model.VerificationRequest;
import com.example.evcarbon.model.AuditReport;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class VerificationService {

    private final List<VerificationRequest> requests = new ArrayList<>();
    private final List<AuditReport> reports = new ArrayList<>();

    private Long requestIdCounter = 1L;
    private Long reportIdCounter = 1L;

    // Chủ xe gửi yêu cầu chứng nhận1
    public VerificationRequest submitRequest(Long ownerId, double emissionReduced, int credits) {
        VerificationRequest req = new VerificationRequest(
                requestIdCounter++,
                ownerId,
                emissionReduced,
                credits,
                "PENDING",
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        requests.add(req);
        return req;
    }

    // Auditor duyệt yêu cầu
    public VerificationRequest approve(Long requestId, Long auditorId) {
        VerificationRequest req = findRequest(requestId);
        req.setStatus("APPROVED");
        req.setUpdatedAt(LocalDateTime.now());

        reports.add(new AuditReport(
                reportIdCounter++,
                requestId,
                auditorId,
                "VERIFIED",
                "https://reports.example/" + UUID.randomUUID(),
                LocalDateTime.now()
        ));

        return req;
    }

    // Auditor từ chối
    public VerificationRequest reject(Long requestId, Long auditorId, String reason) {
        VerificationRequest req = findRequest(requestId);
        req.setStatus("REJECTED");
        req.setNote(reason);
        req.setUpdatedAt(LocalDateTime.now());

        reports.add(new AuditReport(
                reportIdCounter++,
                requestId,
                auditorId,
                "REJECTED",
                null,
                LocalDateTime.now()
        ));

        return req;
    }

    public List<VerificationRequest> getPending() {
        return requests.stream()
                .filter(r -> r.getStatus().equals("PENDING"))
                .toList();
    }

    public List<AuditReport> getReports() {
        return reports;
    }

    private VerificationRequest findRequest(Long id) {
        return requests.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Request not found"));
    }
}
