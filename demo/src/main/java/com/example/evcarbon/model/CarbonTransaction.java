package com.example.evcarbon.model;
//GIAO DỊCH CARBON

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "carbon_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarbonTransaction {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;//ID GIAO DỊCH
private Long buyerId;//ID NGƯỜI MUA
private Long sellerId;//ID NGƯỜI BÁN
private Long listingId;//ID DÁNH SÁCH
private double credits;//SỐ LƯỢNG TÍN CHỈ
private double totalPrice;//TỔNG GIÁ TIỀN
private String status; // PENDING, CANCELED, COMPLETED
private LocalDateTime createdAt;//THỜI GIAN TẠO
}