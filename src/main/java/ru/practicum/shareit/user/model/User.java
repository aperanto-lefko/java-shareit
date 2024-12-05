package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validate.EmailRepeatValid;

@Data
@Builder
public class User {
    private Long id;  //уникальный идентификатор пользователя
    private String name; // имя или логин пользователя
    @Email(message = "Некорректное указание E-mail")
    @NotBlank(message = "email должен быть указан")
    @EmailRepeatValid
    private String email;  //адрес электронной почты, проверка на уникальность

}
