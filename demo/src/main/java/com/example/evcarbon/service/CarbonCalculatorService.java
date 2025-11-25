package com.example.evcarbon.service;


import org.springframework.stereotype.Service;


@Service
public class CarbonCalculatorService {
private static final double CO2_PER_KM = 0.192; // kg CO2 per km for ICE baseline


public double calcCO2kgFromKm(double km) {
return km * CO2_PER_KM;
}


public double convertToCredits(double co2kg) {
return co2kg; // 1 credit = 1 kg CO2
}
}