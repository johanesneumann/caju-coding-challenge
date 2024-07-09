package com.caju.card.authorization.account.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Entity()
@Table(name = "accounts")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "account_number")
    private String accountNumber;
    @Column(name = "balance_food")
    private BigDecimal balanceFood;
    @Column(name = "balance_meal")
    private BigDecimal balanceMeal;
    @Column(name = "balance_cash")
    private BigDecimal balanceCash;


    public Account(String accountNumber, BigDecimal balanceFood, BigDecimal balanceMeal, BigDecimal balanceCash) {
        this.accountNumber = accountNumber;
        this.balanceFood = balanceFood;
        this.balanceMeal = balanceMeal;
        this.balanceCash = balanceCash;

        Assert.notNull(accountNumber, "Account number cannot be null or empty");
        Assert.notNull(balanceFood, "Balance food cannot be null or negative");
        Assert.isTrue(balanceFood.compareTo(BigDecimal.ZERO) >= 0, "Balance food cannot be null or negative");
        Assert.notNull(balanceMeal, "Balance meal cannot be null or negative");
        Assert.isTrue(balanceMeal.compareTo(BigDecimal.ZERO) >= 0, "Balance meal cannot be null or negative");
        Assert.notNull(balanceCash, "Balance cash cannot be null or negative");
        Assert.isTrue(balanceCash.compareTo(BigDecimal.ZERO) >= 0, "Balance cash cannot be null or negative");

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
