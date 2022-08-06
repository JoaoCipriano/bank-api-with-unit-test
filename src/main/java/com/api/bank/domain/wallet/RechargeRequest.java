package com.api.bank.domain.wallet;

import lombok.Builder;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Builder
public record RechargeRequest(
        @DecimalMin(value = "0.01", message = "O valor mínimo para a movimentação é R$ 0.01")
        BigDecimal value) {
}
