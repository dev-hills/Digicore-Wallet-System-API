package com.example.wallet.system.api.shared.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(BigDecimal requested, BigDecimal available) {
        super("Insufficient funds: requested " + requested + " but available balance is " + available);
    }
}
