package ru.practicum.shareit.request.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")

@Builder
@Entity
@Table(name = "requests", schema = "public")
@AllArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  //уникальный идентификатор запроса
    @Column(nullable = false)
    private String description; // текст запроса, содержащий описание требуемой вещи
    @ManyToOne
    @JoinColumn(name = "requestor", referencedColumnName = "id", nullable = false)
    private User requestor; //пользователь, создавший запрос
    private LocalDateTime created; // дата и время создания запроса
    public ItemRequest(){}
}
