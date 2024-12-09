package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Item { //вещь
    private Long id;  //уникальный идентификатор вещи
    private String name; //краткое название, не должно быть пустым
    private String description; // развёрнутое описание
    private Boolean available; //статус о том, доступна или нет вещь для аренды //должен быть указан
    private Long owner; //владелец вещи
    private Long request; // если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка на соответствующий запрос

}
