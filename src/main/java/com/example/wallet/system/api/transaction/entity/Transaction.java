package com.example.wallet.system.api.transaction.entity;

import com.example.wallet.system.api.transaction.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "A record of a credit or debit transaction on a wallet")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique transaction identifier", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "ID of the wallet this transaction belongs to", example = "1")
    private Long walletId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Transaction type: CREDIT or DEBIT")
    private TransactionType type;

    @Column(nullable = false, precision = 19, scale = 4)
    @Schema(description = "Transaction amount", example = "500.00")
    private BigDecimal amount;

    @Column(nullable = false, precision = 19, scale = 4)
    @Schema(description = "Wallet balance before this transaction", example = "1000.00")
    private BigDecimal balanceBefore;

    @Column(nullable = false, precision = 19, scale = 4)
    @Schema(description = "Wallet balance after this transaction", example = "1500.00")
    private BigDecimal balanceAfter;

    @Column(nullable = false, updatable = false)
    @Schema(description = "When the transaction occurred")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
