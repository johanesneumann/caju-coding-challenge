package com.caju.card.authorization.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class Account {

    private UUID id;
    private String accountNumber;
    private BigDecimal balanceFood;
    private BigDecimal balanceMeal;
    private BigDecimal balanceCash;


    public Account(String accountNumber, BigDecimal balanceFood, BigDecimal balanceMeal, BigDecimal balanceCash) {
        this.id = UUID.randomUUID();
        this.accountNumber = accountNumber;
        this.balanceFood = balanceFood;
        this.balanceMeal = balanceMeal;
        this.balanceCash = balanceCash;

        //account number cannot be null or empty
        if (accountNumber == null || accountNumber.isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be null or empty");
        }

        //balanceFood cannot be null or negative
        if (balanceFood == null || balanceFood.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance food cannot be null or negative");
        }
        //balanceMeal cannot be null or negative
        if (balanceMeal == null || balanceMeal.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance meal cannot be null or negative");
        }
        //balanceCash cannot be null or negative
        if (balanceCash == null || balanceCash.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cash cannot be null or negative");
        }
    }

}
