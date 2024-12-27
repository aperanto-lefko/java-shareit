package ru.practicum.shareit.booking.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrItemOwnerId(Long bookerId, Long ownerId);

    Optional<Booking> findByBookerIdAndItemId(Long bookerId, Long itemId);

    List<Booking> findByItemOwnerId(Long ownerId);

    Optional<Booking> findByIdAndBookerIdOrItemOwnerId(Long bookingId, Long bookerId, Long ownerId);

    List<Booking> findByBookerIdOrItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long bookerId,  //текущие бронирования
                                                                                       Long ownerId,
                                                                                       LocalDateTime startDate,
                                                                                       LocalDateTime endDate);

    List<Booking> findByBookerIdOrItemOwnerIdAndEndBeforeOrderByStartDesc(Long bookerId, Long ownerId, LocalDateTime date); //прошедшие бронирования

    List<Booking> findByBookerIdOrItemOwnerIdAndStartAfterOrderByStartDesc(Long bookerId, Long ownerId, LocalDateTime date); //будущие бронирования

    List<Booking> findByBookerIdOrItemOwnerIdAndStatusOrderByStartDesc(Long bookerId, Long ownerId, Status status); //поиск со статусом

    Optional<Booking> findByItemIdAndStartBeforeAndEndAfter(Long itemId, LocalDateTime startDate, LocalDateTime endDate); //поиск текущего бронирования

    Optional<Booking> findFirstByItemIdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime date); //поиск по id вещи и его ближайшее будущее бронирование
}
