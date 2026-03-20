package com.example.wallet.system.api.transaction.service;

import com.example.wallet.system.api.transaction.dto.TransactionResponse;
import com.example.wallet.system.api.transaction.entity.Transaction;
import com.example.wallet.system.api.transaction.enums.TransactionType;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    List<TransactionResponse> getTransactions(Long walletId);
    Transaction recordTransaction(Long walletId, TransactionType type, BigDecimal amount, BigDecimal balanceBefore, BigDecimal balanceAfter);
}
