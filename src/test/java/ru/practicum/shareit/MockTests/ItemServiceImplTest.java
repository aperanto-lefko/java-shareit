package ru.practicum.shareit.MockTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.InvalidItemIdException;
import ru.practicum.shareit.exception.InvalidUserIdException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.UpdateItemRequest;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private UserService userService;
    @Mock
    private CommentService commentService;
    @Mock
    private BookingService bookingService;
    @InjectMocks
    private ItemServiceImpl itemService;
    private User user;

    private ItemDto itemDto,expectedItemDto;
    private Item item, updatedItem;
    private UpdateItemRequest request;
    private Comment commentOne, commentTwo;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("name u1")
                .email("u1@mail.ru")
                .build();
        item = Item.builder()
                .id(1L)
                .name("item1 name")
                .description("item1 description")
                .available(true)
                .owner(user)
                .build();
        itemDto = ItemDto.builder()
                .id(1L)
                .name("item1 name")
                .description("item1 description")
                .available(true)
                .build();
        commentOne = Comment.builder()
                .id(1L)
                .text("CommentOne")
                .build();
        commentTwo = Comment.builder()
                .id(2L)
                .text("CommentTwo")
                .build();
        expectedItemDto = ItemDto.builder()
                .id(1L)
                .name("item1 name")
                .description("item1 description")
                .comments(List.of(commentOne, commentTwo))
                .available(true)
                .build();
        updatedItem = Item.builder()
                .id(1L)
                .name("item1Updated name")
                .description("item1Updated description")
                .available(true)
                .build();
        request = UpdateItemRequest.builder()
                .id(1L)
                .name("item1Updated name")
                .description("item1Updated description")
                .build();

    }

    @Test
    void createItem() {
        when(itemRepository.save(any(Item.class)))
                .thenReturn(item);
        when(userService.getUserById(anyLong()))
                .thenReturn(user);
        when(commentService.commentsForItem(anyLong()))
                .thenReturn(List.of(commentOne, commentTwo));
        when(bookingService.lastBookingForItem(itemDto.getId()))
                .thenReturn(null);
        when(bookingService.nextBookingForItem(itemDto.getId()))
                .thenReturn(null);
        ItemDto actualItemDto = itemService.createItem(1L, itemDto);
        assertEquals(expectedItemDto, actualItemDto);

        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);//тестируем метод, чтобы убедиться,
        //что в базу передается правильный объект
        verify(itemRepository).save(itemCaptor.capture()); //захваченный объект, который был передан в метод
        Item capturedItem = itemCaptor.getValue();
        assertEquals(user, capturedItem.getOwner()); //проверяем, что хозяин вещи сохранен в базу

        verify(userService).getUserById(user.getId());
        verify(itemRepository).save(any(Item.class));
        verify(bookingService).lastBookingForItem(item.getId());
        verify(bookingService).nextBookingForItem(item.getId());
    }

    @Test
    void updatedItem() {
        when(itemRepository.save(any(Item.class)))
                .thenReturn(updatedItem);
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(commentService.commentsForItem(anyLong()))
                .thenReturn(List.of(commentOne, commentTwo));
        when(bookingService.lastBookingForItem(itemDto.getId()))
                .thenReturn(null);
        when(bookingService.nextBookingForItem(itemDto.getId()))
                .thenReturn(null);
        ItemDto actualItemDto = itemService.updateItem(request);

        assertEquals(updatedItem.getName(), actualItemDto.getName());
        assertEquals(updatedItem.getDescription(), actualItemDto.getDescription());
        assertEquals(2, actualItemDto.getComments().size());
    }

    @Test
    void updateItemWhenItemNotFound() {
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        assertThrows(InvalidItemIdException.class, () -> itemService.updateItem(request));
        verify(itemRepository).findById(request.getId());
        verify(itemRepository, never()).save(any(Item.class));
    }
}
