package com.caju.card.authorization.transaction.infrastructure.controller;

import com.caju.card.authorization.transaction.application.TransactionResult;
import com.caju.card.authorization.transaction.domain.ResultCode;
import com.caju.card.authorization.transaction.domain.Transaction;
import com.caju.card.authorization.transaction.domain.TransactionStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionDTO(UUID id, String accountId, BigDecimal amount, String merchant, String mcc, TransactionStatus status, String resultCode) {


    public static TransactionDTO parse(Transaction transaction) {
        return new TransactionDTO(transaction.getId(), transaction.getAccountId(), transaction.getAmount(), transaction.getMerchant(), transaction.getMcc(), transaction.getStatus(), transaction.getResultCode().getCode());
    }

}
