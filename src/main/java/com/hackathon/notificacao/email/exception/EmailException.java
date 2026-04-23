package com.hackathon.notificacao.email.exception;

public class EmailException extends RuntimeException {
    public EmailException(String message, Exception ex) {
        super(message);
    }
}
