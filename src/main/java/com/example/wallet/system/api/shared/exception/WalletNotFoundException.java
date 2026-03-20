package com.example.wallet.system.api.shared.exception;

public class WalletNotFoundException extends RuntimeException{
    public WalletNotFoundException(Long id) {
        super("Wallet with id " + id + " not found");
    }
}
