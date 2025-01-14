package com.github.felipeaaa1.libraryapi.exception;

public class OperacaoNaoPermitida extends RuntimeException {
    public OperacaoNaoPermitida(String message) {
        super(message);
    }
}
