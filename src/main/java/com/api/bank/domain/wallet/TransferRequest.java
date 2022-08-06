package com.api.bank.domain.wallet;

import lombok.Builder;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
public record TransferRequest(
        @DecimalMin(value = "0.01", message = "O valor mínimo para a movimentação é R$ 0.01")
        BigDecimal value,
        @NotNull Long destinyCustomerId) {
}
