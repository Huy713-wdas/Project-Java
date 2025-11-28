package com.example.evcarbon.dto;

import lombok.Data;

@Data
public class AiSuggestRequest {
    private double credits;         // số lượng tín chỉ muốn bán
    private double targetRevenue;   // doanh thu mong muốn (USD)
    private String region;          // khu vực
    private String carbonType;      // loại tín chỉ / dự án
    private String notes;           // mô tả thêm
}

