package com.example.evcarbon.model;
//KẾT NỐI VÀ ĐỒNG BỘ HÀNH TRÌNH VỚI XE ĐIỆN

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "carbon_listing")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarbonListing {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;//id của listing
private Long ownerId;//id của chủ sở hữu
private double credits;//số tín chỉ carbon được liệt kê
private Double price; // fixed price per credit (if auction=false)
private boolean auction;
private LocalDateTime createdAt;
private boolean active;//hoạt động 
}