package com.caju.card.authorization.transaction.application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AuthorizeTransactionPayload(@NotBlank String account, @Positive BigDecimal totalAmount,
                                          @NotBlank String merchant, @Pattern(regexp = "^[0-9]{4}$") String mcc) {
}
