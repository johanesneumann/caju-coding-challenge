package com.caju.card.authorization.transaction.infrastructure.controller;

import com.caju.card.authorization.transaction.application.AuthorizeTransactionPayload;
import com.caju.card.authorization.transaction.application.AuthorizeTransactionUseCase;
import com.caju.card.authorization.transaction.application.TransactionResult;
import com.caju.card.authorization.transaction.domain.Transaction;
import com.caju.card.authorization.transaction.domain.TransactionRepository;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Log
public class TransactionRestController {

    private final AuthorizeTransactionUseCase authorizeTransactionUseCase;

    private final TransactionRepository transactionRepository;

    @PostMapping
    public @ResponseBody TransactionResultDTO processTransaction(@Valid @RequestBody AuthorizeTransactionPayload payload) {
        log.info("Processing transaction");
        TransactionResult result = this.authorizeTransactionUseCase.execute(payload);
        return TransactionResultDTO.parse(result);
    }

    @GetMapping
    public @ResponseBody List<TransactionDTO> listTransactions(){
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream().map(TransactionDTO::parse).toList();
    }
}
