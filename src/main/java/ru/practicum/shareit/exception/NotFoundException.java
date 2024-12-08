package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(HttpStatus.NOT_FOUND) //404
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
        log.warn(message);
    }
}