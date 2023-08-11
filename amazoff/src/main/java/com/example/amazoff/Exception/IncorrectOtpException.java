package com.example.amazoff.Exception;

public class IncorrectOtpException extends RuntimeException {
    public IncorrectOtpException(String incorrect_otp) {
        super(incorrect_otp);
    }
}
