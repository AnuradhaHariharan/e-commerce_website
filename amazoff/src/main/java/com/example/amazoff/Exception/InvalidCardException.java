package com.example.amazoff.Exception;

public class InvalidCardException extends RuntimeException {
    public InvalidCardException(String s) {
        super(s);
    }
}
