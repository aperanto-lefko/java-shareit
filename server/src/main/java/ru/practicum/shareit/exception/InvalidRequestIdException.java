package ru.practicum.shareit.exception;

public class InvalidRequestIdException extends RuntimeException {
    public InvalidRequestIdException(String message) {
        super(message);
    }
}
