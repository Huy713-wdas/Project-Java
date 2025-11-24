package com.example.evcarbon.controller;


import com.example.evcarbon.model.EVTripData;
import com.example.evcarbon.service.EVDataLoaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;


@RestController
@RequestMapping("/api/ev")
@RequiredArgsConstructor
public class EVDataController {
private final EVDataLoaderService loader;


// upload CSV to simulate EV connection
@PostMapping(value = "/upload/{ownerId}")
public ResponseEntity<List<EVTripData>> uploadCsv(@PathVariable Long ownerId, @RequestParam("file") MultipartFile file) throws Exception {
List<EVTripData> trips = loader.loadFromCsv(file, ownerId);
return ResponseEntity.ok(trips);
}
}