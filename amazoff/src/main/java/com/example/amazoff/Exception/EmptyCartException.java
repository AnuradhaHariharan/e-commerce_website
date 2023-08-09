package com.example.amazoff.Exception;

public class EmptyCartException extends RuntimeException {
    public EmptyCartException(String s) {
        super(s);
    }
}
