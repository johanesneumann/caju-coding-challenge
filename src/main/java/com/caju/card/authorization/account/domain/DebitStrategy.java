package com.caju.card.authorization.account.domain;

import java.math.BigDecimal;

public interface DebitStrategy {

    boolean canDebit(Account account, BigDecimal amount);

    void debit(Account account, BigDecimal amount);

}
