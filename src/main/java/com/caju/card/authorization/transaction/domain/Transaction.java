package com.caju.card.authorization.transaction.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Entity
@Table(name = "transactions")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Transaction {

    @Id
    private UUID id;
    @Column(name = "account_number")
    private String accountId;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "merchant_name")
    private String merchant;
    @Column(name = "mcc_code")
    private String mcc;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TransactionStatus status;


    public Transaction(String accountId, BigDecimal amount, String merchant, String mcc) {
        this.id = UUID.randomUUID();
        this.accountId = accountId;
        this.amount = amount;
        this.merchant = merchant;
        this.mcc = mcc;
        this.status = TransactionStatus.PENDING;


        if (accountId == null || accountId.isEmpty()) {
            throw new IllegalArgumentException("Transaction account id cannot be null or empty");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Transaction amount cannot be null or negative");
        }

        if (merchant == null || merchant.isEmpty()) {
            throw new IllegalArgumentException("Transaction merchant cannot be null or empty");
        }

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
