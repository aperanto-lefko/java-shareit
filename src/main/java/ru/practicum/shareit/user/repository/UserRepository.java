package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

public interface UserRepository {
    User create(User user);
    boolean isEmailRegistered(String email);
}
