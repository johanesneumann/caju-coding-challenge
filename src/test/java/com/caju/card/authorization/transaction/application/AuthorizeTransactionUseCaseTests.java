package com.caju.card.authorization.transaction.application;

import com.caju.card.authorization.account.domain.Account;
import com.caju.card.authorization.account.domain.AccountRepository;
import com.caju.card.authorization.transaction.domain.ResultCode;
import com.caju.card.authorization.transaction.domain.Transaction;
import com.caju.card.authorization.transaction.domain.TransactionRepository;
import com.caju.card.authorization.transaction.domain.TransactionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
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
        AuthorizeTransactionPayload transaction = new AuthorizeTransactionPayload("123", new BigDecimal(100), "SUPERMERCADO ANGELONI    JOINVILLE BR", "5411");
        TransactionResult result = authorizeTransactionUseCase.execute(transaction);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction capturedTransaction = transactionCaptor.getValue();

        assertEquals(ResultCode.APPROVED, result.resultCode());
        assertEquals(new BigDecimal(0), account.getBalanceFood());
        assertEquals(new BigDecimal(200), account.getBalanceMeal());
        assertEquals(new BigDecimal(300), account.getBalanceCash());
        verify(accountRepository).save(account);
        verify(transactionRepository).save(capturedTransaction);

        assertEquals("123", capturedTransaction.getAccountId());
        assertEquals(new BigDecimal(100), capturedTransaction.getAmount());
        assertEquals("SUPERMERCADO ANGELONI    JOINVILLE BR", capturedTransaction.getMerchant());
        assertEquals("5411", capturedTransaction.getMcc());
        assertEquals(TransactionStatus.AUTHORIZED, capturedTransaction.getStatus());
        assertEquals(ResultCode.APPROVED, capturedTransaction.getResultCode());
    }

    @Test
    public void testWillDebtFromFoodBalanceWhenMCC5412() {
        AuthorizeTransactionPayload transaction = new AuthorizeTransactionPayload("123", new BigDecimal(100), "SUPERMERCADO ANGELONI    JOINVILLE BR", "5412");
        TransactionResult result = authorizeTransactionUseCase.execute(transaction);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction capturedTransaction = transactionCaptor.getValue();

        assertEquals(ResultCode.APPROVED, result.resultCode());
        assertEquals(new BigDecimal(0), account.getBalanceFood());
        assertEquals(new BigDecimal(200), account.getBalanceMeal());
        assertEquals(new BigDecimal(300), account.getBalanceCash());
        verify(accountRepository).save(account);
        verify(transactionRepository).save(capturedTransaction);

        assertEquals("123", capturedTransaction.getAccountId());
        assertEquals(new BigDecimal(100), capturedTransaction.getAmount());
        assertEquals("SUPERMERCADO ANGELONI    JOINVILLE BR", capturedTransaction.getMerchant());
        assertEquals("5412", capturedTransaction.getMcc());
        assertEquals(TransactionStatus.AUTHORIZED, capturedTransaction.getStatus());
        assertEquals(ResultCode.APPROVED, capturedTransaction.getResultCode());

    }

    @Test
    public void testTransactionWithInsuficientFoodBalanceWillReject() {
        AuthorizeTransactionPayload transaction = new AuthorizeTransactionPayload("123", new BigDecimal(401), "SUPERMERCADO ANGELONI    JOINVILLE BR", "5411");
        TransactionResult result = authorizeTransactionUseCase.execute(transaction);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction capturedTransaction = transactionCaptor.getValue();

        assertEquals(ResultCode.REJECTED_INSUFFICIENT_FUNDS, result.resultCode());
        assertEquals(new BigDecimal(100), account.getBalanceFood());
        assertEquals(new BigDecimal(200), account.getBalanceMeal());
        assertEquals(new BigDecimal(300), account.getBalanceCash());

        verify(transactionRepository).save(capturedTransaction);

        assertEquals("123", capturedTransaction.getAccountId());
        assertEquals(new BigDecimal(401), capturedTransaction.getAmount());
        assertEquals("SUPERMERCADO ANGELONI    JOINVILLE BR", capturedTransaction.getMerchant());
        assertEquals("5411", capturedTransaction.getMcc());
        assertEquals(TransactionStatus.DENIED, capturedTransaction.getStatus());
        assertEquals(ResultCode.REJECTED_INSUFFICIENT_FUNDS, capturedTransaction.getResultCode());

    }

    @Test
    public void testWillDebtFromMealBalanceWhenMCC5811() {
        AuthorizeTransactionPayload transaction = new AuthorizeTransactionPayload("123", new BigDecimal(100), "SUPERMERCADO ANGELONI    JOINVILLE BR", "5811");
        TransactionResult result = authorizeTransactionUseCase.execute(transaction);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction capturedTransaction = transactionCaptor.getValue();

        assertEquals(ResultCode.APPROVED, result.resultCode());
        assertEquals(new BigDecimal(100), account.getBalanceFood());
        assertEquals(new BigDecimal(100), account.getBalanceMeal());
        assertEquals(new BigDecimal(300), account.getBalanceCash());
        verify(accountRepository).save(account);
        verify(transactionRepository).save(capturedTransaction);

        assertEquals("123", capturedTransaction.getAccountId());
        assertEquals(new BigDecimal(100), capturedTransaction.getAmount());
        assertEquals("SUPERMERCADO ANGELONI    JOINVILLE BR", capturedTransaction.getMerchant());
        assertEquals("5811", capturedTransaction.getMcc());
        assertEquals(TransactionStatus.AUTHORIZED, capturedTransaction.getStatus());
        assertEquals(TransactionStatus.AUTHORIZED, capturedTransaction.getStatus());
        assertEquals(ResultCode.APPROVED, capturedTransaction.getResultCode());

    }

    @Test
    public void testWillDebtFromMealBalanceWhenMCC5812() {
        AuthorizeTransactionPayload transaction = new AuthorizeTransactionPayload("123", new BigDecimal(100), "SUPERMERCADO ANGELONI    JOINVILLE BR", "5812");
        TransactionResult result = authorizeTransactionUseCase.execute(transaction);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction capturedTransaction = transactionCaptor.getValue();

        assertEquals(ResultCode.APPROVED, result.resultCode());
        assertEquals(new BigDecimal(100), account.getBalanceFood());
        assertEquals(new BigDecimal(100), account.getBalanceMeal());
        assertEquals(new BigDecimal(300), account.getBalanceCash());
        verify(accountRepository).save(account);
        verify(transactionRepository).save(capturedTransaction);

        assertEquals("123", capturedTransaction.getAccountId());
        assertEquals(new BigDecimal(100), capturedTransaction.getAmount());
        assertEquals("SUPERMERCADO ANGELONI    JOINVILLE BR", capturedTransaction.getMerchant());
        assertEquals("5812", capturedTransaction.getMcc());
        assertEquals(TransactionStatus.AUTHORIZED, capturedTransaction.getStatus());
        assertEquals(ResultCode.APPROVED, capturedTransaction.getResultCode());

    }

    @Test
    public void testWillDebtFromCashBalanceWhenMCC1234() {
        AuthorizeTransactionPayload transaction = new AuthorizeTransactionPayload("123", new BigDecimal(100), "SUPERMERCADO ANGELONI    JOINVILLE BR", "1234");
        TransactionResult result = authorizeTransactionUseCase.execute(transaction);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction capturedTransaction = transactionCaptor.getValue();

        assertEquals(ResultCode.APPROVED, result.resultCode());
        assertEquals(new BigDecimal(100), account.getBalanceFood());
        assertEquals(new BigDecimal(200), account.getBalanceMeal());
        assertEquals(new BigDecimal(200), account.getBalanceCash());
        verify(accountRepository).save(account);
        verify(transactionRepository).save(capturedTransaction);

        assertEquals("123", capturedTransaction.getAccountId());
        assertEquals(new BigDecimal(100), capturedTransaction.getAmount());
        assertEquals("SUPERMERCADO ANGELONI    JOINVILLE BR", capturedTransaction.getMerchant());
        assertEquals("1234", capturedTransaction.getMcc());
        assertEquals(TransactionStatus.AUTHORIZED, capturedTransaction.getStatus());
        assertEquals(ResultCode.APPROVED, capturedTransaction.getResultCode());

    }

    @Test
    public void testTransactionForNonExistingAccountWillReject() {
        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.empty());
        AuthorizeTransactionPayload transaction = new AuthorizeTransactionPayload("123", new BigDecimal(100), "SUPERMERCADO ANGELONI    JOINVILLE BR", "5411");
        TransactionResult result = authorizeTransactionUseCase.execute(transaction);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction capturedTransaction = transactionCaptor.getValue();

        assertEquals(ResultCode.REJECTED, result.resultCode());
        verify(transactionRepository).save(capturedTransaction);

        assertEquals("123", capturedTransaction.getAccountId());
        assertEquals(new BigDecimal(100), capturedTransaction.getAmount());
        assertEquals("SUPERMERCADO ANGELONI    JOINVILLE BR", capturedTransaction.getMerchant());
        assertEquals("5411", capturedTransaction.getMcc());
        assertEquals(TransactionStatus.DENIED, capturedTransaction.getStatus());
        assertEquals(ResultCode.REJECTED, capturedTransaction.getResultCode());
    }


    @Test
    public void testTransactionWhenExceptionThrownFindingAccountWillReject() {
        when(accountRepository.findByAccountNumber("123")).thenThrow(new IllegalArgumentException("Unprocessable transaction, account not found"));
        AuthorizeTransactionPayload transaction = new AuthorizeTransactionPayload("123", new BigDecimal(100), "SUPERMERCADO ANGELONI    JOINVILLE BR", "5411");
        TransactionResult result = authorizeTransactionUseCase.execute(transaction);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction capturedTransaction = transactionCaptor.getValue();

        assertEquals(ResultCode.REJECTED, result.resultCode());
        verify(transactionRepository).save(capturedTransaction);

        assertEquals("123", capturedTransaction.getAccountId());
        assertEquals(new BigDecimal(100), capturedTransaction.getAmount());
        assertEquals("SUPERMERCADO ANGELONI    JOINVILLE BR", capturedTransaction.getMerchant());
        assertEquals("5411", capturedTransaction.getMcc());
        assertEquals(TransactionStatus.DENIED, capturedTransaction.getStatus());
        assertEquals(ResultCode.REJECTED, capturedTransaction.getResultCode());

    }

    @Test
    public void testTransactionDebitCashIfFoodExceedsBalance() {
        AuthorizeTransactionPayload transaction = new AuthorizeTransactionPayload("123", new BigDecimal(400), "SUPERMERCADO ANGELONI    JOINVILLE BR", "5411");
        TransactionResult result = authorizeTransactionUseCase.execute(transaction);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction capturedTransaction = transactionCaptor.getValue();

        assertEquals(ResultCode.APPROVED, result.resultCode());
        assertEquals(new BigDecimal(0), account.getBalanceFood());
        assertEquals(new BigDecimal(200), account.getBalanceMeal());
        assertEquals(new BigDecimal(0), account.getBalanceCash());
        verify(accountRepository).save(account);
        verify(transactionRepository).save(capturedTransaction);

        assertEquals("123", capturedTransaction.getAccountId());
        assertEquals(new BigDecimal(400), capturedTransaction.getAmount());
        assertEquals("SUPERMERCADO ANGELONI    JOINVILLE BR", capturedTransaction.getMerchant());
        assertEquals("5411", capturedTransaction.getMcc());
        assertEquals(TransactionStatus.AUTHORIZED, capturedTransaction.getStatus());
        assertEquals(ResultCode.APPROVED, capturedTransaction.getResultCode());

    }

    @Test
    public void testTransactionDebitCashIfMealExceedBalance() {
        AuthorizeTransactionPayload transaction = new AuthorizeTransactionPayload("123", new BigDecimal(300), "SUPERMERCADO ANGELONI    JOINVILLE BR", "5811");
        TransactionResult result = authorizeTransactionUseCase.execute(transaction);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction capturedTransaction = transactionCaptor.getValue();

        assertEquals(ResultCode.APPROVED, result.resultCode());
        assertEquals(new BigDecimal(100), account.getBalanceFood());
        assertEquals(new BigDecimal(0), account.getBalanceMeal());
        assertEquals(new BigDecimal(200), account.getBalanceCash());
        verify(accountRepository).save(account);
        verify(transactionRepository).save(capturedTransaction);

        assertEquals("123", capturedTransaction.getAccountId());
        assertEquals(new BigDecimal(300), capturedTransaction.getAmount());
        assertEquals("SUPERMERCADO ANGELONI    JOINVILLE BR", capturedTransaction.getMerchant());
        assertEquals("5811", capturedTransaction.getMcc());
        assertEquals(TransactionStatus.AUTHORIZED, capturedTransaction.getStatus());
        assertEquals(ResultCode.APPROVED, capturedTransaction.getResultCode());

    }


}
