package com.example.evcarbon.controller;
//API CỦA AUDIT

import com.example.evcarbon.model.VerificationRequest;
import com.example.evcarbon.model.AuditReport;
import com.example.evcarbon.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/verification")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService service;

    // Chủ xe gửi yêu cầu audit
    @PostMapping("/request")
    public VerificationRequest submit(
            @RequestParam Long ownerId,
            @RequestParam double emissionReduced,
            @RequestParam int credits) {
        return service.submitRequest(ownerId, emissionReduced, credits);
    }

    // Auditor duyệt
    @PostMapping("/approve")
    public VerificationRequest approve(
            @RequestParam Long requestId,
            @RequestParam Long auditorId) {
        return service.approve(requestId, auditorId);
    }

    // Auditor từ chối
    @PostMapping("/reject")
    public VerificationRequest reject(
            @RequestParam Long requestId,
            @RequestParam Long auditorId,
            @RequestParam String reason) {
        return service.reject(requestId, auditorId, reason);
    }

    // List yêu cầu chờ duyệt
    @GetMapping("/pending")
    public List<VerificationRequest> pending() {
        return service.getPending();
    }

    // Báo cáo audit
    @GetMapping("/reports")
    public List<AuditReport> reports() {
        return service.getReports();
    }
}
