package com.codex.codex_api.exceptions;

public class ErrorApi  extends RuntimeException {

    public ErrorApi() {
        super("Error conecting a api.");
    }

    public ErrorApi(String message) {
        super(message);
    }

    public ErrorApi(String message, Throwable cause) {
        super(message, cause);
    }

}