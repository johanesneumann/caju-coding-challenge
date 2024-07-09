package com.caju.card.authorization.account.application;

import com.caju.card.authorization.UseCase;
import com.caju.card.authorization.account.domain.Account;
import com.caju.card.authorization.account.domain.AccountRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CreateAccountUseCase {

    private final AccountRepository accountRepository;

    public Account execute(CreateAccountPayload payload) {
        Account account = new Account(payload.accountNumber(), payload.balanceFood(), payload.balanceMeal(), payload.balanceCash());
        return accountRepository.save(account);
    }

}
