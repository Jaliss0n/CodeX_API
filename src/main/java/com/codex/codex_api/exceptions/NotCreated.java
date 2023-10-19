package com.codex.codex_api.exceptions;

public class NotCreated extends RuntimeException {

    public NotCreated() {
        super("Don't have possible to create this register.");
    }

    public NotCreated(String message) {
        super(message);
    }

    public NotCreated(String message, Throwable cause) {
        super(message, cause);
    }

}