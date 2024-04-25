package org.userservice.common.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.userservice.common.validation.BirthValidation;

import java.time.LocalDate;
import java.time.Period;

public class BirthValidator implements ConstraintValidator<BirthValidation, LocalDate> {

    @Value("${common.ageLimiter}")
    private int ageLimiter;

    @Override
    public boolean isValid(LocalDate birthDate,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (birthDate == null) {
            return true;
        }
        if (!isAdult(birthDate)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("User must be at least " + ageLimiter + " years old to register.")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean isAdult(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(birthDate, currentDate);
        return age.getYears() >= ageLimiter;
    }
}
