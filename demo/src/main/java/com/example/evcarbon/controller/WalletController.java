package com.example.evcarbon.controller;


import com.example.evcarbon.model.CarbonWallet;
import com.example.evcarbon.service.CarbonWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {
private final CarbonWalletService walletService;


@GetMapping("/{ownerId}")
public ResponseEntity<CarbonWallet> get(@PathVariable Long ownerId) {
return ResponseEntity.ok(walletService.getOrCreate(ownerId));
}
}