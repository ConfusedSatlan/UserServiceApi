package org.userservice.common.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.userservice.common.validation.BirthValidation;

import java.time.LocalDate;
import java.time.Period;

public class BirthValidator implements ConstraintValidator<BirthValidation, LocalDate> {

    @Override
    public boolean isValid(LocalDate birthDate,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (birthDate == null) {
            return true;
        }
        return isAdult(birthDate);
    }

    private boolean isAdult(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(birthDate, currentDate);
        return age.getYears() >= 18;
    }
}
