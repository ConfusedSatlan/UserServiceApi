package org.userservice.common.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.userservice.common.validation.PhoneNumberValidation;

import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberValidation, String> {
    private static final String PHONE_NUMBER_PATTERN = "(^$|[0-9]{10})";

    @Override
    public boolean isValid(String phoneNumber,
                           ConstraintValidatorContext constraintValidatorContext) {
        return phoneNumber == null ? true : Pattern.compile(PHONE_NUMBER_PATTERN)
                .matcher(phoneNumber).matches();
    }
}
