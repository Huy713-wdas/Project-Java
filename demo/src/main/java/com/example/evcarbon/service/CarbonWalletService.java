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

// LẤY HOẶC TẠO MỚI VÍ TÍN CHỈ CHO CHỦ SỞ HƯU
public CarbonWallet getOrCreate(Long ownerId) {
Optional<CarbonWallet> opt = walletRepository.findById(ownerId);
return opt.orElseGet(() -> walletRepository.save(CarbonWallet.builder().ownerId(ownerId).balance(0.0).build()));
}

// CỘNG TÍN CHỈ VÀO VÍ CỦA CHỦ SỞ HỮU
public void addCredits(Long ownerId, double credits) {
CarbonWallet w = getOrCreate(ownerId);
w.setBalance(w.getBalance() + credits);
walletRepository.save(w);
}

// TRỪ TÍN CHỈ TỪ VÍ CỦA CHỦ SỞ HỮU
public boolean deductCredits(Long ownerId, double credits) {
CarbonWallet w = getOrCreate(ownerId);
if (w.getBalance() < credits) return false;
w.setBalance(w.getBalance() - credits);
walletRepository.save(w);
return true;
}
}