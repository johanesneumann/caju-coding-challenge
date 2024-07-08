package com.caju.card.authorization.domain;

import com.caju.card.authorization.transaction.domain.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTests {


    @Test
    public void testTransactionWithNullPropertiesCannotBeCreated() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(null, null, null, null);
        });
    }

    @Test
    public void testTransactionNeedsToHaveAnAccountId() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(null, new BigDecimal(1), "PADARIA DO ZE               SAO PAULO BR", "5811");
        });
        assertEquals("Transaction account id cannot be null or empty", thrown.getMessage());
    }

    @Test
    public void testTransactionNeedsToHaveAMerchant() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction("123", new BigDecimal(1), null, "5811");
        });

        assertEquals("Transaction merchant cannot be null or empty", thrown.getMessage());
    }

    @Test
    public void testTransactionNeedsToHaveAValidMcc() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction("123", new BigDecimal(1), "PADARIA DO ZE               SAO PAULO BR", "123");
        });

        assertEquals("Transaction mcc must have 4 numeric digits", thrown.getMessage());
    }

    @Test
    public void testTransactionNeedsToHaveAnAmount() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction("123", null, "PADARIA DO ZE               SAO PAULO BR", "5811");
        });

        assertEquals("Transaction amount cannot be null or negative", thrown.getMessage());
    }

    @Test
    public void testTransactionNeedsToHavePositiveAmount() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction("123", new BigDecimal(-1), "PADARIA DO ZE               SAO PAULO BR", "5811");
        });


        assertEquals("Transaction amount cannot be null or negative", thrown.getMessage());
    }

    @Test
    public void testTransactionIsCreatedPending() {
        Transaction transaction = new Transaction("123", new BigDecimal(1), "PADARIA DO ZE               SAO PAULO BR", "5811");
        assertTrue(transaction.getStatus().isPending());
        assertEquals("123", transaction.getAccountId());
        assertEquals(new BigDecimal(1), transaction.getAmount());
        assertEquals("PADARIA DO ZE               SAO PAULO BR", transaction.getMerchant());
        assertEquals("5811", transaction.getMcc());

    }

    @Test
    public void testAuthorizeTransaction() {
        Transaction transaction = new Transaction("123", new BigDecimal(1), "PADARIA DO ZE               SAO PAULO BR", "5811");
        transaction.authorize();
        assertTrue(transaction.getStatus().isAuthorized());
    }

    @Test
    public void testDenyTransaction() {
        Transaction transaction = new Transaction("123", new BigDecimal(1), "PADARIA DO ZE               SAO PAULO BR", "5811");
        transaction.deny();
        assertTrue(transaction.getStatus().isDenied());
    }

}
