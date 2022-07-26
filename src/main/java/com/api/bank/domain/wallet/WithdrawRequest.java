package com.api.bank.domain.wallet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawRequest {

    @DecimalMin(value = "0.01", message = "O valor mínimo para a movimentação é R$ 0.01")
    private BigDecimal value;
}
