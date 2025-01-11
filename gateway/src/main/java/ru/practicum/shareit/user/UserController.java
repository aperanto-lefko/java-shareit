package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.shareit.user.UserDto.UserDto;

@Controller
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserClient client;
    private static final String USER_ID_REQUEST_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDto userDto) {
        log.info("Создание  пользователем с именем {}, email {}", userDto.getName(), userDto.getEmail());
        return client.createUser(userDto);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable Long id) {
        return client.getUser(id); }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody UserDto userDto, @PathVariable Long id) {
                return client.updateUser(id, userDto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        return client.deleteUser(id);
    }
}