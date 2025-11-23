package com.example.carboncredit.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class CarbonCreditDTO {
    private Long id;
    private Long ownerId;
    private Double creditAmount;
    private Double co2ReducedKg;
    private String status;
    private String type;
    private BigDecimal price;
    private LocalDateTime generatedAt;

    public CarbonCreditDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Double getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(Double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public Double getCo2ReducedKg() {
        return co2ReducedKg;
    }

    public void setCo2ReducedKg(Double co2ReducedKg) {
        this.co2ReducedKg = co2ReducedKg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CarbonCreditDTO that = (CarbonCreditDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(ownerId, that.ownerId) &&
                Objects.equals(creditAmount, that.creditAmount) &&
                Objects.equals(co2ReducedKg, that.co2ReducedKg) &&
                Objects.equals(status, that.status) &&
                Objects.equals(type, that.type) &&
                Objects.equals(price, that.price) &&
                Objects.equals(generatedAt, that.generatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerId, creditAmount, co2ReducedKg, status, type, price, generatedAt);
    }

    @Override
    public String toString() {
        return "CarbonCreditDTO{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", creditAmount=" + creditAmount +
                ", co2ReducedKg=" + co2ReducedKg +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", generatedAt=" + generatedAt +
                '}';
    }
}