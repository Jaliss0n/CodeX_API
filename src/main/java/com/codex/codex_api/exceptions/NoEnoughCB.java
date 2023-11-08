package com.codex.codex_api.exceptions;

public class NoEnoughCB  extends RuntimeException {

    public NoEnoughCB() {
        super("Don't have codecoins necessary to purchase this item.");
    }

    public NoEnoughCB(String message) {
        super(message);
    }

    public NoEnoughCB(String message, Throwable cause) {
        super(message, cause);
    }

}