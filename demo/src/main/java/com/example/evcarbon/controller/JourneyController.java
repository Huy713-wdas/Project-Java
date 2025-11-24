package com.example.evcarbon.controller;


import com.example.evcarbon.model.Journey;
import com.example.evcarbon.service.JourneyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/journeys")
public class JourneyController {
private final JourneyService service;
public JourneyController(JourneyService service) { this.service = service; }


@PostMapping("/upload-csv")
public ResponseEntity<List<Journey>> uploadCsv(@RequestParam("file") MultipartFile file, @RequestParam String ownerId) {
List<Journey> list = service.parseAndSaveCsv(file, ownerId);
return ResponseEntity.ok(list);
}


@GetMapping
public List<Journey> all(@RequestParam String ownerId) {
return service.findByOwner(ownerId);
}
}