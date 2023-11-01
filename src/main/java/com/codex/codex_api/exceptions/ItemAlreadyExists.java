package com.codex.codex_api.exceptions;


public class ItemAlreadyExists extends RuntimeException {

    public ItemAlreadyExists() {
        super("Item already exist, in this database.");
    }

    public ItemAlreadyExists(String message) {
        super(message);
    }

    public ItemAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }

}