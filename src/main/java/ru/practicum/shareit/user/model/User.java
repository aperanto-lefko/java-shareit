package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validate.annotationGroup.CreateGroup;
import ru.practicum.shareit.validate.EmailRepeatValid;
import ru.practicum.shareit.validate.annotationGroup.SearchGroup;
import ru.practicum.shareit.validate.UserIdValid;

@Data
@Builder
public class User {
    private Long id;  //уникальный идентификатор пользователя
    private String name; // имя или логин пользователя
    @Email(message = "Некорректное указание E-mail", groups = {CreateGroup.class})
    @NotBlank(message = "email должен быть указан",groups = {CreateGroup.class})
    @EmailRepeatValid(groups = CreateGroup.class)
    private String email;  //адрес электронной почты, проверка на уникальность

}
