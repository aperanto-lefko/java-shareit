package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.stateStrategy.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.validate.UserIdValid;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Validated
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping //Добавление нового бронирования
    public BookingDto addBooking(@UserIdValid @RequestHeader("X-Sharer-User-Id") Long userId,
                          @Valid @RequestBody BookingDto bookingDto) {

        return bookingService.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}") //Подтверждение или отклонение запроса на бронирование
    public BookingDto addApprove(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long bookingId,
                                 @RequestParam(name = "approved") boolean approved) {
        return bookingService.createApprove(userId, bookingId, approved);
    }

    @GetMapping("/owner") //поиск бронирований для хозяина вещей
    public List<BookingDto> searchBookingsForOwner(@UserIdValid @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return bookingService.searchBookingsForOwner(ownerId);
    }

    @GetMapping("/{bookingId}") //поиск бронирования по id
    public BookingDto searchBookingByIdForOwnerOrForBooker(@UserIdValid @RequestHeader("X-Sharer-User-Id") Long userId,
                                                           @PathVariable Long bookingId) {
        return bookingService.searchBooking(userId, bookingId);
    }

    @GetMapping //GET /bookings?state={state} //поиск бронирования со статусом
    public List<BookingDto> searchBookingForUserWithState(@UserIdValid @RequestHeader("X-Sharer-User-Id") Long userId,
                                                          @RequestParam(name = "state", defaultValue = "ALL") Status state) {
        return bookingService.searchBookingsWithState(userId, state);
    }

}
