package com.caju.card.authorization.account.application;

import com.caju.card.authorization.account.domain.Account;
import com.caju.card.authorization.account.domain.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
public class CreateAccountUseCaseTests {


    private final AccountRepository accountRepository = new AccountRepository() {

        @Override
        public Optional<Account> findByAccountNumber(String accountNumber) {
            return Optional.empty();
        }

        public Account save(Account account) {

            return account;
        }

    };

    @Test
    public void testWillCreateAccountWithValidParameters() {

        CreateAccountUseCase createAccountUseCase = new CreateAccountUseCase(accountRepository);
        CreateAccountPayload payload = new CreateAccountPayload("123", new BigDecimal("100.0"), new BigDecimal("100.0"), new BigDecimal("100.0"));
        Account account = createAccountUseCase.execute(payload);


        assertNotNull(account);
        assertEquals("123", account.getAccountNumber(), "Account number should be 123");
        assertEquals(new BigDecimal("100.0"), account.getBalanceFood(), "Balance food should be 100.0");
        assertEquals(new BigDecimal("100.0"), account.getBalanceMeal(), "Balance meal should be 100.0");
        assertEquals(new BigDecimal("100.0"), account.getBalanceCash(), "Balance cash should be 100.0");

    }

    @Test
    public void testWillNotCreateAccountWithNegativeFoodBalance() {

        CreateAccountUseCase createAccountUseCase = new CreateAccountUseCase(accountRepository);
        CreateAccountPayload payload = new CreateAccountPayload("123", new BigDecimal("-100.0"), new BigDecimal("100.0"), new BigDecimal("100.0"));
        var e = assertThrows(IllegalArgumentException.class, () -> createAccountUseCase.execute(payload));
        assertEquals("Balance food cannot be null or negative", e.getMessage());

    }

    @Test
    public void testWillNotCreateAccountWithNegativeMealBalance() {

        CreateAccountUseCase createAccountUseCase = new CreateAccountUseCase(accountRepository);
        CreateAccountPayload payload = new CreateAccountPayload("123", new BigDecimal("100.0"), new BigDecimal("-100.0"), new BigDecimal("100.0"));
        var e = assertThrows(IllegalArgumentException.class, () -> createAccountUseCase.execute(payload));
        assertEquals("Balance meal cannot be null or negative", e.getMessage());

    }

    @Test
    public void testWillNotCreateAccountWithNegativeCashBalance() {

        CreateAccountUseCase createAccountUseCase = new CreateAccountUseCase(accountRepository);
        CreateAccountPayload payload = new CreateAccountPayload("123", new BigDecimal("100.0"), new BigDecimal("100.0"), new BigDecimal("-100.0"));
        var e = assertThrows(IllegalArgumentException.class, () -> createAccountUseCase.execute(payload));
        assertEquals("Balance cash cannot be null or negative", e.getMessage());

    }

    @Test
    public void testWillNotCreateAccountWithNullAccountNumber() {

        CreateAccountUseCase createAccountUseCase = new CreateAccountUseCase(accountRepository);
        CreateAccountPayload payload = new CreateAccountPayload(null, new BigDecimal("100.0"), new BigDecimal("100.0"), new BigDecimal("100.0"));
        var e = assertThrows(IllegalArgumentException.class, () -> createAccountUseCase.execute(payload));
        assertEquals("Account number cannot be null or empty", e.getMessage());

    }


}
