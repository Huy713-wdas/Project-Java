package com.example.evcarbon.controller;

import com.example.evcarbon.dto.AiChatRequest;
import com.example.evcarbon.dto.AiSuggestRequest;
import com.example.evcarbon.service.AiSuggestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@CrossOrigin
public class AiController {

    private final AiSuggestService service;

    @PostMapping("/price-suggest")
    public ResponseEntity<Map<String, Object>> suggest(@RequestBody AiSuggestRequest request) {
        String suggestion = service.suggestPrice(request);
        return ResponseEntity.ok(Map.of(
                "suggestion", suggestion,
                "inputCredits", request.getCredits(),
                "targetRevenue", request.getTargetRevenue()
        ));
    }

    @PostMapping("/chat")
    public ResponseEntity<Map<String, Object>> chat(@RequestBody AiChatRequest request) {
        String reply = service.chat(request.getMessage());
        return ResponseEntity.ok(Map.of("reply", reply));
    }
}

