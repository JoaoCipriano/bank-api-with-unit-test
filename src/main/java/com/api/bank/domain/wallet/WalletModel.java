package com.api.bank.domain.wallet;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record WalletModel (BigDecimal balance) {
}
