package com.example.evcarbon.controller;
//TRẢ DỮ LIỆU HÀNH TRÌNH

import com.example.evcarbon.model.Journey;
import com.example.evcarbon.repository.JourneyRepository;
import com.example.evcarbon.service.JourneyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/journeys")
public class JourneyController {
private final JourneyService service;

 @Autowired
JourneyRepository repo;
//
public JourneyController(JourneyService service) { this.service = service; }


@PostMapping("/upload-csv")
public ResponseEntity<List<Journey>> uploadCsv(@RequestParam("file") MultipartFile file, @RequestParam String ownerId) {
List<Journey> list = service.parseAndSaveCsv(file, ownerId);
return ResponseEntity.ok(list);
}

@GetMapping
public List<Journey> all(@RequestParam(required = false) String ownerId) {
    if (ownerId == null) {
        return repo.findAll();  // hoặc trả [] tùy bạn
    }
    return service.findByOwner(ownerId);
}
}