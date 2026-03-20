package com.example.wallet.system.api.transaction.dto;

import com.example.wallet.system.api.transaction.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response after a fund or debit operation")
public class TransactionResponse {

    @Schema(description = "Transaction ID", example = "1")
    private Long transactionId;

    @Schema(description = "Wallet ID", example = "1")
    private Long walletId;

    @Schema(description = "Transaction type", example = "CREDIT")
    private TransactionType type;

    @Schema(description = "Transaction amount", example = "500.00")
    private BigDecimal amount;

    @Schema(description = "Balance before the transaction", example = "1000.00")
    private BigDecimal balanceBefore;

    @Schema(description = "Balance after the transaction", example = "1500.00")
    private BigDecimal balanceAfter;

    @Schema(description = "When the transaction was recorded")
    private LocalDateTime createdAt;

}
