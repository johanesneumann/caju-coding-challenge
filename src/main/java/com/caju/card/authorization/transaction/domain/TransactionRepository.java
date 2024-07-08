package com.caju.card.authorization.transaction.domain;

import java.util.Optional;

public interface TransactionRepository {

    Transaction save(Transaction transaction);

    Optional<Transaction> findById(Long transactionId);
}
