package com.caju.card.authorization.account.domain;

import java.math.BigDecimal;

public class CashDebitStrategy implements DebitStrategy {

    @Override
    public boolean canDebit(Account account, BigDecimal amount) {
        return account.getBalanceCash().compareTo(amount) >= 0;
    }

    @Override
    public void debit(Account account, BigDecimal amount) {
        if (canDebit(account, amount)) {
            account.setBalanceCash(account.getBalanceCash().subtract(amount));
        } else {
            throw new IllegalArgumentException("Insufficient funds");
        }
    }
}
