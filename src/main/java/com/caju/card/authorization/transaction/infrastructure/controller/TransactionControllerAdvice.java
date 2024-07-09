package com.caju.card.authorization.transaction.infrastructure.controller;

import com.caju.card.authorization.transaction.domain.ResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice(assignableTypes = {TransactionRestController.class})
public class TransactionControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<TransactionResultDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(new TransactionResultDTO(ResultCode.REJECTED.getCode()), HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<TransactionResultDTO> handleValidationExceptions(Exception ex) {
        return new ResponseEntity<>(new TransactionResultDTO(ResultCode.REJECTED.getCode()), HttpStatus.OK);
    }
}
