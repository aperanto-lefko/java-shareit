package ru.practicum.shareit.error_handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.InvalidBookingIdException;
import ru.practicum.shareit.exception.InvalidEmailException;
import ru.practicum.shareit.exception.InvalidItemIdException;
import ru.practicum.shareit.exception.InvalidParameterForBooking;
import ru.practicum.shareit.exception.InvalidRequestIdException;
import ru.practicum.shareit.exception.InvalidUserIdException;

import java.util.stream.Collectors;

@RestControllerAdvice("ru.practicum.shareit")
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) //404
    public ErrorResponse handleAnnotationsField(ConstraintViolationException e) { //исключение при срабатывании аннотации на отдельных полях (переменные пути)
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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    public ErrorResponse handleMissingHeader(MissingRequestHeaderException e) { //исключение для отсутствия заголовка X-Sharer-User-Id
        log.error("Отсутствует заголовок X-Sharer-User-Id");
        return new ErrorResponse("Не указан заголовок X-Sharer-User-Id. " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    public ErrorResponse handleMissingField(JsonMappingException e) { //исключение для отсутствия поля в JSOn
        log.error("В JSON не указано обязательно поле");
        return new ErrorResponse("В запросе отсутствует обязательное поле" + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) //404
    public ErrorResponse invalidUserId(InvalidUserIdException e) { //исключение для некорректного номер id user
        log.error("Неверно указан userId");
        return new ErrorResponse("В запросе неверно указан userId " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500
    public ErrorResponse invalidEmail(InvalidEmailException e) { //исключение для некорректного email
        log.error("Неверно указан email");
        return new ErrorResponse("В запросе неверно указан email. " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) //404
    public ErrorResponse invalidItemId(InvalidItemIdException e) { //исключение для некорректного номер id item
        log.error("Неверно указан itemId");
        return new ErrorResponse("В запросе неверно указан itemId" + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) //404
    public ErrorResponse invalidBookingId(InvalidBookingIdException e) { //исключение для некорректного номер id booking
        log.error("Неверно указан bookingId");
        return new ErrorResponse("В запросе неверно указан bookingId" + e.getMessage());
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) //404
    public ErrorResponse invalidRequestId(InvalidRequestIdException e) { //исключение для некорректного номер id запроса
        log.error("Неверно указан requestId");
        return new ErrorResponse("В запросе неверно указан requestId" + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500
    public ErrorResponse invalidBookingParameter(InvalidParameterForBooking e) { //исключение для некорректного параметра бронирования
        log.error("Неверно указан параметр booking " + e.getMessage());
        return new ErrorResponse("В запросе неверно указан параметр " + e.getMessage());
    }
}
