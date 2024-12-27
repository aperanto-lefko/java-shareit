package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public BookingDto createBooking(Long userId, BookingDto bookingDto) {
        if (bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new BadRequestException("Дата начала и конца бронирования не может совпадать");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("Пользователь с id " + userId + " не найден"));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new BadRequestException("Вещь с id " + bookingDto.getItemId() + "не найдена"));
        if (!item.getAvailable()) {
            throw new BadRequestException("Вещь не доступна для бронирования");
        }
        Booking booking = BookingMapper.toBooking(bookingDto, user, item);
        BookingDto savedBookingDto = BookingMapper.toBookingDto(bookingRepository.save(booking));
        log.info("Бронирование {booking} сохранено в базу данных");
        return savedBookingDto;
    }

    @Transactional
    public BookingDto createApprove(Long userId, Long bookingId, String approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BadRequestException("Вещь с id " + bookingId + " не найдена"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("Пользователь с id " + userId + " не найден"));
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new BadRequestException("Статус может менять только хозяин вещи. Пользователь с id " +
                    userId + " не является хозяином вещи с id " + booking.getItem().getId());
        }

        return switch (approved) {
            case "true" -> {
                booking.setStatus(Status.APPROVED);
                log.info("Сохранение " + booking + " в базу данных");
                yield BookingMapper.toBookingDto(bookingRepository.save(booking));
            }
            case "false" -> {
                booking.setStatus(Status.REJECTED);
                log.info("Сохранение " + booking + " в базу данных");
                yield BookingMapper.toBookingDto(bookingRepository.save(booking));
            }
            default -> throw new BadRequestException("Некорректный параметр запроса " + approved);
        };
    }

    public List<BookingDto> searchBookingsForOwner(Long ownerId) {
        return toListBookingDto(bookingRepository.findByItemOwnerId(ownerId));
    }

    public BookingDto searchBooking(Long userId, Long bookingId) {
        return BookingMapper.toBookingDto(bookingRepository.findByIdAndBookerIdOrItemOwnerId(bookingId, userId, userId)
                .orElseThrow(() ->
                        new BadRequestException("Бронирование для пользователя с id " + userId + " не найдено")));
    }

    public List<BookingDto> searchBookingsWithState(Long userId, String state) {
        log.info("Поиск бронирований с параметром " + state);
        return switch (state) {
            case "ALL" -> toListBookingDto(bookingRepository.findByBookerIdOrItemOwnerId(userId, userId));
            case "CURRENT" -> toListBookingDto(//текущие
                    bookingRepository.findByBookerIdOrItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                            userId,
                            userId,
                            LocalDateTime.now(),
                            LocalDateTime.now()));
            case "PAST" -> toListBookingDto(//прошедшие
                    bookingRepository.findByBookerIdOrItemOwnerIdAndEndBeforeOrderByStartDesc(
                            userId,
                            userId,
                            LocalDateTime.now()));
            case "FUTURE" -> toListBookingDto(//будущие
                    bookingRepository.findByBookerIdOrItemOwnerIdAndStartAfterOrderByStartDesc(
                            userId,
                            userId,
                            LocalDateTime.now()));
            case "WAITING" -> toListBookingDto(//ожидающие подтверждения
                    bookingRepository.findByBookerIdOrItemOwnerIdAndStatusOrderByStartDesc(
                            userId,
                            userId,
                            Status.WAITING));
            case "REJECTED" -> toListBookingDto(//отклоненные
                    bookingRepository.findByBookerIdOrItemOwnerIdAndStatusOrderByStartDesc(
                            userId,
                            userId,
                            Status.REJECTED));
            default -> throw new BadRequestException("Некорректный параметр запроса " + state);
        };
    }

    private List<BookingDto> toListBookingDto(List<Booking> list) {
        return list.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }
}
