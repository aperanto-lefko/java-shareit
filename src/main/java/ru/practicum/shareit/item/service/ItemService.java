package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.UpdateItemRequest;

import java.util.List;

public interface ItemService {
    ItemDto getItem(Long id);

    List<ItemDto> getItemsForUser(Long userId);

    ItemDto createItem(ItemDto itemdto);

    ItemDto updateItem(UpdateItemRequest request);

    List<ItemDto> searchItems(String text);

    boolean isItemRegistered(Long id);
}
