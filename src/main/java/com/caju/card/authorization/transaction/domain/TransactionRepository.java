package com.caju.card.authorization.transaction.domain;

import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    Transaction save(Transaction transaction);

    Optional<Transaction> findById(Long transactionId);

    List<Transaction> findAll();
}
