package ru.practicum.shareit.user.repository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> userStorage;
    private static Long id = 0L;

    public User create(User user) {
        user.setId(++id);
        log.info("Добавление пользователя {} ", user);
        userStorage.put(user.getId(), user);
        return user;
    }
    public boolean isEmailRegistered(String email) {
        return userStorage.values().stream()
                .map(User::getEmail)
                .anyMatch(k->k.equals(email));
    }

}
