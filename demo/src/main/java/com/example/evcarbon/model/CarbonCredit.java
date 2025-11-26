package com.example.evcarbon.model;
//TÍN CHỈ CARBON

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarbonCredit {
    private Long id;//ID TÍN CHỈ
    private String name;//TÊN TÍN CHỈ
    private double price;//GIÁ TÍN CHỈ
    private int quantity;//SỐ LƯỢNG TÍN CHỈ
    private String region;//VÙNG
    private Long sellerId;//ID NGƯỜI BÁN
}
