package com.example.evcarbon.controller;
//KIỂM SOÁT GIAO DỊCH TÍN CHỈ

import com.example.evcarbon.model.CarbonCredit;
import com.example.evcarbon.model.CarbonTransaction;
import com.example.evcarbon.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService txService;

    @GetMapping("/credits")
    public List<CarbonCredit> search(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Double maxPrice) {
        return txService.searchCredits(region, maxPrice);
    }

    @PostMapping("/buy")
    public CarbonTransaction buy(
            @RequestParam Long buyerId,//ID NGƯỜI MUA
            @RequestParam Long creditId,//ID NGƯỜI BÁN
            @RequestParam int quantity,//SỐ LƯỢNG TÍN CHỈ
            @RequestParam(defaultValue = "BANKING") String paymentMethod) {
        return txService.buy(buyerId, creditId, quantity, paymentMethod);
    }

    @GetMapping("/history")
    public List<CarbonTransaction> history(@RequestParam Long buyerId) {
        return txService.history(buyerId);//LỊCH SỦ GIAO DỊCH
    }
}
