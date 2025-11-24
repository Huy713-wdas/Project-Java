package com.carbonmarket.model;

public class CarbonCredit {
    private String creditId;
    private String evOwnerId;
    private double co2ReductionKg;
    private int creditAmount; // Lượng tín chỉ (1 tín chỉ = 1 tấn CO2e)
    private long timestamp;
    private String status; // Ví dụ: PENDING_VERIFICATION, APPROVED, REJECTED, ISSUED

    public CarbonCredit(String creditId, String evOwnerId, double co2ReductionKg) {
        this.creditId = creditId;
        this.evOwnerId = evOwnerId;
        this.co2ReductionKg = co2ReductionKg;
        // Giả sử 1000 kg CO2 giảm phát thải tương đương 1 tín chỉ (1 tấn)
        this.creditAmount = (int) (co2ReductionKg / 1000); 
        this.timestamp = System.currentTimeMillis();
        this.status = "PENDING_VERIFICATION";
    }

    // --- Getters và Setters ---

    public String getCreditId() {
        return creditId;
    }

    public String getEvOwnerId() {
        return evOwnerId;
    }

    public double getCo2ReductionKg() {
        return co2ReductionKg;
    }

    public int getCreditAmount() {
        return creditAmount;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Credit ID: %s, EV Owner: %s, CO2 Reduced: %.2f kg, Credits: %d, Status: %s",
                             creditId, evOwnerId, co2ReductionKg, creditAmount, status);
    }
}
