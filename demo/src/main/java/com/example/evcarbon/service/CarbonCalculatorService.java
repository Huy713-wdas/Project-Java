package com.example.evcarbon.service;
//TÍNH TOÁN TÍN CHỈ CARBON

import org.springframework.stereotype.Service;


@Service
public class CarbonCalculatorService {
private static final double CO2_PER_KM = 0.192; //TRƯNG BÌNH 192 GRAM CO2 TRÊN 1 KM


public double calcCO2kgFromKm(double km) {
return km * CO2_PER_KM;//
}


public double convertToCredits(double co2kg) {
return co2kg; // 1 credit = 1 kg CO2
}
}