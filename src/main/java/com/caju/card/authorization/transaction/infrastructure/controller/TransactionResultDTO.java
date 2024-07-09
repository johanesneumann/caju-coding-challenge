package com.caju.card.authorization.transaction.infrastructure.controller;

import com.caju.card.authorization.transaction.application.TransactionResult;

public record TransactionResultDTO(String code) {

    public static TransactionResultDTO parse(TransactionResult result) {
        return new TransactionResultDTO(result.resultCode().getCode());
    }
}
