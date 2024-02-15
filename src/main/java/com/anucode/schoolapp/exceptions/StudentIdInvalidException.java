package com.anucode.schoolapp.exceptions;

public class StudentIdInvalidException extends RuntimeException{
    public StudentIdInvalidException(String message) {
        super(message);
    }
}
