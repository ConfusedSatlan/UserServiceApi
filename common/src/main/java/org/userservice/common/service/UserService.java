package org.userservice.common.service;

import org.userservice.common.model.dto.CreateRequestUserDto;
import org.userservice.common.model.dto.UserDto;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    UserDto createUser(CreateRequestUserDto userDto);

    UserDto getUserById(Long userId);

    UserDto updateUser(Long userId, UserDto userDto);

    UserDto updateAllUser(Long userId, CreateRequestUserDto userDto);

    boolean deleteUser(Long userId);

    List<UserDto> searchUsersByBirthDateRange(LocalDate fromDate, LocalDate toDate);
}
