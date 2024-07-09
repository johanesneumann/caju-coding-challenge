package com.caju.card.authorization.transaction.application;

import com.caju.card.authorization.UseCase;
import com.caju.card.authorization.account.domain.Account;
import com.caju.card.authorization.account.domain.AccountRepository;
import com.caju.card.authorization.account.domain.BalanceCategory;
import com.caju.card.authorization.account.domain.InsufficientFundsException;
import com.caju.card.authorization.transaction.domain.Transaction;
import com.caju.card.authorization.transaction.domain.TransactionRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@UseCase
public class AuthorizeTransactionUseCase {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final Map<String, BalanceCategory> mccCategory;
    private final CorrectTransactionMCCUseCase correctTransactionMCCUseCase;

    public AuthorizeTransactionUseCase(AccountRepository accountRepository, TransactionRepository transactionRepository, CorrectTransactionMCCUseCase correctTransactionMCCUseCase) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.correctTransactionMCCUseCase = correctTransactionMCCUseCase;
        this.mccCategory = new HashMap<>();
        this.mccCategory.put("5411", BalanceCategory.FOOD);
        this.mccCategory.put("5412", BalanceCategory.FOOD);
        this.mccCategory.put("5811", BalanceCategory.MEAL);
        this.mccCategory.put("5812", BalanceCategory.MEAL);

    }


    public TransactionResult execute(AuthorizeTransactionPayload payload) {
        Transaction transaction = new Transaction(payload.account(), payload.totalAmount(), payload.merchant(), payload.mcc());
        try {
            String mcc = correctTransactionMCCUseCase.execute(new CorrectTransactionMccPayload(transaction.getMcc(), transaction.getMerchant()));
            transaction.setMcc(mcc);

            Account account = accountRepository.findByAccountNumber(transaction.getAccountId()).orElseThrow(() -> new IllegalArgumentException("Unprocessable transaction, account not found"));
            BalanceCategory balanceCategory = Optional.ofNullable(mccCategory.get(mcc)).orElse(BalanceCategory.CASH);

            account.debit(transaction.getAmount(), balanceCategory);
            transaction.authorize();
            accountRepository.save(account);


        } catch (InsufficientFundsException e) {
            transaction.insuficientFunds();
        } catch (Exception e) {
            transaction.deny();
        } finally {
            transactionRepository.save(transaction);
        }
        return new TransactionResult(transaction.getResultCode());


    }

}
