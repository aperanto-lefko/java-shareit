package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;


import java.util.List;

public interface BookingService {
    BookingDto createBooking(Long userId, BookingDto bookingDto);

    BookingDto createApprove(Long userId, Long bookingId, String approved);

    List<BookingDto> searchBookingsForOwner(Long ownerId);

    BookingDto searchBooking(Long userId, Long bookingId);

    List<BookingDto> searchBookingsWithState(Long userId, String state);
}
