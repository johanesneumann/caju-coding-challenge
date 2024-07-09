package com.caju.card.authorization.transaction.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Entity
@Table(name = "transactions")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Transaction {

    @Id
    @Column(name = "id")
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
    @Column(name = "result_code")
    @Enumerated(EnumType.STRING)
    private ResultCode resultCode;


    public Transaction(String accountId, BigDecimal amount, String merchant, String mcc) {
        this.id = UUID.randomUUID();
        this.accountId = accountId;
        this.amount = amount;
        this.merchant = merchant;
        this.mcc = mcc;
        this.status = TransactionStatus.PENDING;

        Assert.notNull(accountId, "Transaction account id cannot be null or empty");
        Assert.notNull(merchant, "Transaction merchant cannot be null or empty");
        Assert.isTrue(mcc.matches("^[0-9]{4}$"), "Transaction mcc must have 4 numeric digits");
        Assert.notNull(amount, "Transaction amount cannot be null or negative");
        Assert.isTrue(amount.compareTo(BigDecimal.ZERO) > 0, "Transaction amount cannot be null or negative");
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public void authorize() {
        this.status = TransactionStatus.AUTHORIZED;
        this.resultCode = ResultCode.APPROVED;
    }

    public void deny() {
        this.status = TransactionStatus.DENIED;
        this.resultCode = ResultCode.REJECTED;
    }

    public void insuficientFunds() {
        this.status = TransactionStatus.DENIED;
        this.resultCode = ResultCode.REJECTED_INSUFFICIENT_FUNDS;
    }


}
