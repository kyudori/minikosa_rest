package com.kosa.mini.mvc.exception;

public class StoreRegistrationException extends Exception {
    public StoreRegistrationException(String message) {
        super(message);
    }

    public StoreRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
