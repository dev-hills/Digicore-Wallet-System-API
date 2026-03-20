package com.example.wallet.system.api.wallet.service;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public WalletResponse createWallet(CreateWalletRequest request) {
        Wallet wallet = Wallet.builder()
                .userId(request.getUserId())
                .balance(BigDecimal.ZERO)
                .build();

        return toWalletResponse(walletRepository.save(wallet));
    }

    @Override
    @Transactional
    public TransactionResponse fundWallet(Long walletId, AmountRequest request) {
        Wallet wallet = findWalletOrThrow(walletId);

        BigDecimal balanceBefore = wallet.getBalance();
        BigDecimal balanceAfter = balanceBefore.add(request.getAmount());

        wallet.setBalance(balanceAfter);
        walletRepository.save(wallet);

        Transaction transaction = recordTransaction(wallet.getId(), TransactionType.CREDIT,
                request.getAmount(), balanceBefore, balanceAfter);

        return toTransactionResponse(transaction);
    }

    @Override
    @Transactional
    public TransactionResponse debitWallet(Long walletId, AmountRequest request) {
        Wallet wallet = findWalletOrThrow(walletId);

        BigDecimal balanceBefore = wallet.getBalance();

        if (balanceBefore.compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException(request.getAmount(), balanceBefore);
        }

        BigDecimal balanceAfter = balanceBefore.subtract(request.getAmount());
        wallet.setBalance(balanceAfter);
        walletRepository.save(wallet);

        Transaction transaction = recordTransaction(wallet.getId(), TransactionType.DEBIT,
                request.getAmount(), balanceBefore, balanceAfter);

        return toTransactionResponse(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public WalletWithHistoryResponse getWallet(Long walletId) {
        Wallet wallet = findWalletOrThrow(walletId);

        List<TransactionResponse> transactions = transactionRepository
                .findByWalletIdOrderByCreatedAtDesc(walletId)
                .stream()
                .map(this::toTransactionResponse)
                .toList();

        return WalletWithHistoryResponse.builder()
                .wallet(toWalletResponse(wallet))
                .transactions(transactions)
                .build();
    }


    private Wallet findWalletOrThrow(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));
    }

    private WalletResponse toWalletResponse(Wallet wallet) {
        return WalletResponse.builder()
                .id(wallet.getId())
                .userId(wallet.getUserId())
                .balance(wallet.getBalance())
                .createdAt(wallet.getCreatedAt())
                .updatedAt(wallet.getUpdatedAt())
                .build();
    }

    private Transaction recordTransaction(Long walletId, TransactionType type,BigDecimal amount, BigDecimal balanceBefore, BigDecimal balanceAfter) {
        Transaction transaction = Transaction.builder()
                .walletId(walletId)
                .type(type)
                .amount(amount)
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .build();

        return transactionRepository.save(transaction);
    }

    private TransactionResponse toTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .transactionId(transaction.getId())
                .walletId(transaction.getWalletId())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .balanceBefore(transaction.getBalanceBefore())
                .balanceAfter(transaction.getBalanceAfter())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
