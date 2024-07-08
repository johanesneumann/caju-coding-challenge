package com.caju.card.authorization.transaction.domain;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {

    void save(Transaction transaction);

    Optional<Transaction> findById(UUID transactionId);
}
