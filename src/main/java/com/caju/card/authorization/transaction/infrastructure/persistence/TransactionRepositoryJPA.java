package com.caju.card.authorization.transaction.infrastructure.persistence;

import com.caju.card.authorization.transaction.domain.Transaction;
import com.caju.card.authorization.transaction.domain.TransactionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepositoryJPA extends TransactionRepository, JpaRepository<Transaction, Long> {


}
