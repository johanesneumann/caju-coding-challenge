package com.caju.card.authorization.account.domain;

import java.math.BigDecimal;

public class MealDebitStrategy implements DebitStrategy {

    @Override
    public boolean canDebit(Account account, BigDecimal amount) {
        return account.getBalanceMeal().add(account.getBalanceCash()).compareTo(amount) >= 0;
    }

    @Override
    public void debit(Account account, BigDecimal amount) {
        if (account.getBalanceMeal().compareTo(amount) >= 0) {
            account.setBalanceMeal(account.getBalanceMeal().subtract(amount));
        } else {
            BigDecimal remainingAmount = amount.subtract(account.getBalanceMeal());
            account.setBalanceMeal(BigDecimal.ZERO);
            account.setBalanceCash(account.getBalanceCash().subtract(remainingAmount));
        }
    }
}
