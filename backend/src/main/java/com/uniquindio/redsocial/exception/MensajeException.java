package com.uniquindio.redsocial.exception;

public class MensajeException extends RuntimeException {
    public MensajeException(String message) {
        super(message);
    }

    public MensajeException(String message, Throwable cause) {
        super(message, cause);
    }
}
