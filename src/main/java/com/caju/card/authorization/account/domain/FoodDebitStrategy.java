package com.caju.card.authorization.account.domain;

import java.math.BigDecimal;

public class FoodDebitStrategy implements DebitStrategy {

    @Override
    public boolean canDebit(Account account, BigDecimal amount) {
        return account.getBalanceFood().add(account.getBalanceCash()).compareTo(amount) >= 0;
    }

    @Override
    public void debit(Account account, BigDecimal amount) {
        if (account.getBalanceFood().compareTo(amount) >= 0) {
            account.setBalanceFood(account.getBalanceFood().subtract(amount));
        } else {
            BigDecimal remainingAmount = amount.subtract(account.getBalanceFood());
            account.setBalanceFood(BigDecimal.ZERO);
            account.setBalanceCash(account.getBalanceCash().subtract(remainingAmount));
        }
    }
}
