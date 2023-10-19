package com.codex.codex_api.exceptions;

public class NotFound extends RuntimeException {

    public NotFound() {
        super("Register not found, in this database.");
    }

    public NotFound(String message) {
        super(message);
    }

    public NotFound(String message, Throwable cause) {
        super(message, cause);
    }

}