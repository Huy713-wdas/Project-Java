package com.example.evcarbon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarbonCredit {
    private long id;
    private String name;
    private double price;
    private int quantity;
    private String region;

}
