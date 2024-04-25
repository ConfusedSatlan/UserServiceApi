package org.userservice.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.userservice.common.validation.impl.EmailValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailValidation {
    String message() default "Invalid format email";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
