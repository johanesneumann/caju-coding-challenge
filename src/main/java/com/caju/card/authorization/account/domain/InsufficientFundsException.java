package com.caju.card.authorization.account.domain;

public class InsufficientFundsException extends RuntimeException {


    public InsufficientFundsException(String message) {
        super(message);
    }


}
