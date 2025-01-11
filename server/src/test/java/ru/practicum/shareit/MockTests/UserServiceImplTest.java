package ru.practicum.shareit.MockTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.InvalidEmailException;
import ru.practicum.shareit.exception.InvalidUserIdException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UpdateUserRequest;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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

    private UserDto userDto, expectedUserDto;
    private User user, updatedUser;
    private UpdateUserRequest request;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(1L)
                .name("name u1")
                .email("u1@mail.ru")
                .build();
        request = UpdateUserRequest.builder()
                .id(1L)
                .name("name updated")
                .email("updated@mail.ru")
                .build();
        updatedUser = User.builder()
                .id(1L)
                .name("name updated")
                .email("updated@mail.ru")
                .build();
        expectedUserDto = UserDto.builder()
                .id(1L)
                .name("name updated")
                .email("updated@mail.ru")
                .build();
        user = User.builder()
                .id(1L)
                .name("name u1")
                .email("u1@mail.ru")
                .build();

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

    @Test
    void updateUserWhenEmailAlreadyRegistered() {
        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        when(userService.isEmailRegistered(request.getEmail()))
                .thenReturn(true);
        assertThrows(InvalidEmailException.class, () -> userService.updateUser(request));
    }

    @Test
    void updateUserEmailIsNotRegistered() {
        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        when(userService.isEmailRegistered(request.getEmail()))
                .thenReturn(false);
        when(userRepository.save(any(User.class)))
                .thenReturn(updatedUser);
        UserDto actualUserDto = userService.updateUser(request);

        verify(userRepository).save(updatedUser);
        assertEquals(expectedUserDto, actualUserDto);
    }

    @Test
    void updateUserWhenUserDoesNotExist() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        assertThrows(InvalidUserIdException.class, () -> userService.updateUser(request));
        verify(userRepository).findById(user.getId()); //метод был вызван
        verify(userRepository, never()).save(any(User.class)); //метод не был вызван
    }
}
