package ru.practicum.shareit.user.UserDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class UserDto {
    private String name;
    @Email(message = "Некорректное указание E-mail")
    @NotBlank(message = "email должен быть указан")
    private String email;
}
