package com.example.evcarbon.service;
//HÀNH TRÌNH VÀ GIẢM THẢI CCO2

import com.example.evcarbon.model.Journey;
import com.example.evcarbon.repository.JourneyRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class JourneyService {
private final JourneyRepository repo;
private final CarbonWalletService walletService;

// 200 GRAM CO2 TRÊN 1 KM 
private final double baselineGperKm = 200.0; 

// KHỞI TẠO
public JourneyService(JourneyRepository repo, CarbonWalletService walletService) { 
    this.repo = repo; 
    this.walletService = walletService;
}

// PHÂN TÍCH VÀ LƯU HÀNH TRÌNH TỪ FILE CSV
public List<Journey> parseAndSaveCsv(MultipartFile file, String ownerId) {
List<Journey> out = new ArrayList<>();
try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
String line;
// 
br.readLine(); // skip header if exists
while ((line = br.readLine()) != null) {
String[] cols = line.split(",");
double distance = Double.parseDouble(cols[1]);
Journey j = new Journey();
j.setOwnerId(ownerId);
j.setDistanceKm(distance);
double savedKg = (baselineGperKm/1000.0) * distance; // g->kg
j.setSavedKgCO2(savedKg);
double credits = savedKg / 1000.0; // tons
j.setCredits(credits);
j.setTimestamp(LocalDateTime.now());
repo.save(j);
out.add(j);
walletService.addCredits(parseOwner(ownerId), credits);
}
} catch (Exception e) {
throw new RuntimeException("CSV parse error: " + e.getMessage());
}
return out;
}

// HÀNH TRÌNH THEO CHỦ SỞ HỨU
public List<Journey> findByOwner(String ownerId) {
return repo.findByOwnerId(ownerId);
}

private Long parseOwner(String ownerId) {
    try {
        return Long.parseLong(ownerId);
    } catch (NumberFormatException e) {
        throw new RuntimeException("Owner ID phải là số để đồng bộ ví");
    }
}
}