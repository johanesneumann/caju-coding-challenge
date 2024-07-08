package com.caju.card.authorization.transaction.application;

public enum ResultCode {

    APPROVED("00"),
    REJECTED_INSUFFICIENT_FUNDS("51"),
    REJECTED("07");


    private final String code;

    ResultCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


}
