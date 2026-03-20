package com.example.wallet.system.api.shared.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard error response body")
public class ErrorResponse {

    @Schema(description = "HTTP status code", example = "404")
    private int status;

    @Schema(description = "Short error label", example = "NOT_FOUND")
    private String error;

    @Schema(description = "Human-readable message", example = "Wallet with id 5 not found")
    private String message;

    @Schema(description = "Request path that triggered the error", example = "/wallets/5")
    private String path;
}
