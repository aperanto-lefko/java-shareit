package ru.practicum.shareit.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = DataInPastValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataInPastValid {
    String message() default "Дата не может быть в прошлом";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
