package ru.practicum.shareit.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = EmailRepeatValidator.class) // указывает, что аннотация, к которой она применяется, является аннотацией валидации
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailRepeatValid {
    String message() default "Данный e-mail уже зарегестрирован";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default {};
}
