package com.example.evcarbon.service;

import com.example.evcarbon.dto.AiSuggestRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiSuggestService {

    private static final Logger log = LoggerFactory.getLogger(AiSuggestService.class);

    @Value("${gemini.api.key}")
    private String apiKey;
    @Value("${gemini.api.model}")
    private String model;
    @Value("${gemini.api.url}")
    private String apiUrl;
    @Value("${gemini.api.temperature:0.7}")
    private double temperature;
    @Value("${gemini.api.max-tokens:1024}")
    private int maxTokens;

    private final RestTemplate restTemplate = new RestTemplate();

    public String suggestPrice(AiSuggestRequest request) {
        String prompt = buildPrompt(request);
        Map<String, Object> payload = Map.of(
                "contents", List.of(Map.of(
                        "parts", List.of(Map.of("text", prompt))
                )),
                "generationConfig", Map.of(
                        "temperature", temperature,
                        "maxOutputTokens", maxTokens
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String endpoint = apiUrl.replace("{model}", model) + "?key=" + apiKey;
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    endpoint,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<>() {}
            );
            return extractText(response.getBody());
        } catch (Exception e) {
            log.error("Gemini API error: {}", e.getMessage(), e);
            return "Không thể gọi AI ngay lúc này. Vui lòng kiểm tra cấu hình API hoặc thử lại sau.";
        }
    }

    public String chat(String message) {
        Map<String, Object> payload = Map.of(
                "contents", List.of(Map.of(
                        "parts", List.of(Map.of("text", "Trả lời ngắn gọn bằng tiếng Việt cho câu hỏi: " + message))
                )),
                "generationConfig", Map.of(
                        "temperature", temperature,
                        "maxOutputTokens", maxTokens
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String endpoint = apiUrl.replace("{model}", model) + "?key=" + apiKey;
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    endpoint,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<>() {}
            );
            return extractText(response.getBody());
        } catch (Exception e) {
            log.error("Gemini chat error: {}", e.getMessage(), e);
            return "AI hiện không phản hồi, thử lại sau.";
        }
    }

    private String buildPrompt(AiSuggestRequest req) {
        return """
                Bạn là chuyên gia định giá tín chỉ carbon. Hãy đề xuất giá bán khuyến nghị và giải thích ngắn gọn dựa trên dữ liệu:
                - Credits: %.2f
                - Target revenue: %.2f USD
                - Region: %s
                - Carbon type: %s
                - Ghi chú thêm: %s

                Trả lời bằng tiếng Việt, định dạng thành bullet ngắn gọn (giá khuyến nghị, biên độ cao/thấp, nhận định cung cầu).
                """.formatted(
                req.getCredits(),
                req.getTargetRevenue(),
                defaultText(req.getRegion()),
                defaultText(req.getCarbonType()),
                defaultText(req.getNotes())
        );
    }

    private String defaultText(String value) {
        return (value == null || value.isBlank()) ? "Không cung cấp" : value;
    }

    @SuppressWarnings("unchecked")
    private String extractText(Map<String, Object> body) {
        if (body == null) return "Không nhận được phản hồi từ AI.";
        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) body.get("candidates");
            if (candidates == null || candidates.isEmpty()) return "AI không trả về gợi ý.";
            Map<String, Object> first = candidates.get(0);
            Map<String, Object> content = (Map<String, Object>) first.get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            Map<String, Object> part0 = parts.get(0);
            return String.valueOf(part0.get("text"));
        } catch (Exception e) {
            log.warn("Không parse được phản hồi AI: {}", e.getMessage());
            return "Không parse được phản hồi AI.";
        }
    }
}

