package com.kosa.mini.mvc.exception;

public class DuplicateEmailException extends Exception {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
