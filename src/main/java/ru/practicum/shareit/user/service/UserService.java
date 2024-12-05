package ru.practicum.shareit.user.service;


import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserService {
    UserDto create(User user);
    boolean isEmailRegistered(String email);
}
