package com.caju.card.authorization.account.application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateAccountPayload(@NotBlank String accountNumber,
                                   @Positive BigDecimal balanceFood,
                                   @Positive BigDecimal balanceMeal, @Positive BigDecimal balanceCash) {
}
