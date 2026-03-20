package com.example.wallet.system.api;

import com.example.wallet.system.api.shared.exception.InsufficientFundsException;
import com.example.wallet.system.api.shared.exception.WalletNotFoundException;
import com.example.wallet.system.api.transaction.dto.TransactionResponse;
import com.example.wallet.system.api.transaction.entity.Transaction;
import com.example.wallet.system.api.transaction.enums.TransactionType;
import com.example.wallet.system.api.transaction.repository.TransactionRepository;
import com.example.wallet.system.api.wallet.dto.AmountRequest;
import com.example.wallet.system.api.wallet.dto.CreateWalletRequest;
import com.example.wallet.system.api.wallet.dto.WalletResponse;
import com.example.wallet.system.api.wallet.dto.WalletWithHistoryResponse;
import com.example.wallet.system.api.wallet.entity.Wallet;
import com.example.wallet.system.api.wallet.repository.WalletRepository;
import com.example.wallet.system.api.wallet.service.WalletServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private WalletServiceImpl walletServiceImpl;

    private Wallet mockWallet;

    @BeforeEach
    void setUp() {
        mockWallet = Wallet.builder()
                .id(1L)
                .userId(1L)
                .balance(new BigDecimal("1000.00"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("createWallet: should create wallet with zero balance")
    void createWallet_shouldReturnWalletWithZeroBalance() {
        Wallet saved = Wallet.builder()
                .id(1L)
                .userId(1L)
                .balance(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(walletRepository.save(any(Wallet.class))).thenReturn(saved);

        WalletResponse result = walletServiceImpl.createWallet(new CreateWalletRequest(1L));

        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("fundWallet: should increase balance by the funded amount")
    void fundWallet_shouldIncreaseBalance() {
        when(walletRepository.findById(1L)).thenReturn(Optional.of(mockWallet));
        when(walletRepository.save(any())).thenReturn(mockWallet);

        Transaction tx = Transaction.builder()
                .id(1L).walletId(1L).type(TransactionType.CREDIT)
                .amount(new BigDecimal("500.00"))
                .balanceBefore(new BigDecimal("1000.00"))
                .balanceAfter(new BigDecimal("1500.00"))
                .createdAt(LocalDateTime.now())
                .build();
        when(transactionRepository.save(any())).thenReturn(tx);

        TransactionResponse result = walletServiceImpl.fundWallet(1L, new AmountRequest(new BigDecimal("500.00")));

        assertThat(result.getBalanceAfter()).isEqualByComparingTo("1500.00");
        assertThat(result.getType()).isEqualTo(TransactionType.CREDIT);
    }

    @Test
    @DisplayName("fundWallet: should throw WalletNotFoundException for unknown ID")
    void fundWallet_walletNotFound_shouldThrow() {
        when(walletRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> walletServiceImpl.fundWallet(99L, new AmountRequest(new BigDecimal("100"))))
                .isInstanceOf(WalletNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("debitWallet: should decrease balance by the debited amount")
    void debitWallet_shouldDecreaseBalance() {
        when(walletRepository.findById(1L)).thenReturn(Optional.of(mockWallet));
        when(walletRepository.save(any())).thenReturn(mockWallet);

        Transaction tx = Transaction.builder()
                .id(1L).walletId(1L).type(TransactionType.DEBIT)
                .amount(new BigDecimal("200.00"))
                .balanceBefore(new BigDecimal("1000.00"))
                .balanceAfter(new BigDecimal("800.00"))
                .createdAt(LocalDateTime.now())
                .build();
        when(transactionRepository.save(any())).thenReturn(tx);

        TransactionResponse result = walletServiceImpl.debitWallet(1L, new AmountRequest(new BigDecimal("200.00")));

        assertThat(result.getBalanceAfter()).isEqualByComparingTo("800.00");
        assertThat(result.getType()).isEqualTo(TransactionType.DEBIT);
    }

    @Test
    @DisplayName("debitWallet: should throw InsufficientFundsException when amount exceeds balance")
    void debitWallet_insufficientFunds_shouldThrow() {
        when(walletRepository.findById(1L)).thenReturn(Optional.of(mockWallet));

        assertThatThrownBy(() -> walletServiceImpl.debitWallet(1L, new AmountRequest(new BigDecimal("9999.00"))))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessageContaining("Insufficient funds");
    }

    @Test
    @DisplayName("debitWallet: should allow debiting the exact full balance")
    void debitWallet_exactBalance_shouldSucceed() {
        when(walletRepository.findById(1L)).thenReturn(Optional.of(mockWallet));
        when(walletRepository.save(any())).thenReturn(mockWallet);

        Transaction tx = Transaction.builder()
                .id(1L).walletId(1L).type(TransactionType.DEBIT)
                .amount(new BigDecimal("1000.00"))
                .balanceBefore(new BigDecimal("1000.00"))
                .balanceAfter(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .build();
        when(transactionRepository.save(any())).thenReturn(tx);

        TransactionResponse result = walletServiceImpl.debitWallet(1L, new AmountRequest(new BigDecimal("1000.00")));

        assertThat(result.getBalanceAfter()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("getWallet: should return wallet with transaction history")
    void getWallet_shouldReturnWalletWithHistory() {
        when(walletRepository.findById(1L)).thenReturn(Optional.of(mockWallet));
        when(transactionRepository.findByWalletIdOrderByCreatedAtDesc(1L)).thenReturn(List.of());

        WalletWithHistoryResponse result = walletServiceImpl.getWallet(1L);

        assertThat(result.getWallet().getId()).isEqualTo(1L);
        assertThat(result.getTransactions()).isEmpty();
    }

    @Test
    @DisplayName("getWallet: should throw WalletNotFoundException for unknown ID")
    void getWallet_notFound_shouldThrow() {
        when(walletRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> walletServiceImpl.getWallet(99L))
                .isInstanceOf(WalletNotFoundException.class);
    }
}
