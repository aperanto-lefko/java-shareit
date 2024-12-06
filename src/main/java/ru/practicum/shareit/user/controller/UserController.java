package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UpdateUserRequest;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validate.UserIdValid;
import ru.practicum.shareit.validate.annotationGroup.CreateGroup;
import ru.practicum.shareit.validate.annotationGroup.SearchGroup;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto getUser(@UserIdValid @PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping
    public UserDto create(@Validated(CreateGroup.class) @RequestBody User user) {
        return userService.createUser(user); //создание нового пользователя
    }

    @DeleteMapping("/{id}")
    public void delete(@UserIdValid @PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PatchMapping("/{id}")
    public UserDto update(@RequestBody UpdateUserRequest request, @UserIdValid @PathVariable Long id) {
        request.setId(id);
        return userService.updateUser(request);
    }

}
