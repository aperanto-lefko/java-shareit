package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> userStorage;
    private static Long id = 0L;

    public User createUser(User user) {
        user.setId(++id);
        log.info("Добавление пользователя {} ", user);
        userStorage.put(user.getId(), user);
        log.info("Пользователь {} добавлен в базу данных", user);
        return user;
    }

    public Optional<User> getUserById(Long id) {
        log.info("Поиск пользователя с id {}", id);
        return Optional.ofNullable(userStorage.get(id));
    }

    public void updateUser(User user) {
        log.info("Обновление пользователя с id {}", user.getId());
        userStorage.put(user.getId(), user);
        log.info("Обновленный Пользователь {} добавлен в базу данных", user);
    }

    public void deleteUserById(Long id) {
        log.info("Удаление пользователя с id {} ", id);
        userStorage.remove(id);
        log.info("Пользователь с id {} удален", id);
    }

    public boolean isEmailRegistered(String email) {
        return userStorage.values().stream()
                .map(User::getEmail)
                .anyMatch(k -> k.equals(email));
    }

}
