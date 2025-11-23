package com.example.carboncredit.dto;

import java.math.BigDecimal;

public class ListingRequestDTO {
    private Long creditId;
    private String listingType;
    private BigDecimal price;
    private Integer quantity;
    private Integer auctionDurationHours;

    public Long getCreditId() {
        return creditId;
    }

    public void setCreditId(Long creditId) {
        this.creditId = creditId;
    }

    public String getListingType() {
        return listingType;
    }

    public void setListingType(String listingType) {
        this.listingType = listingType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getAuctionDurationHours() {
        return auctionDurationHours;
    }

    public void setAuctionDurationHours(Integer auctionDurationHours) {
        this.auctionDurationHours = auctionDurationHours;
    }
}