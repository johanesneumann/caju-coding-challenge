package com.caju.card.authorization.account.domain;

import java.util.Optional;

public interface AccountRepository {

    Optional<Account> findByAccountNumber(String accountNumber);

    Account save(Account account);
}
