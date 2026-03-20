package com.example.wallet.system.api.wallet.service;

import com.example.wallet.system.api.transaction.dto.TransactionResponse;
import com.example.wallet.system.api.wallet.dto.AmountRequest;
import com.example.wallet.system.api.wallet.dto.CreateWalletRequest;
import com.example.wallet.system.api.wallet.dto.WalletResponse;
import com.example.wallet.system.api.wallet.dto.WalletWithHistoryResponse;
import com.example.wallet.system.api.wallet.repository.WalletRepository;

public interface WalletService {
    WalletResponse createWallet(CreateWalletRequest request);
    TransactionResponse fundWallet(Long walletId, AmountRequest request);
    TransactionResponse debitWallet(Long walletId, AmountRequest request);
    WalletWithHistoryResponse getWallet(Long walletId);
}
