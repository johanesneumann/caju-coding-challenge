package com.caju.card.authorization.domain;

import com.caju.card.authorization.account.domain.Account;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTests {

    @Test
    public void testAccountWillHaveAnId() {
        Account account = new Account(1L, "123456", new BigDecimal(501), new BigDecimal(502), new BigDecimal(503));
        assertNotNull(account.getId());
        assertEquals("123456", account.getAccountNumber());
        assertEquals(new BigDecimal(501), account.getBalanceFood());
        assertEquals(new BigDecimal(502), account.getBalanceMeal());
        assertEquals(new BigDecimal(503), account.getBalanceCash());


    }

    @Test
    public void testAccountNeedsToHaveAccountNumber() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new Account(1L, null, new BigDecimal(500), new BigDecimal(500), new BigDecimal(500));
        });

        assertEquals("Account number cannot be null or empty", thrown.getMessage());
    }

    @Test
    public void testAccountNeedToHavePositiveBalanceFood() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new Account(1L, "123456", new BigDecimal(-1), new BigDecimal(500), new BigDecimal(500));
        });

        assertEquals("Balance food cannot be null or negative", thrown.getMessage());
    }

    @Test
    public void testAccountNeedToHavePositiveBalanceMeal() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new Account(1L, "123456", new BigDecimal(500), new BigDecimal(-1), new BigDecimal(500));
        });

        assertEquals("Balance meal cannot be null or negative", thrown.getMessage());
    }

    @Test
    public void testAccountNeedToHavePositiveBalanceCash() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new Account(1L, "123456", new BigDecimal(500), new BigDecimal(500), new BigDecimal(-1));
        });

        assertEquals("Balance cash cannot be null or negative", thrown.getMessage());
    }


}
