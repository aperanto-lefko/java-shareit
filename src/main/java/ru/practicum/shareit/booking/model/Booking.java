package ru.practicum.shareit.booking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "bookings", schema = "public")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  //уникальный идентификатор бронирования
    private LocalDateTime start; // дата и время начала бронирования
    private LocalDateTime end; //  дата и время конца бронирования
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private Item item;    //вещь, которую пользователь бронирует;
    @OneToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private User booker; //пользователь, который осуществляет бронирование
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; // статус бронирования

}
