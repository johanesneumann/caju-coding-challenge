package com.caju.card.authorization.account.infrastructure.controller;


import com.caju.card.authorization.account.application.CreateAccountPayload;
import com.caju.card.authorization.account.application.CreateAccountUseCase;
import com.caju.card.authorization.account.domain.Account;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Log
public class AccountRestController {

    private final CreateAccountUseCase createAccountUseCase;


    @PostMapping()
    @Validated
    public AccountDTO createAccount(@RequestBody @Valid CreateAccountPayload payload) {
        log.info("Creating account");
        Account account = createAccountUseCase.execute(payload);
        log.info("Account created");
        return AccountDTO.parse(account);
    }


}
