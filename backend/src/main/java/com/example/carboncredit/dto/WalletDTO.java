package com.example.carboncredit.dto;

import java.math.BigDecimal;

public class WalletDTO {
    private BigDecimal carbonCreditBalance;
    private BigDecimal cashBalance;

    public WalletDTO() {
    }

    public BigDecimal getCarbonCreditBalance() {
        return carbonCreditBalance;
    }

    public void setCarbonCreditBalance(BigDecimal carbonCreditBalance) {
        this.carbonCreditBalance = carbonCreditBalance;
    }

    public BigDecimal getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(BigDecimal cashBalance) {
        this.cashBalance = cashBalance;
    }

    @Override
    public String toString() {
        return "WalletDTO{" +
                "carbonCreditBalance=" + carbonCreditBalance +
                ", cashBalance=" + cashBalance +
                '}';
    }
}