package com.kosa.mini.api.exception;

public class SignupException extends Exception {

    public SignupException(String message){
        super(message);
    }
    public SignupException(String message, Throwable cause) {
        super(message, cause);
    }
}
