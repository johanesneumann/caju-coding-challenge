package com.caju.card.authorization.transaction.infrastructure.controller;

import com.caju.card.authorization.account.domain.Account;
import com.caju.card.authorization.account.domain.AccountRepository;
import com.caju.card.authorization.merchant.domain.MerchantRepository;
import com.caju.card.authorization.transaction.application.AuthorizeTransactionPayload;
import com.caju.card.authorization.transaction.domain.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TransactionRestControllerTests {

    private final Account account = new Account("123", new BigDecimal(100), new BigDecimal(200), new BigDecimal(300));
    @MockBean
    private MerchantRepository merchantRepository;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private TransactionRepository transactionRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {

        when(merchantRepository.findByTransactionName("BORRACHARIA DO ZE               SAO PAULO BR")).thenReturn(Optional.empty());
        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(account));
    }

    @Test
    @DisplayName("Garante que retorna {\"code\":\"00\"} com HttpStatus 200 quando o formato de entrada estiver valido")
    public void testInvalidParameterReturnsOkResponseWith07Code() throws Exception {

        AuthorizeTransactionPayload payload = new AuthorizeTransactionPayload("123", new BigDecimal("100"), "BORRACHARIA DO ZE               SAO PAULO BR", "9999");


        String jsonRequest = objectMapper.writeValueAsString(payload);
        String response = objectMapper.writeValueAsString(new AbstractMap.SimpleEntry<String, String>("code", "00"));

        // Faz a chamada POST e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }

    @Test
    @DisplayName("Garante que retorna {\"code\":\"07\"} com HttpStatus 200 quando o formato de entrada estiver invalido")
    public void testValidaParametroInvalidoRetornaStatusOkComCodigo07() throws Exception {

        AuthorizeTransactionPayload payload = new AuthorizeTransactionPayload("456", new BigDecimal("100"), "BORRACHARIA DO ZE               SAO PAULO BR", "9999");


        String jsonRequest = objectMapper.writeValueAsString(payload);
        String response = objectMapper.writeValueAsString(new AbstractMap.SimpleEntry<String, String>("code", "07"));

        // Faz a chamada POST e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }

    @Test
    @DisplayName("Garante que retorna {\"code\":\"51\"} com HttpStatus 200 quando o saldo for insuficiente")
    public void testInsufficientFundsReturnsOkResponseWith51Code() throws Exception {

        AuthorizeTransactionPayload payload = new AuthorizeTransactionPayload("123", new BigDecimal("1000"), "BORRACHARIA DO ZE               SAO PAULO BR", "9999");

        String jsonRequest = objectMapper.writeValueAsString(payload);
        String response = objectMapper.writeValueAsString(new AbstractMap.SimpleEntry<String, String>("code", "51"));

        // Faz a chamada POST e verifica a resposta
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }


}
