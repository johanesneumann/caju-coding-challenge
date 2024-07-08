package com.caju.card.authorization.account.infrastructure.persistence;

import com.caju.card.authorization.account.domain.Account;
import com.caju.card.authorization.account.domain.AccountRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepositoryJPA extends AccountRepository, JpaRepository<Account, Long> {


}
