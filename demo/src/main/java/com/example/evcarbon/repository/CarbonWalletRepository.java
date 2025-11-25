package com.example.evcarbon.repository;


import com.example.evcarbon.model.CarbonWallet;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CarbonWalletRepository extends JpaRepository<CarbonWallet, Long> {
}