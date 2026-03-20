package com.example.wallet.system.api.wallet.dto;

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
@Schema(description = "Wallet details response")
public class WalletResponse {

    @Schema(description = "Wallet ID", example = "1")
    private Long id;

    @Schema(description = "Owner user ID", example = "user-123")
    private Long userId;

    @Schema(description = "Current balance", example = "1500.00")
    private BigDecimal balance;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last updated timestamp")
    private LocalDateTime updatedAt;
}
