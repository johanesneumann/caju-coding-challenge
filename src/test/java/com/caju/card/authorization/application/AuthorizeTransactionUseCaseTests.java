package com.caju.card.authorization.application;

import com.caju.card.authorization.account.domain.Account;
import com.caju.card.authorization.account.domain.AccountRepository;
import com.caju.card.authorization.transaction.application.AuthorizeTransactionUseCase;
import com.caju.card.authorization.transaction.application.ResultCode;
import com.caju.card.authorization.transaction.application.TransactionResult;
import com.caju.card.authorization.transaction.domain.Transaction;
import com.caju.card.authorization.transaction.domain.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthorizeTransactionUseCaseTests {

    private final Account account = new Account("123", new BigDecimal(100), new BigDecimal(200), new BigDecimal(300));

    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private TransactionRepository transactionRepository;
    @Autowired
    private AuthorizeTransactionUseCase authorizeTransactionUseCase;

    @BeforeEach
    public void setUp() {
        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(account));
    }


    @Test
    public void testWillDebtFromFoodBalanceWhenMCC5411() {
        Transaction transaction = new Transaction("123", new BigDecimal(100), "SUPERMERCADO ANGELONI    JOINVILLE BR", "5411");
        TransactionResult result = authorizeTransactionUseCase.execute(transaction);

        assertEquals(ResultCode.APPROVED, result.resultCode());
        assertEquals(new BigDecimal(0), account.getBalanceFood());
        assertEquals(new BigDecimal(200), account.getBalanceMeal());
        assertEquals(new BigDecimal(300), account.getBalanceCash());
        verify(accountRepository).save(account);
        verify(transactionRepository).save(transaction);
    }

    @Test
    public void testWillDebtFromFoodBalanceWhenMCC5412() {
        Transaction transaction = new Transaction("123", new BigDecimal(100), "SUPERMERCADO ANGELONI    JOINVILLE BR", "5412");
        TransactionResult result = authorizeTransactionUseCase.execute(transaction);

        assertEquals(ResultCode.APPROVED, result.resultCode());
        assertEquals(new BigDecimal(0), account.getBalanceFood());
        assertEquals(new BigDecimal(200), account.getBalanceMeal());
        assertEquals(new BigDecimal(300), account.getBalanceCash());
        verify(accountRepository).save(account);
        verify(transactionRepository).save(transaction);
    }

    @Test
    public void testTransactionWithInsuficientFoodBalanceWillReject() {
        Transaction transaction = new Transaction("123", new BigDecimal(401), "SUPERMERCADO ANGELONI    JOINVILLE BR", "5411");
        TransactionResult result = authorizeTransactionUseCase.execute(transaction);

        assertEquals(ResultCode.REJECTED_INSUFFICIENT_FUNDS, result.resultCode());
        assertEquals(new BigDecimal(100), account.getBalanceFood());
        assertEquals(new BigDecimal(200), account.getBalanceMeal());
        assertEquals(new BigDecimal(300), account.getBalanceCash());

        verify(transactionRepository).save(transaction);
    }

    @Test
    public void testWillDebtFromMealBalanceWhenMCC5811() {
        Transaction transaction = new Transaction("123", new BigDecimal(100), "SUPERMERCADO ANGELONI    JOINVILLE BR", "5811");
        TransactionResult result = authorizeTransactionUseCase.execute(transaction);

        assertEquals(ResultCode.APPROVED, result.resultCode());
        assertEquals(new BigDecimal(100), account.getBalanceFood());
        assertEquals(new BigDecimal(100), account.getBalanceMeal());
        assertEquals(new BigDecimal(300), account.getBalanceCash());
        verify(accountRepository).save(account);
        verify(transactionRepository).save(transaction);
    }

    @Test
    public void testWillDebtFromMealBalanceWhenMCC5812() {
        Transaction transaction = new Transaction("123", new BigDecimal(100), "SUPERMERCADO ANGELONI    JOINVILLE BR", "5812");
        TransactionResult result = authorizeTransactionUseCase.execute(transaction);

        assertEquals(ResultCode.APPROVED, result.resultCode());
        assertEquals(new BigDecimal(100), account.getBalanceFood());
        assertEquals(new BigDecimal(100), account.getBalanceMeal());
        assertEquals(new BigDecimal(300), account.getBalanceCash());
        verify(accountRepository).save(account);
        verify(transactionRepository).save(transaction);
    }

    @Test
    public void testWillDebtFromCashBalanceWhenMCC1234() {
        Transaction transaction = new Transaction("123", new BigDecimal(100), "SUPERMERCADO ANGELONI    JOINVILLE BR", "1234");
        TransactionResult result = authorizeTransactionUseCase.execute(transaction);

        assertEquals(ResultCode.APPROVED, result.resultCode());
        assertEquals(new BigDecimal(100), account.getBalanceFood());
        assertEquals(new BigDecimal(200), account.getBalanceMeal());
        assertEquals(new BigDecimal(200), account.getBalanceCash());
        verify(accountRepository).save(account);
        verify(transactionRepository).save(transaction);
    }

    @Test
    public void testTransactionForNonExistingAccountWillReject() {
        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.empty());
        Transaction transaction = new Transaction("123", new BigDecimal(100), "SUPERMERCADO ANGELONI    JOINVILLE BR", "5411");
        TransactionResult result = authorizeTransactionUseCase.execute(transaction);

        assertEquals(ResultCode.REJECTED, result.resultCode());
        verify(transactionRepository).save(transaction);
    }


    @Test
    public void testTransactionWhenExceptionThrownFindingAccountWillReject() {
        when(accountRepository.findByAccountNumber("123")).thenThrow(new IllegalArgumentException("Unprocessable transaction, account not found"));
        Transaction transaction = new Transaction("123", new BigDecimal(100), "SUPERMERCADO ANGELONI    JOINVILLE BR", "5411");
        TransactionResult result = authorizeTransactionUseCase.execute(transaction);

        assertEquals(ResultCode.REJECTED, result.resultCode());
        verify(transactionRepository).save(transaction);
    }

    @Test
    public void testTransactionDebitCashIfFoodExceedsBalance() {
        Transaction transaction = new Transaction("123", new BigDecimal(400), "SUPERMERCADO ANGELONI    JOINVILLE BR", "5411");
        TransactionResult result = authorizeTransactionUseCase.execute(transaction);

        assertEquals(ResultCode.APPROVED, result.resultCode());
        assertEquals(new BigDecimal(0), account.getBalanceFood());
        assertEquals(new BigDecimal(200), account.getBalanceMeal());
        assertEquals(new BigDecimal(0), account.getBalanceCash());
        verify(accountRepository).save(account);
        verify(transactionRepository).save(transaction);
    }

    @Test
    public void testTransactionDebitCashIfMealExceedBalance() {
        Transaction transaction = new Transaction("123", new BigDecimal(300), "SUPERMERCADO ANGELONI    JOINVILLE BR", "5811");
        TransactionResult result = authorizeTransactionUseCase.execute(transaction);

        assertEquals(ResultCode.APPROVED, result.resultCode());
        assertEquals(new BigDecimal(100), account.getBalanceFood());
        assertEquals(new BigDecimal(0), account.getBalanceMeal());
        assertEquals(new BigDecimal(200), account.getBalanceCash());
        verify(accountRepository).save(account);
        verify(transactionRepository).save(transaction);
    }


}
