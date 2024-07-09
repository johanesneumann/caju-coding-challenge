package com.caju.card.authorization.account.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountTests {


    @Test
    public void testAccountNeedsToHaveAccountNumber() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new Account(null, new BigDecimal(500), new BigDecimal(500), new BigDecimal(500));
        });

        assertEquals("Account number cannot be null or empty", thrown.getMessage());
    }

    @Test
    public void testAccountNeedToHavePositiveBalanceFood() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new Account("123456", new BigDecimal(-1), new BigDecimal(500), new BigDecimal(500));
        });

        assertEquals("Balance food cannot be null or negative", thrown.getMessage());
    }

    @Test
    public void testAccountNeedToHavePositiveBalanceMeal() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new Account("123456", new BigDecimal(500), new BigDecimal(-1), new BigDecimal(500));
        });

        assertEquals("Balance meal cannot be null or negative", thrown.getMessage());
    }

    @Test
    public void testAccountNeedToHavePositiveBalanceCash() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new Account("123456", new BigDecimal(500), new BigDecimal(500), new BigDecimal(-1));
        });

        assertEquals("Balance cash cannot be null or negative", thrown.getMessage());
    }


}
