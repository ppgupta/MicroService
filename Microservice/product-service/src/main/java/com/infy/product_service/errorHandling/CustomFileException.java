package com.infy.product_service.errorHandling;

public class CustomFileException extends RuntimeException {
    public CustomFileException(String message) {
        super(message);
    }
}
