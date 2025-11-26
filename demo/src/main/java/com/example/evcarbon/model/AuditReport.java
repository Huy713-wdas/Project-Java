package com.example.evcarbon.model;
//BÁO CÁO AUDIT

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditReport {
    private Long id;//ID BÁO CÁO
    private Long verificationId;//ID YÊU CẦU XÁC MINH
    private Long auditorId;//ID NGƯỜI KIỂM TOÁN
    private String conclusion;     // VERIFIED, FRAUD, DATA_INSUFFICIENT ...
    private String documentUrl;    // file chứng nhận
    private LocalDateTime createdAt;//THỜI GIÁN TẠO
}
