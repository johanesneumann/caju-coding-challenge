package com.caju.card.authorization.account.infrastructure.controller;

import com.caju.card.authorization.account.domain.Account;

import java.math.BigDecimal;


public record AccountDTO(Long id, String accountNumber, BigDecimal balanceFood, BigDecimal balanceMeal,
                         BigDecimal balanceCash) {

    public static AccountDTO parse(Account account) {
        return new AccountDTO(account.getId(), account.getAccountNumber(), account.getBalanceFood(),
                account.getBalanceMeal(), account.getBalanceCash());
    }
}
