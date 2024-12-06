package ru.practicum.shareit.errorHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.BadRequestException;

import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice("ru.practicum.shareit")
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500
    public ErrorResponse handleAnnotationsObject(MethodArgumentNotValidException e) {//исключение при срабатывании аннотации на объектах
        String response = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage(); //можно вернуть массивом все ошибки валидации
        log.error("Пользователь указал некорректные данные." + response);
        return new ErrorResponse("Указаны некорректные данные. " + response);
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500
    public ErrorResponse handleAnnotationsField (ConstraintViolationException e) { //исключение при срабатывании аннотации на отдельных полях (переменные пути)
        String response = e.getConstraintViolations() //выводим все сообщения через запятую
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(","));
        log.error("Пользователь указал некорректные данные. " + response);
        return new ErrorResponse("Указаны некорректные данные. " + response);
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500
    public ErrorResponse handleBadRequest(BadRequestException e) {
        return new ErrorResponse(e.getMessage());
    }
}
