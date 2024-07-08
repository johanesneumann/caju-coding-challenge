package com.caju.card.authorization;

import com.caju.card.authorization.account.domain.AccountRepository;
import com.caju.card.authorization.transaction.domain.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CardTransactionAuthorizationApplicationTests {

    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private TransactionRepository transactionRepository;

    @Test
    void contextLoads() {
    }

}
