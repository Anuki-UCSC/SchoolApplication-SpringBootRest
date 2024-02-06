package com.anucode.schoolapp.exceptions;

public class StudentNameAlreadyExistsException extends RuntimeException {
    public StudentNameAlreadyExistsException(String message) {
        super(message);
    }
}
