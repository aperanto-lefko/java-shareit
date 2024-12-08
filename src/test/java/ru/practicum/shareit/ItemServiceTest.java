package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.UpdateItemRequest;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@ComponentScan(basePackages = "ru.practicum.shareit")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {
    private final ItemService itemService;
    private final UserService userService;
    UserDto u1, createdUserU1;
    ItemDto i1, i2;
    ItemDto createdItemI1, createdItemI2;
    UpdateItemRequest request;

    @BeforeEach
    public void createItemDto() {
        u1 = UserDto.builder()
                .name("name u1")
                .email("u1@mail.ru")
                .build();
        createdUserU1 = userService.createUser(u1);
        i1 = ItemDto.builder()
                .name("name i1")
                .description("description i1")
                .available(true)
                .owner(createdUserU1.getId())
                .build();
        i2 = ItemDto.builder()
                .name("name i2")
                .description("description i2")
                .available(false)
                .owner(createdUserU1.getId())
                .build();
        request = UpdateItemRequest.builder()
                .name("name request")
                .description("description request")
                .available(false)
                .owner(createdUserU1.getId())
                .build();
        createdItemI1 = itemService.createItem(i1);
        createdItemI2 = itemService.createItem(i2);
    }

    @Test
    public void testCreateItemInRepository() {
        assertThat(createdItemI1.getId()).isNotNull();
        assertThat(createdItemI1)
                .hasFieldOrPropertyWithValue("name", "name i1")
                .hasFieldOrPropertyWithValue("description", "description i1")
                .hasFieldOrPropertyWithValue("available", true);
    }

    @Test
    public void testUpdateItem() {
        request.setId(createdItemI1.getId());
        ItemDto updatedItem = itemService.updateItem(request);
        assertThat(updatedItem)
                .hasFieldOrPropertyWithValue("name", "name request")
                .hasFieldOrPropertyWithValue("description", "description request")
                .hasFieldOrPropertyWithValue("available", false);
    }

    @Test
    public void testGetItemsForUser() {
        List<ItemDto> list = itemService.getItemsForUser(createdUserU1.getId());
        assertThat(list)
                .hasSize(2)
                .extracting(ItemDto::getName)
                .contains("name i1", "name i2");
    }

    @Test
    public void testSearchItems() {
        List<ItemDto> list = itemService.searchItems("scrip");
        assertThat(list)
                .filteredOn(ItemDto::getAvailable, true)
                .extracting(ItemDto::getDescription)
                .contains("description i1");
    }

    @Test
    public void testIsItemRegistered() {
        assertTrue(itemService.isItemRegistered(createdItemI1.getId()));
    }
}
