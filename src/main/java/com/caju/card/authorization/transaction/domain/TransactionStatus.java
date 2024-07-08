package com.caju.card.authorization.transaction.domain;

public enum TransactionStatus {

    PENDING,
    AUTHORIZED,
    DENIED;

    public boolean isPending() {
        return this == PENDING;
    }

    public boolean isAuthorized() {
        return this == AUTHORIZED;
    }

    public boolean isDenied() {
        return this == DENIED;
    }
}
