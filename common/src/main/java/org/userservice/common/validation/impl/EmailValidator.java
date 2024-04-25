package org.userservice.common.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.userservice.common.validation.EmailValidation;

import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<EmailValidation, String> {
    private static final String EMAIL_PATTERN = ".+@.+\\..+";

    @Override
    public boolean isValid(String email,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (email == null) {
            return true;
        }
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }
}
