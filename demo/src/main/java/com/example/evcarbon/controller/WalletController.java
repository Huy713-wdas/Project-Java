package com.example.evcarbon.controller;
//KIỂM SOÁT GIAO DỊCH VÍ TÍN CHỈ

import com.example.evcarbon.dto.WalletOperationRequest;
import com.example.evcarbon.model.CarbonWallet;
import com.example.evcarbon.service.CarbonWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final CarbonWalletService walletService;

    @GetMapping("/{ownerId}")
    public ResponseEntity<CarbonWallet> get(@PathVariable Long ownerId) {
        return ResponseEntity.ok(walletService.getOrCreate(ownerId));
    }

    @PostMapping("/deposit")
    public ResponseEntity<CarbonWallet> deposit(@RequestBody WalletOperationRequest request) {
        walletService.addCredits(request.getOwnerId(), request.getAmount());
        return ResponseEntity.ok(walletService.getOrCreate(request.getOwnerId()));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<CarbonWallet> withdraw(@RequestBody WalletOperationRequest request) {
        boolean ok = walletService.deductCredits(request.getOwnerId(), request.getAmount());
        if (!ok) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không đủ credits để rút");
        }
        return ResponseEntity.ok(walletService.getOrCreate(request.getOwnerId()));
    }

    @PostMapping("/cash/deposit")
    public ResponseEntity<CarbonWallet> cashDeposit(@RequestBody WalletOperationRequest request) {
        walletService.addCash(request.getOwnerId(), request.getAmount());
        return ResponseEntity.ok(walletService.getOrCreate(request.getOwnerId()));
    }

    @PostMapping("/cash/withdraw")
    public ResponseEntity<CarbonWallet> cashWithdraw(@RequestBody WalletOperationRequest request) {
        boolean ok = walletService.withdrawCash(request.getOwnerId(), request.getAmount());
        if (!ok) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không đủ tiền mặt để rút");
        }
        return ResponseEntity.ok(walletService.getOrCreate(request.getOwnerId()));
    }
}