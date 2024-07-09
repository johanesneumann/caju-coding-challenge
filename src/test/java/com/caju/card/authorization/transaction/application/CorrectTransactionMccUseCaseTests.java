package com.caju.card.authorization.transaction.application;

import com.caju.card.authorization.merchant.domain.Merchant;
import com.caju.card.authorization.merchant.domain.MerchantRepository;
import com.caju.card.authorization.transaction.domain.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class CorrectTransactionMccUseCaseTests {

    @MockBean
    private MerchantRepository merchantRepository;

    @MockBean
    private AuthorizeTransactionUseCase authorizeTransactionUseCase;

    @MockBean
    private TransactionRepository transactionRepository;

    private CorrectTransactionMCCUseCase useCase;

    @BeforeEach
    public void setUp() {
        Merchant borracharia = new Merchant(1L, "BORRACHARIA DO ZE", "SAO PAULO BR", "BORRACHARIA DO ZE               SAO PAULO BR", "9999");
        Merchant uber = new Merchant(2L, "UBER", "SAO PAULO BR", "UBER TRIP SAO PAULO BR", "9999");
        Merchant uberEats = new Merchant(3L, "UBER EATS", "SAO PAULO BR", "UBER EATS SAO PAULO BR", "5812");
        Merchant picpay = new Merchant(4L, "PICPAY", "GOIANIA BR", "PICPAY*BILHETEUNICO GOIANIA BR", "9999");

        when(merchantRepository.findByTransactionName("UBERTRIPSAOPAULOBR")).thenReturn(Optional.of(uber));
        when(merchantRepository.findByTransactionName("UBEREATSSAOPAULOBR")).thenReturn(Optional.of(uberEats));
        when(merchantRepository.findByTransactionName("PICPAY*BILHETEUNICOGOIANIABR")).thenReturn(Optional.of(picpay));
        when(merchantRepository.findByTransactionName("BORRACHARIADOZESAOPAULOBR")).thenReturn(Optional.of(borracharia));
        useCase = new CorrectTransactionMCCUseCase(merchantRepository);
    }

    @Test
    public void testWillUseMerchantMccIfFound() {

        assertEquals("9999", this.useCase.execute(new CorrectTransactionMccPayload("5811", "UBER TRIP SAO PAULO BR")));
        assertEquals("5812", this.useCase.execute(new CorrectTransactionMccPayload("5411", "UBER EATS SAO PAULO BR")));
        assertEquals("9999", this.useCase.execute(new CorrectTransactionMccPayload("5811", "PICPAY*BILHETEUNICO GOIANIA BR")));
        assertEquals("9999", this.useCase.execute(new CorrectTransactionMccPayload("5811", "BORRACHARIA DO ZE               SAO PAULO BR")));

    }
}
