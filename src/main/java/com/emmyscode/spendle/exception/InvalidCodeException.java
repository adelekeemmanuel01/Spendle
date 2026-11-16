package com.emmyscode.spendle.exception;

public class InvalidCodeException extends RuntimeException {
    public InvalidCodeException() { super("Invalid or expired code"); }
}