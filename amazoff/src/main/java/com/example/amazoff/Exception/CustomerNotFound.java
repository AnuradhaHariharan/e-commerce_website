package com.example.amazoff.Exception;

public class CustomerNotFound extends RuntimeException{

    public CustomerNotFound(String message){
        super(message);
    }
}
