package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.UpdateItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemDto getItem(Long id) {
        return ItemMapper.toItemDto(itemRepository.findById(id).get());
    }

    public List<ItemDto> getItemsForUser(Long userId) {
        return toListItemDto(itemRepository.findByOwnerId(userId));
    }

    public ItemDto createItem(Long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь с id {userId} не найден"));
        Item item = ItemMapper.toItem(itemDto, user);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    public ItemDto updateItem(UpdateItemRequest request) {
        Item item = itemRepository.findById(request.getId()).get();
        ItemMapper.updateItemFields(item, request);
        itemRepository.save(item);
        return ItemMapper.toItemDto(itemRepository.findById(item.getId()).get());
    }

    public List<ItemDto> searchItems(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return toListItemDto(itemRepository.findByNameContaining(text));
    }

    public boolean isItemRegistered(Long id) {
        return itemRepository.findById(id).isPresent();
    }

    private List<ItemDto> toListItemDto(List<Item> list) {
        return list.stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }


}
