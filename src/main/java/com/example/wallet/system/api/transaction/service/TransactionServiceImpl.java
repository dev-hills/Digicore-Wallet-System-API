package com.example.wallet.system.api.transaction.service;

import com.example.wallet.system.api.shared.exception.WalletNotFoundException;
import com.example.wallet.system.api.transaction.dto.TransactionResponse;
import com.example.wallet.system.api.transaction.entity.Transaction;
import com.example.wallet.system.api.transaction.enums.TransactionType;
import com.example.wallet.system.api.transaction.mapper.TransactionMapper;
import com.example.wallet.system.api.transaction.repository.TransactionRepository;
import com.example.wallet.system.api.wallet.entity.Wallet;
import com.example.wallet.system.api.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public List<TransactionResponse> getTransactions(Long walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));

        return transactionRepository
                .findByWalletIdOrderByCreatedAtDesc(walletId)
                .stream()
                .map(this.transactionMapper::toTransactionResponse)
                .toList();
    }

    @Override
    public Transaction recordTransaction(Long walletId, TransactionType type, BigDecimal amount, BigDecimal balanceBefore, BigDecimal balanceAfter) {
        Transaction transaction = Transaction.builder()
                .walletId(walletId)
                .type(type)
                .amount(amount)
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .build();

        return transactionRepository.save(transaction);
    }
}
