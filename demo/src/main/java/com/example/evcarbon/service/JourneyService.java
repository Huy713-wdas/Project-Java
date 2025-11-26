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
// baseline grams CO2 per km (configurable)
private final double baselineGperKm = 200.0; // 200 gCO2/km default


public JourneyService(JourneyRepository repo) { this.repo = repo; }


public List<Journey> parseAndSaveCsv(MultipartFile file, String ownerId) {
List<Journey> out = new ArrayList<>();
try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
String line;
// example CSV: timestamp,distance_km
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
}
} catch (Exception e) {
throw new RuntimeException("CSV parse error: " + e.getMessage());
}
return out;
}


public List<Journey> findByOwner(String ownerId) {
return repo.findByOwnerId(ownerId);
}
}