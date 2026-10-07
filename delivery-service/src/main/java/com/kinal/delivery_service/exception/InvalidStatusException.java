package com.kinal.delivery_service.exception;

public class InvalidStatusException extends RuntimeException {

    public InvalidStatusException(String mensaje) {
        super(mensaje);
    }
}