package ru.practicum.shareit.MockTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
/*
используется для юнит-тестирования и не создает контекст Spring. Он предназначен
для изолированного тестирования классов и методов.
 */
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    /*
    аннотация используется для создания экземпляра тестируемого класса (User Service) и
    автоматического внедрения в него мок-объектов, которые
    были созданы с помощью аннотации @Mock. Mockito ищет поля, помеченные аннотацией
     @Mock, и автоматически инжектирует их в тестируемый объект.
     */
    private UserServiceImpl userService;

    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .name("name u1")
                .email("u1@mail.ru")
                .build();

        user = UserMapper.toUser(userDto);
    }

    @Test
    void createUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserDto createdUser = userService.createUser(userDto);
        assertEquals(userDto.getId(), createdUser.getId());
        assertEquals(userDto.getEmail(), createdUser.getEmail());
        assertEquals(userDto.getName(), createdUser.getName());
        verify(userRepository).save(any(User.class));
    }

}
