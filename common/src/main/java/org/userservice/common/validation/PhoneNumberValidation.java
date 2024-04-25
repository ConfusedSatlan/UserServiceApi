package org.userservice.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.userservice.common.validation.impl.PhoneNumberValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumberValidation {
    String message() default "Invalid format phone number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
