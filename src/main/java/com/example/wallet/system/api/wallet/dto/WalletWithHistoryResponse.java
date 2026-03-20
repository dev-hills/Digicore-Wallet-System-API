package com.example.wallet.system.api.wallet.dto;

import com.example.wallet.system.api.transaction.dto.TransactionResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Wallet details along with its transaction history")
public class WalletWithHistoryResponse {

    @Schema(description = "Wallet details")
    private WalletResponse wallet;

    @Schema(description = "All transactions for this wallet")
    private List<TransactionResponse> transactions;

}
