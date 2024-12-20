package ru.practicum.shareit.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.service.UserService;


@RequiredArgsConstructor

public class EmailRepeatValidator implements ConstraintValidator<EmailRepeatValid, String> {
    private final UserService userService;

    @Override
    public void initialize(EmailRepeatValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !userService.isEmailRegistered(email);
    }
}
