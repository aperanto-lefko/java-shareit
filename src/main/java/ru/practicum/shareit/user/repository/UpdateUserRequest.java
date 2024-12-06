package ru.practicum.shareit.user.repository;

import lombok.Data;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;


@Data
public class UpdateUserRequest {

    private Long id;
    private String name;
    private String email;

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasEmail() {
        return !(email == null || email.isBlank());
    }


}
