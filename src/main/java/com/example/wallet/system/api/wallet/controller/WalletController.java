package com.example.wallet.system.api.wallet.controller;

import com.example.wallet.system.api.shared.ApiResponse;
import com.example.wallet.system.api.shared.error.ErrorResponse;
import com.example.wallet.system.api.transaction.dto.TransactionResponse;
import com.example.wallet.system.api.wallet.dto.AmountRequest;
import com.example.wallet.system.api.wallet.dto.CreateWalletRequest;
import com.example.wallet.system.api.wallet.dto.WalletResponse;
import com.example.wallet.system.api.wallet.dto.WalletWithHistoryResponse;
import com.example.wallet.system.api.wallet.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
@Tag(name = "Wallets", description = "Operations for managing wallets, funding, and debiting")
public class WalletController {
    private final WalletService walletService;


    @Operation(summary = "Create a wallet",description = "Creates a new wallet for a user with a zero starting balance")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Wallet created successfully",
                    content = @Content(schema = @Schema(implementation = WalletResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation error – userId is blank",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<WalletResponse>> createWallet(@Valid @RequestBody CreateWalletRequest request) {
        WalletResponse response = walletService.createWallet(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));

    }

    @Operation(summary = "Fund a wallet",description = "Credits a positive amount to the specified wallet")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Wallet funded successfully",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Amount is invalid (null or ≤ 0)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Wallet not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{id}/fund")
    public ResponseEntity<ApiResponse<TransactionResponse>> fundWallet(@Parameter(description = "Wallet ID", example = "1") @PathVariable Long id, @Valid @RequestBody AmountRequest request){
        return ResponseEntity.ok(ApiResponse.success(walletService.fundWallet(id, request)));
    }

    @Operation(summary = "Debit a wallet",description = "Deducts a positive amount from the wallet. Fails if balance is insufficient")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Debit successful",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Amount is invalid or insufficient funds",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Wallet not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{id}/debit")
    public ResponseEntity<ApiResponse<TransactionResponse>> debitWallet(@Parameter(description = "Wallet ID", example = "1") @PathVariable Long id,@Valid @RequestBody AmountRequest request) {
        return ResponseEntity.ok(ApiResponse.success(walletService.debitWallet(id, request)));
    }

    @Operation(summary = "Get wallet details",description = "Retrieves wallet information along with its full transaction history")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Wallet found",
                    content = @Content(schema = @Schema(implementation = WalletWithHistoryResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Wallet not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WalletWithHistoryResponse>> getWallet(
            @Parameter(description = "Wallet ID", example = "1") @PathVariable Long id) {

        return ResponseEntity.ok(ApiResponse.success(walletService.getWallet(id)));
    }
}

