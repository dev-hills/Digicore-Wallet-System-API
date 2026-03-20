package com.example.wallet.system.api.wallet.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Wallet entity representing a user's wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique wallet identifier")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "The ID of the user who owns this wallet", example = "1")
    private Long userId;

    @Column(nullable = false, precision = 19, scale = 4)
    @Schema(description = "Current wallet balance", example = "1500.00")
    private BigDecimal balance;

    @Column(nullable = false, updatable = false)
    @Schema(description = "Timestamp when the wallet was created")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Schema(description = "Timestamp of the last wallet update")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
