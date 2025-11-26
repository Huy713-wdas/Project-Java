package com.example.evcarbon.model;
 //GIAO DỊCH TÍN CHỈ

import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Transaction {
    private Long id;//ID GIAO DỊCH
    private Long buyerId;//ID NGƯỜI MUA THAM GIA GIAO DỊCH
    private Long sellerId;//ID NGƯỜI BÁN THAM GIA GIAO DỊCH
    private Long creditId;//ID TÍN CHỈ
    private int quantity;//SỐ LƯỢNG TÍN CHỈ
    private double unitPrice;//GIÁ MỖI TÍN CHỈ
    private double totalAmount;//TỔNG SỐ TIỀN
    private String status;//TRẠNG THÁI GIAO DỊCH: PENDING, COMPLETED, CANCELED
    private String paymentmethod;//PHƯƠNG THỨC THANH TOÁN
    private String certificateUrl;//URL GIAO DỊCH
    private LocalDateTime createdAt;//THỜI GIAN TẠO

}
