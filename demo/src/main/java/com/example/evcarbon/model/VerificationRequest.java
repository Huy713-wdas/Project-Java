package com.example.evcarbon.model;
//YÊU CẦU XÁC MINH TÍN CHỈ
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationRequest {
    private Long id;
    private Long ownerId;         // người nộp yêu cầu
    private double emissionReduced;  // CO2 giảm (kg/ton)
    private int requestedCredits; // số tín chỉ cần cấp
    private String status;        // PENDING, APPROVED, REJECTED
    private String note;          // lý do từ chối hoặc ghi chú auditor
    private LocalDateTime createdAt;//THỜI GIAN TẠO
    private LocalDateTime updatedAt;//THỜI GIAN CẬP NHẬT
}
