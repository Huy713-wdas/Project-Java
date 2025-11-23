package com.example.carboncredit.service;

import com.example.carboncredit.entity.User;
import com.example.carboncredit.entity.Transaction;
import com.example.carboncredit.entity.Wallet;
import com.example.carboncredit.repository.UserRepository;
import com.example.carboncredit.repository.TransactionRepository;
import com.example.carboncredit.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    public List<User> listAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUser(UUID id) {
        return userRepository.findById(id);
    }

    public User updateUser(UUID id, String name, Boolean enabled, String role) {
        User u = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (name != null) u.setName(name);
        if (enabled != null) u.setEnabled(enabled);
        if (role != null) u.setRole(role);
        return userRepository.save(u);
    }

    public List<Transaction> listTransactions(String status) {
        if (status == null) return transactionRepository.findAll();
        return transactionRepository.findByStatus(status);
    }

    @Transactional
    public Transaction resolveTransaction(UUID txId, String action, String comment) {
        Transaction tx = transactionRepository.findById(txId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        if ("refund".equalsIgnoreCase(action)) {
            if (tx.getToUserId() != null && tx.getFromUserId() != null) {
                Wallet fromWallet = walletRepository.findByUserId(tx.getFromUserId())
                        .orElseGet(() -> Wallet.builder().userId(tx.getFromUserId()).balanceCents(0L).build());
                fromWallet.setBalanceCents(fromWallet.getBalanceCents() + (tx.getAmountCents() == null ? 0L : tx.getAmountCents()));
                fromWallet.setUpdatedAt(OffsetDateTime.now());
                walletRepository.save(fromWallet);

                walletRepository.findByUserId(tx.getToUserId()).ifPresent(w -> {
                    long newBal = Math.max(0L, w.getBalanceCents() - (tx.getAmountCents() == null ? 0L : tx.getAmountCents()));
                    w.setBalanceCents(newBal);
                    w.setUpdatedAt(OffsetDateTime.now());
                    walletRepository.save(w);
                });
            }
            tx.setStatus("RESOLVED");
            tx.setUpdatedAt(OffsetDateTime.now());
            transactionRepository.save(tx);
        } else if ("complete".equalsIgnoreCase(action)) {
            tx.setStatus("COMPLETED");
            tx.setUpdatedAt(OffsetDateTime.now());
            transactionRepository.save(tx);
        } else {
            throw new RuntimeException("Unsupported action");
        }
        return tx;
    }

    // Simple report: total volume and revenue in cents
    public ReportSummary getReportSummary() {
        long totalTradeCents = transactionRepository.findAll().stream()
                .filter(t -> "SALE".equalsIgnoreCase(t.getTxType()) && "COMPLETED".equalsIgnoreCase(t.getStatus()))
                .mapToLong(t -> t.getAmountCents() == null ? 0L : t.getAmountCents())
                .sum();

        double totalCc = transactionRepository.findAll().stream()
                .filter(t -> "SALE".equalsIgnoreCase(t.getTxType()) && "COMPLETED".equalsIgnoreCase(t.getStatus()))
                .mapToDouble(t -> t.getAmountCcTons() == null ? 0.0 : t.getAmountCcTons().doubleValue())
                .sum();

        return new ReportSummary(totalTradeCents, totalCc);
    }

    @Data
    @AllArgsConstructor
    public static class ReportSummary {
        private long totalRevenueCents;
        private double totalCcTons;
    }
}
