package ru.practicum.shareit.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.service.UserService;

@RequiredArgsConstructor
public class UserIdValidator implements ConstraintValidator<UserIdValid, Long> {
    private final UserService userService;

    @Override
    public void initialize(UserIdValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        return id != 0 && userService.isUserRegistered(id);
    }
}
