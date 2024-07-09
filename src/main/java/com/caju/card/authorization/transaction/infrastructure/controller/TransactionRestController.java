package com.caju.card.authorization.transaction.infrastructure.controller;

import com.caju.card.authorization.transaction.application.AuthorizeTransactionPayload;
import com.caju.card.authorization.transaction.application.AuthorizeTransactionUseCase;
import com.caju.card.authorization.transaction.application.TransactionResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Log
public class TransactionRestController {

    private final AuthorizeTransactionUseCase authorizeTransactionUseCase;

    @PostMapping
    public @ResponseBody TransactionResultDTO processTransaction(@Valid @RequestBody AuthorizeTransactionPayload payload) {
        log.info("Processing transaction");
        TransactionResult result = this.authorizeTransactionUseCase.execute(payload);
        return TransactionResultDTO.parse(result);
    }
}
