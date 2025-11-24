package com.example.evcarbon.service;


import com.example.evcarbon.model.EVTripData;
import com.example.evcarbon.model.Journey;
import com.example.evcarbon.repository.EVTripRepository;
import com.example.evcarbon.repository.JourneyRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class EVDataLoaderService {
private final EVTripRepository tripRepository;
private final CarbonCalculatorService calculator;
private final CarbonWalletService walletService;
private final JourneyRepository journeyRepository;


// simple CSV parser: tripId,distanceKm,energyKWh,timestamp
public List<EVTripData> loadFromCsv(MultipartFile file, Long ownerId) throws Exception {
List<EVTripData> saved = new ArrayList<>();
try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
String line;
while ((line = br.readLine()) != null) {
if (line.trim().isEmpty()) continue;
String[] parts = line.split(",");
String tripId = parts[0];
double distance = Double.parseDouble(parts[1]);
double energy = Double.parseDouble(parts[2]);
LocalDateTime ts = LocalDateTime.parse(parts[3]);
EVTripData trip = EVTripData.builder()
.tripId(tripId)
.ownerId(ownerId)
.distanceKm(distance)
.energyKWh(energy)
.timestamp(ts)
.build();
tripRepository.save(trip);


// credit calculation + wallet update
double co2 = calculator.calcCO2kgFromKm(distance);
double credits = calculator.convertToCredits(co2);
walletService.addCredits(ownerId, credits);
Journey journey = new Journey();
journey.setOwnerId(String.valueOf(ownerId)); 
    
journey.setDistanceKm(distance);
journey.setSavedKgCO2(co2);
journey.setCredits(credits);
journey.setTimestamp(ts);

journeyRepository.save(journey); 

saved.add(trip);

}
}
return saved;
}
}