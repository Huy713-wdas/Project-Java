package com.example.carboncredit.service;

import com.example.carboncredit.entity.TransactionEntity;
import com.example.carboncredit.entity.Wallet;
import com.example.carboncredit.repo.TransactionRepository;
import com.example.carboncredit.repo.UserRepository;
import com.example.carboncredit.repo.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void resolveTransaction_complete_updatesStatus() {
        UUID txId = UUID.randomUUID();
        TransactionEntity tx = TransactionEntity.builder()
                .id(txId)
                .txType("SALE")
                .status("PENDING")
                .amountCents(1000L)
                .fromUserId(UUID.randomUUID())
                .toUserId(UUID.randomUUID())
                .createdAt(OffsetDateTime.now())
                .build();

        when(transactionRepository.findById(txId)).thenReturn(Optional.of(tx));
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        TransactionEntity result = adminService.resolveTransaction(txId, "complete", "ok");
        assertEquals("COMPLETED", result.getStatus());
        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    void resolveTransaction_refund_movesBalances() {
        UUID txId = UUID.randomUUID();
        UUID from = UUID.randomUUID();
        UUID to = UUID.randomUUID();
        TransactionEntity tx = TransactionEntity.builder()
                .id(txId)
                .txType("SALE")
                .status("PENDING")
                .amountCents(5000L)
                .fromUserId(from)
                .toUserId(to)
                .createdAt(OffsetDateTime.now())
                .build();

        Wallet fromWallet = Wallet.builder().userId(from).balanceCents(0L).build();
        Wallet toWallet = Wallet.builder().userId(to).balanceCents(5000L).build();

        when(transactionRepository.findById(txId)).thenReturn(Optional.of(tx));
        when(walletRepository.findByUserId(from)).thenReturn(Optional.of(fromWallet));
        when(walletRepository.findByUserId(to)).thenReturn(Optional.of(toWallet));
        when(walletRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(transactionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        TransactionEntity res = adminService.resolveTransaction(txId, "refund", "refund reason");

        assertEquals("RESOLVED", res.getStatus());
        // from wallet credited
        assertEquals(5000L, fromWallet.getBalanceCents());
        // to wallet debited
        assertEquals(0L, toWallet.getBalanceCents());
        verify(walletRepository, times(2)).save(any());
        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    void resolveTransaction_unknownTx_throws() {
        UUID txId = UUID.randomUUID();
        when(transactionRepository.findById(txId)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> adminService.resolveTransaction(txId, "complete", "x"));
    }
}
