package com.kinal.delivery_service.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String mensaje) {
        super(mensaje);
    }
}