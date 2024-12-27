package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.UpdateItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    public ItemDto getItem(Long id) {
        return ItemMapper.toItemDto(itemRepository.findById(id).get(),
                lastBookingForItem(id),
                nextBookingForItem(id),
                commentsForItem(id));
    }


    public List<ItemDto> getItemsForUser(Long userId) {
        return toListItemDto(itemRepository.findByOwnerId(userId));
    }

    @Transactional
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("Пользователь с id " + userId + " не найден"));
        Item item = ItemMapper.toItem(itemDto, user);
        return ItemMapper.toItemDto(itemRepository.save(item),
                lastBookingForItem(item.getId()),
                nextBookingForItem(item.getId()),
                commentsForItem(item.getId()));
    }

    @Transactional
    public ItemDto updateItem(UpdateItemRequest request) {
        Item item = itemRepository.findById(request.getId()).get();
        ItemMapper.updateItemFields(item, request);
        itemRepository.save(item);
        return ItemMapper.toItemDto(itemRepository.findById(item.getId()).get(),
                lastBookingForItem(item.getId()),
                nextBookingForItem(item.getId()),
                commentsForItem(item.getId()));
    }

    public List<ItemDto> searchItems(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        List<Item> list = itemRepository.findByNameContaining(text) //фильтрация доступных вещей
                .stream()
                .filter(Item::getAvailable)
                .toList();
        return toListItemDto(list);
    }

    public boolean isItemRegistered(Long id) {
        return itemRepository.findById(id).isPresent();
    }

    private List<ItemDto> toListItemDto(List<Item> list) {
        return list.stream()
                .map(item ->
                        ItemMapper.toItemDto(item,
                                lastBookingForItem(item.getId()),
                                nextBookingForItem(item.getId()),
                                commentsForItem(item.getId()))
                )
                .toList();
    }

    private List<Comment> commentsForItem(Long id) {
        log.info("Поиск комментариев для вещи с id {}", id);
        return commentRepository.findAllByItemId(id);
    }

    private Booking lastBookingForItem(Long id) { //текущее бронирование
        try {
            return bookingRepository.findByItemIdAndStartBeforeAndEndAfter(id, LocalDateTime.now(), LocalDateTime.now())
                    .orElseThrow(() -> new BadRequestException("Текущее бронирование для вещи с id " + id + " не найдено"));
        } catch (BadRequestException e) {
            log.warn("Текущее бронирование для вещи с id " + id + " не найдено");
            return null;
        }
    }

    private Booking nextBookingForItem(Long id) { //следующее бронирование
        try {
            return bookingRepository.findFirstByItemIdAndStartAfterOrderByStartAsc(id, LocalDateTime.now())
                    .orElseThrow(() -> new BadRequestException("Следующее бронирование для вещи с id " + id + " не найдено"));
        } catch (BadRequestException e) {
            log.warn("Следующее бронирование для вещи с id " + id + " не найдено");
            return null;
        }
    }
}
