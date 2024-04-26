package org.userservice.common.model.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Past;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.userservice.common.validation.BirthValidation;
import org.userservice.common.validation.EmailValidation;
import org.userservice.common.validation.PhoneNumberValidation;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto implements Serializable {

    Long id;

    @EmailValidation
    String email;

    String firstName;

    String lastName;

    @Past(message = "Birth Date cannot be greater than present")
    @BirthValidation
    LocalDate birthDate;

    String address;

    @PhoneNumberValidation
    String phoneNumber;
}
