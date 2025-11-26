package com.example.evcarbon.service;
//TÍNH VÍ TÍN CHỈ CARBON

import com.example.evcarbon.model.CarbonWallet;
import com.example.evcarbon.repository.CarbonWalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CarbonWalletService {
private final CarbonWalletRepository walletRepository;


public CarbonWallet getOrCreate(Long ownerId) {
Optional<CarbonWallet> opt = walletRepository.findById(ownerId);
return opt.orElseGet(() -> walletRepository.save(CarbonWallet.builder().ownerId(ownerId).balance(0.0).build()));
}


public void addCredits(Long ownerId, double credits) {
CarbonWallet w = getOrCreate(ownerId);
w.setBalance(w.getBalance() + credits);
walletRepository.save(w);
}


public boolean deductCredits(Long ownerId, double credits) {
CarbonWallet w = getOrCreate(ownerId);
if (w.getBalance() < credits) return false;
w.setBalance(w.getBalance() - credits);
walletRepository.save(w);
return true;
}
}