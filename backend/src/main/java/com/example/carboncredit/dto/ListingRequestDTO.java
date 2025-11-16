package com.example.carboncredit.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ListingRequestDTO {
    private Long creditId;
    private String listingType;
    private BigDecimal price;
    private Integer quantity;
    private Integer auctionDurationHours;
}