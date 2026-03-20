package com.example.wallet.system.api.wallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body to fund or debit a wallet")
public class AmountRequest {
    @NotNull(message = "amount is required")
    @Positive(message = "amount must be greater than 0")
    @Schema(description = "The amount to credit or debit (must be > 0)", example = "500.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal amount;
}
