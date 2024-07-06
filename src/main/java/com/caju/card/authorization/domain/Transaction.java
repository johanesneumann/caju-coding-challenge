package com.caju.card.authorization.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class Transaction {

    private UUID id;
    private UUID accountId;
    private BigDecimal amount;
    private String merchant;
    private String mcc;
    private TransactionStatus status;

    public Transaction(UUID accountId, BigDecimal amount, String merchant, String mcc) {
        this.id = UUID.randomUUID();
        this.accountId = accountId;
        this.amount = amount;
        this.merchant = merchant;
        this.mcc = mcc;
        this.status = TransactionStatus.PENDING;

        //accountId cannot be null
        if (accountId == null) {
            throw new IllegalArgumentException("Account id cannot be null");
        }
        //amount cannot be null or negative
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be null or negative");
        }
        //merchant cannot be null or empty
        if (merchant == null || merchant.isEmpty()) {
            throw new IllegalArgumentException("Merchant cannot be null or empty");
        }
        //mcc cannot be null or empty and must have 4 numeric digits
        if (mcc == null || mcc.isEmpty() || !mcc.matches("^[0-9]{4}$")) {
            throw new IllegalArgumentException("Mcc must have 4 numeric digits");
        }

    }

    public void process() {
        this.status = TransactionStatus.AUTHORIZED;
    }


}
