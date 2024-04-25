package org.userservice.common.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.userservice.common.validation.BirthValidation;
import org.userservice.common.validation.EmailValidation;
import org.userservice.common.validation.PhoneNumberValidation;

import java.io.Serializable;
import java.time.LocalDate;

public record CreateRequestUserDto(
        @NotEmpty(message = "Email can't be empty")
        @EmailValidation
        String email,

        @NotEmpty(message = "First Name can't be empty")
        String firstName,

        @NotEmpty(message = "Last Name can't be empty")
        String lastName,
        @NotNull(message = "Birth Date can't be null")
        @Past(message = "Birth Date cannot be greater than present")
        @BirthValidation
        LocalDate birthDate,
        String address,

        @PhoneNumberValidation
        String phoneNumber
) implements Serializable {
}
