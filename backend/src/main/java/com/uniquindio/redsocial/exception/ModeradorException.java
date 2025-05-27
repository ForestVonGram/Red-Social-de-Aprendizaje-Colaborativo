package com.uniquindio.redsocial.exception;

public class ModeradorException extends RuntimeException {
    public ModeradorException(String message) {
        super(message);
    }

    public ModeradorException(String message, Throwable cause) {
        super(message, cause);
    }
}
