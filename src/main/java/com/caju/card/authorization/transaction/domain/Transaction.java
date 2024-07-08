package com.caju.card.authorization.transaction.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class Transaction {

    private UUID id;
    private String accountId;
    private BigDecimal amount;
    private String merchant;
    private String mcc;
    private TransactionStatus status;

    Transaction() {

    }

    public Transaction(String accountId, BigDecimal amount, String merchant, String mcc) {
        this.id = UUID.randomUUID();
        this.accountId = accountId;
        this.amount = amount;
        this.merchant = merchant;
        this.mcc = mcc;
        this.status = TransactionStatus.PENDING;

        //accountId cannot be null or empty
        if (accountId == null || accountId.isEmpty()) {
            throw new IllegalArgumentException("Transaction account id cannot be null or empty");
        }
        //amount cannot be null or negative
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Transaction amount cannot be null or negative");
        }
        //merchant cannot be null or empty
        if (merchant == null || merchant.isEmpty()) {
            throw new IllegalArgumentException("Transaction merchant cannot be null or empty");
        }
        //mcc cannot be null or empty and must have 4 numeric digits
        if (mcc == null || mcc.isEmpty() || !mcc.matches("^[0-9]{4}$")) {
            throw new IllegalArgumentException("Transaction mcc must have 4 numeric digits");
        }

    }

    public void authorize() {
        this.status = TransactionStatus.AUTHORIZED;
    }

    public void deny() {
        this.status = TransactionStatus.DENIED;
    }


}
