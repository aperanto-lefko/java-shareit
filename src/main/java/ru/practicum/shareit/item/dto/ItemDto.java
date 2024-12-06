package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Data
@Builder
public class ItemDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) //Свойство может быть только для чтения. Запрет на изменение через JSon
    private Long id;
    private String name;
    private String description;
    private boolean available;
    private User owner;
    private ItemRequest request;
}
