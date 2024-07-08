package com.caju.card.authorization.account.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Entity()
@Table(name = "accounts")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Account {

    private static final Map<BalanceCategory, DebitStrategy> debitStrategies;

    static {
        debitStrategies = Map.of(
                BalanceCategory.FOOD, new FoodDebitStrategy(),
                BalanceCategory.MEAL, new MealDebitStrategy(),
                BalanceCategory.CASH, new CashDebitStrategy()
        );
    }

    @Id
    @Generated
    private Long id;
    @Column(name = "account_number")
    private String accountNumber;
    @Column(name = "balance_food")
    private BigDecimal balanceFood;
    @Column(name = "balance_meal")
    private BigDecimal balanceMeal;
    @Column(name = "balance_cash")
    private BigDecimal balanceCash;


    public Account(Long id, String accountNumber, BigDecimal balanceFood, BigDecimal balanceMeal, BigDecimal balanceCash) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balanceFood = balanceFood;
        this.balanceMeal = balanceMeal;
        this.balanceCash = balanceCash;


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

    boolean canAcceptDebit(BigDecimal amount, BalanceCategory category) {
        Assert.isTrue(amount.compareTo(BigDecimal.ZERO) > 0, "Amount must be positive");
        return debitStrategies.get(category).canDebit(this, amount);
    }


    public void debit(BigDecimal amount, BalanceCategory category) throws InsufficientFundsException {
        Assert.isTrue(amount.compareTo(BigDecimal.ZERO) > 0, "Amount must be positive");
        if (!canAcceptDebit(amount, category)) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        debitStrategies.get(category).debit(this, amount);
    }


    void setBalanceFood(BigDecimal balanceFood) {
        Assert.isTrue(balanceFood.compareTo(BigDecimal.ZERO) >= 0, "Balance food cannot be negative");
        this.balanceFood = balanceFood;
    }

    void setBalanceMeal(BigDecimal balanceMeal) {
        Assert.isTrue(balanceMeal.compareTo(BigDecimal.ZERO) >= 0, "Balance meal cannot be negative");
        this.balanceMeal = balanceMeal;
    }

    void setBalanceCash(BigDecimal balanceCash) {
        Assert.isTrue(balanceCash.compareTo(BigDecimal.ZERO) >= 0, "Balance cash cannot be negative");
        this.balanceCash = balanceCash;
    }

}
