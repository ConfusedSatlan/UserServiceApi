package org.userservice.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.userservice.common.validation.impl.BirthValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = BirthValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BirthValidation {
    String message() default "User must be at least 18 years old to register.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
